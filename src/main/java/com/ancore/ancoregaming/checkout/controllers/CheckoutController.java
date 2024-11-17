package com.ancore.ancoregaming.checkout.controllers;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    ApiResponse<CheckoutSessionDTO> response = new ApiResponse<>(HttpStatus.OK,
        new CheckoutSessionDTO(session.getUrl()), null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @PostMapping("/webhook")
  public ApiEntityResponse<?> handleStripeWebhook(@RequestBody String payload,
      @RequestHeader("Stripe-Signature") String sigHeader) throws JsonProcessingException {
    Event event;
    try {
      event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (SignatureVerificationException e) {
      ExceptionResponse error = new ExceptionResponse(400, "Error verifying webhook", e.getMessage());
      ApiResponse<ExceptionResponse> response = new ApiResponse<>(HttpStatus.BAD_REQUEST, null, error);
      return ApiEntityResponse.of(HttpStatus.OK, response);
    }

    try {
      switch (event.getType()) {
        case "checkout.session.completed" ->
          this.checkoutService.checkoutSessionComplete(payload);
        default ->
          createErrorResponse("Invalid event type in webhook", HttpStatus.BAD_REQUEST);
      }
    } catch (JsonProcessingException e) {
      ExceptionResponse error = new ExceptionResponse(400, "Error verifying webhook", e.getMessage());
      ApiResponse<ExceptionResponse> response = new ApiResponse<>(HttpStatus.BAD_REQUEST, null, error);
      return ApiEntityResponse.of(HttpStatus.BAD_REQUEST, response);
    }

    return ApiEntityResponse.of(HttpStatus.OK, new ApiResponse<>(HttpStatus.OK, null, null));
  }

  private ResponseEntity<ApiResponse<?>> createErrorResponse(String message, HttpStatus status) {
    ExceptionResponse error = new ExceptionResponse(status.value(), message, null);
    ApiResponse<ExceptionResponse> response = new ApiResponse<>(status, null, error);
    return ResponseEntity.status(status).body(response);
  }
}
