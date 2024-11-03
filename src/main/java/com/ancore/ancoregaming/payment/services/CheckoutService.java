package com.ancore.ancoregaming.payment.services;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.services.ICartService;
import com.ancore.ancoregaming.product.model.Product;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

  @Autowired
  private ICartService cartService;

  public Session createCheckoutSession(UserDetails userDetails) throws StripeException {
    Cart userCart = this.cartService.getUserCart(userDetails);
    Map<String, Object> params = new HashMap<>();

    List<Object> lineItems = new ArrayList<>();
    userCart.getItems().forEach((item) -> {
      lineItems.add(this.getProductData(item.getProduct(), item.getCuantity()));
    });
    params.put("mode", "payment");
    params.put("line_items", lineItems);
    params.put("customer_email", userDetails.getUsername());
    params.put("payment_method_types", List.of("card"));
    params.put("success_url", "https://www.google.com");
    params.put("cancel_url", "https://www.google.com");

    return Session.create(params);
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
