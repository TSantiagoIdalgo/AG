package com.ancore.ancoregaming.checkout.controllers;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ancore.ancoregaming.checkout.dtos.CheckoutSessionDTO;
import com.ancore.ancoregaming.checkout.services.CheckoutService;
import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.common.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

  @Autowired
  private CheckoutService checkoutService;
  @Value("${stripe.webhook.secret}")
  private String endpointSecret;

  @PostMapping("/create-checkout-session")
  public ApiEntityResponse<CheckoutSessionDTO> createCheckoutSession(@AuthenticationPrincipal UserDetails user)
      throws StripeException {
    Session session = this.checkoutService.createCheckoutSession(user);
    ApiResponse<CheckoutSessionDTO> response = new ApiResponse<>(new CheckoutSessionDTO(session.getUrl()), null);
    return ApiEntityResponse.of(HttpStatus.CREATED, response);
  }

  @PostMapping("/webhook")
  public ApiEntityResponse<?> handleStripeWebhook(@RequestBody String payload,
      @RequestHeader("Stripe-Signature") String sigHeader) {
    Event event;
    try {
      event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (SignatureVerificationException e) {
      ExceptionResponse error = new ExceptionResponse(400, "Error verifying webhook", e.getMessage());
      ApiResponse<ExceptionResponse> response = new ApiResponse<>(null, error);
      return ApiEntityResponse.of(HttpStatus.BAD_REQUEST, response);
    }

    try {
        if (!event.getType().equals("checkout.session.completed")) {
          throw new BadRequestException("Invalid webhook event");
        }
        this.checkoutService.checkoutSessionComplete(payload);
    } catch (JsonProcessingException | BadRequestException e) {
      ExceptionResponse error = new ExceptionResponse(400, "Error verifying webhook", e.getMessage());
      ApiResponse<ExceptionResponse> response = new ApiResponse<>(null, error);
      return ApiEntityResponse.of(HttpStatus.BAD_REQUEST, response);
    }

    return ApiEntityResponse.of(HttpStatus.OK, new ApiResponse<>(null, null));
  }
}
