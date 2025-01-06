package com.ancore.ancoregaming.checkout.services;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.model.CartItem;
import com.ancore.ancoregaming.cart.repositories.ICartRepository;
import com.ancore.ancoregaming.checkout.model.Checkout;
import com.ancore.ancoregaming.checkout.model.CheckoutItems;
import com.ancore.ancoregaming.checkout.repositories.ICheckoutItemsRepository;
import com.ancore.ancoregaming.checkout.repositories.ICheckoutRepository;
import com.ancore.ancoregaming.email.dtos.PurchaseEmailDTO;
import com.ancore.ancoregaming.email.services.EmailService;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.user.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

  private final ICartRepository cartRepository;
  private final ICheckoutRepository paymentRepository;
  private final StockReservationService stockReservationService;
  private final ICheckoutItemsRepository checkoutItemsRepository;
  private final EmailService emailService;
  
  @Autowired
  public CheckoutService(ICartRepository cartRepository, ICheckoutRepository paymentRepository, StockReservationService stockReservationService, ICheckoutItemsRepository checkoutItemsRepository, EmailService emailService) {
    this.cartRepository = cartRepository;
    this.paymentRepository = paymentRepository;
    this.stockReservationService = stockReservationService;
    this.checkoutItemsRepository = checkoutItemsRepository;
    this.emailService = emailService;
  }
  
  @Transactional
  public Session createCheckoutSession(UserDetails userDetails) throws StripeException {
    Cart userCart = this.cartRepository.findByUserEmailAndUnpaidItems(userDetails.getUsername());
    if (userCart == null) {
      throw new EntityNotFoundException("User cart not found");
    }
    Map<String, Object> params = new HashMap<>();

    List<Object> lineItems = new ArrayList<>();
    userCart.getItems().forEach((item) -> lineItems.add(this.getProductData(userCart.getUser(), item.getProduct(), item.getQuantity())));
    params.put("mode", "payment");
    params.put("line_items", lineItems);
    params.put("metadata", Map.of("cartId", userCart.getId()));
    params.put("customer_email", userDetails.getUsername());
    params.put("payment_method_types", List.of("card"));
    params.put("success_url", "https://www.google.com");
    params.put("cancel_url", "https://www.google.com");

    return Session.create(params);
  }

  @Transactional
  public void checkoutSessionComplete(String payload) throws JsonProcessingException {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(payload);
      JsonNode sessionNode = jsonNode.get("data").get("object");
      if (sessionNode != null) {
        String paymentStatus = sessionNode.get("payment_status").asText();
        JsonNode metadataNode = sessionNode.get("metadata");
        if (metadataNode != null && metadataNode.has("cartId") && "paid".equals(paymentStatus)) {
          String cartId = metadataNode.get("cartId").asText();
          String userEmail = sessionNode.get("customer_email").asText();
          Cart userCart = this.cartRepository.findCartByIdWithoutItemsUnpaid(UUID.fromString(cartId))
              .orElseThrow(() -> new EntityNotFoundException("User cart not found"));

          this.sendEmail(userEmail, userCart);

          List<CartItem> cartItems = userCart.getItems();
          cartItems.forEach(item -> {
            Product product = item.getProduct();
            if (product == null) {
              throw new EntityNotFoundException("Product not found");
            }
            int newStock = product.getStock() - item.getQuantity();
            if (newStock < 0) {
              throw new IllegalStateException("Not enough stock for product: " + product.getId());
            }
            product.setStock(newStock);

            item.setItemIsPaid(true);

          });

          this.generatePaymentReceipt(sessionNode, userCart);
          userCart.setTotal(BigDecimal.ZERO);
          userCart.setSubtotal(BigDecimal.ZERO);
          userCart.getUser().getStockReservation()
              .forEach((reservation) -> this.stockReservationService.confirmPayment(reservation.getId()));

        }
      }
    } catch (OptimisticLockException e) {
      throw new ConcurrencyFailureException("A concurrency conflict occurred. Try again.");
    }
  }

  private void generatePaymentReceipt(JsonNode sessionNode, Cart userCart) {
    String stripePaymentId = sessionNode.get("id").asText();
    String status = sessionNode.get("payment_status").asText();
    String currency = sessionNode.get("currency").asText();

    Checkout checkout = new Checkout.Builder(stripePaymentId)
        .setTotal(userCart.getTotal())
        .setSubtotal(userCart.getSubtotal())
        .setCurrency(currency)
        .setPaymentStatus(status)
        .setUser(userCart.getUser())
        .setItems(new ArrayList<>())
        .build();

    this.paymentRepository.save(checkout);
    List<CheckoutItems> checkoutItems = userCart.getItems()
        .stream()
        .map((item) -> this.checkoutItemsRepository.save(new CheckoutItems(item, checkout)))
        .collect(Collectors.toList());
    checkout.setCheckoutItems(checkoutItems);

    this.paymentRepository.save(checkout);
  }

  private Map<String, Object> getProductData(User user, Product product, int quantity) {
    Map<String, Object> lineItem = Map.of(
        "price_data", Map.of(
            "currency", "usd",
            "product_data", Map.of(
                "name", product.getName(),
                "images", Collections.singletonList(product.getMainImage())),
            "unit_amount", this.getFinalPrice(product.getPrice(), product.getDiscount())),
        "quantity", quantity);

    this.stockReservationService.createReservation(quantity, user, product);
    return lineItem;
  }

  private long convertToCents(BigDecimal amount) {
    BigDecimal cents = amount.multiply(BigDecimal.valueOf(100));

    return cents.setScale(0, RoundingMode.CEILING).longValue();
  }

  private long getFinalPrice(BigDecimal amount, BigDecimal d) {
    BigDecimal percentage = new BigDecimal("100");
    BigDecimal discount = percentage.subtract(d), multiplyAmount = amount.multiply(discount);
    BigDecimal totalAmount = multiplyAmount.divide(percentage);

    return convertToCents(totalAmount);
  }

  private void sendEmail(String userEmail, Cart cart) {
    PurchaseEmailDTO purchaseEmailDTO = new PurchaseEmailDTO();
    String productsMessage = cart.getItems().get(0).getProduct().getName();
    String productCuantityString = cart.getItems().size() > 1
        ? " and another " + (cart.getItems().size() - 1) + " products"
        : "";
    purchaseEmailDTO.setAddressee(userEmail);
    purchaseEmailDTO.setItems(cart.getItems());
    purchaseEmailDTO.setProductsQuantity("$" + (productsMessage + productCuantityString));
    purchaseEmailDTO.setTotal(cart.getTotal());
    purchaseEmailDTO.setUsername(cart.getUser().getUsername());
    this.emailService.sendPurchaseMail(purchaseEmailDTO);
  }
}
