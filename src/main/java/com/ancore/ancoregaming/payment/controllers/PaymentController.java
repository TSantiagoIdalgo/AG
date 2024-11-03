package com.ancore.ancoregaming.payment.controllers;

import com.ancore.ancoregaming.payment.services.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
public class PaymentController {

  @Autowired
  private CheckoutService checkoutService;

  @PostMapping("/create-checkout-session")
  public String createCheckoutSession(@AuthenticationPrincipal UserDetails user) throws StripeException {

    Session session = this.checkoutService.createCheckoutSession(user);
    return session.getUrl();
  }
}
