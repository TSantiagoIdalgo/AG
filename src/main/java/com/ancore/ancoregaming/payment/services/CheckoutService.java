package com.ancore.ancoregaming.payment.services;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.model.CartItem;
import com.ancore.ancoregaming.cart.repositories.ICartRepository;
import com.ancore.ancoregaming.payment.model.Payment;
import com.ancore.ancoregaming.payment.repositories.IPaymentRepository;
import com.ancore.ancoregaming.product.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

  @Autowired
  private ICartRepository cartRepository;
  @Autowired
  private IPaymentRepository paymentRepository;

  public Session createCheckoutSession(UserDetails userDetails) throws StripeException {
    Cart userCart = this.cartRepository.findByUserEmailAndUnpaidItems(userDetails.getUsername());
    Map<String, Object> params = new HashMap<>();

    List<Object> lineItems = new ArrayList<>();
    userCart.getItems().forEach((item) -> {
      lineItems.add(this.getProductData(item.getProduct(), item.getCuantity()));
    });
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
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(payload);
    JsonNode sessionNode = jsonNode.get("data").get("object");

    if (sessionNode != null) {
      String paymentStatus = sessionNode.get("payment_status").asText();
      String sessionId = sessionNode.get("id").asText();
      JsonNode metadataNode = sessionNode.get("metadata");

      if (metadataNode != null && metadataNode.has("cartId") && "paid".equals(paymentStatus)) {
        String cartId = metadataNode.get("cartId").asText();
        Cart userCart = this.cartRepository.findCartByIdWithoutItemsUnpaid(UUID.fromString(cartId))
                .orElseThrow(() -> new EntityNotFoundException("User cart not found"));
        List<CartItem> cartItems = userCart.getItems();

        cartItems.forEach(item -> {
          Product product = item.getProduct();
          if (product == null) {
            throw new EntityNotFoundException("Product not found");
          }
          int newStock = product.getStock() - item.getCuantity();
          if (newStock < 0) {
            throw new IllegalStateException("Not enough stock for product: " + product.getId());
          }
          product.setStock(newStock);

          item.setItemIsPaid(true);

        });
        this.generatePaymentReceipt(sessionNode, userCart);
        userCart.setTotal(BigDecimal.ZERO);
        userCart.setSubtotal(BigDecimal.ZERO);
        System.out.println("PAGO EXITOSO CON SESSIONID: " + sessionId);
      }
    }
  }

  private Payment generatePaymentReceipt(JsonNode sessionNode, Cart userCart) {
    String stripePaymentId = sessionNode.get("id").asText();
    String status = sessionNode.get("payment_status").asText();
    String currency = sessionNode.get("currency").asText();

    Payment payment = new Payment.Builder(stripePaymentId)
            .setUserEmail(userCart.getUser().getEmail())
            .setPaymentStatus(status)
            .setCartId(userCart.getId())
            .setCurrency(currency)
            .setTotal(userCart.getTotal())
            .setSubtotal(userCart.getSubtotal())
            .build();

    return this.paymentRepository.save(payment);
  }

  private Map getProductData(Product product, int cuantity) {
    Map<String, Object> lineItem = Map.of(
            "price_data", Map.of(
                    "currency", "usd",
                    "product_data", Map.of(
                            "name", product.getName(),
                            "images", Arrays.asList(product.getMainImage()),
                            "description", product.getDescription()
                    ),
                    "unit_amount", this.getFinalPrice(product.getPrice(), product.getDiscount())
            ),
            "quantity", cuantity
    );

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
}
