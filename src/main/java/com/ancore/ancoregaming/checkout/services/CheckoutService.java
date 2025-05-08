package com.ancore.ancoregaming.checkout.services;

import com.ancore.ancoregaming.cart.dtos.UserCartDTO;
import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.model.CartItem;
import com.ancore.ancoregaming.cart.repositories.ICartRepository;
import com.ancore.ancoregaming.checkout.dtos.CheckoutDTO;
import com.ancore.ancoregaming.checkout.dtos.CheckoutItemDTO;
import com.ancore.ancoregaming.checkout.dtos.CheckoutProductDTO;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

  private final ICartRepository cartRepository;
  private final ICheckoutRepository paymentRepository;
  private final StockReservationService stockReservationService;
  private final ICheckoutItemsRepository checkoutItemsRepository;
  private final EmailService emailService;
  private final SseService sseService;
  private final ModelMapper modelMapper = new ModelMapper();
  
  @Autowired
  public CheckoutService(ICartRepository cartRepository, ICheckoutRepository paymentRepository, StockReservationService stockReservationService, ICheckoutItemsRepository checkoutItemsRepository, EmailService emailService, SseService sseService) {
    this.cartRepository = cartRepository;
    this.paymentRepository = paymentRepository;
    this.stockReservationService = stockReservationService;
    this.checkoutItemsRepository = checkoutItemsRepository;
    this.emailService = emailService;
    this.sseService = sseService;
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
    params.put("success_url", "http://localhost:5173/ancore/user/activation");
    params.put("cancel_url", "http://localhost:5173/ancore/user/activation_failed");
    long expiresAt = (System.currentTimeMillis() / 1000L) + (30 * 60);
    params.put("expires_at", expiresAt);
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


          List<CartItem> cartItems = userCart.getItems();
          cartItems.forEach(item -> {
            item.setItemIsPaid(true);
            item.setPaidAt(new Date());
            item.setPaymentStatus("paid");
          });
          this.generatePaymentReceipt(sessionNode, userCart);
          userCart.setTotal(BigDecimal.ZERO);
          userCart.setSubtotal(BigDecimal.ZERO);
          userCart.getUser().getStockReservation()
              .forEach((reservation) -> this.stockReservationService.confirmPayment(reservation.getId()));
          
          UserCartDTO cart = modelMapper.map(userCart, UserCartDTO.class);
          List<CheckoutProductDTO> checkoutProductDTOs = cartItems.stream()
              .map(item -> {
                System.out.println(item.getId());
                CheckoutProductDTO productDTO = modelMapper.map(item.getProduct(), CheckoutProductDTO.class);
                System.out.println(productDTO);
                List<CheckoutItemDTO> itemDTO = List.of(modelMapper.map(item, CheckoutItemDTO.class));
                productDTO.setCartItems(itemDTO);
                return productDTO;
              })
              .toList();

          TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
              sendEmail(userEmail, userCart);
              sseService.sendToClient(userCart.getUser(), SseService.EVENT_TYPES.PAYMENT, cart);
              checkoutProductDTOs.forEach(dto -> {
                sseService.broadcast(SseService.EVENT_TYPES.NEW_PAYMENT_RECEIVED, dto);
              });
            }
          });
        }
      }
    } catch (OptimisticLockException e) {
      throw new ConcurrencyFailureException("A concurrency conflict occurred. Try again.");
    }
  }
  
  public List<Checkout> getUserCheckouts (UserDetails userDetails) {
    final String userEmail = userDetails.getUsername();
    List<Checkout> checkout = this.paymentRepository.findByUserEmail(userEmail);
    if (checkout == null || checkout.isEmpty()) {
      throw new EntityNotFoundException("The user has no payments");
    }
    
    return checkout;
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
    purchaseEmailDTO.setProductsQuantity(productsMessage + productCuantityString);
    purchaseEmailDTO.setTotal(cart.getTotal());
    purchaseEmailDTO.setUsername(cart.getUser().getUsername());
    this.emailService.sendPurchaseMail(purchaseEmailDTO);
  }
  
  public List<Checkout> findAll(int pageSize, int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    return this.paymentRepository.findAllOrdered(pageRequest);
  }
  
  public List<Product> findProductsCheckout(int pageSize, int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime sixMonthsAgo = now.minusMonths(6);
    return this.paymentRepository.findProductWithCartItemsPaid(sixMonthsAgo, now, pageRequest);
  }
  
}
