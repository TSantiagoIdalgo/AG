package com.ancore.ancoregaming.checkout.controllers;

import com.ancore.ancoregaming.checkout.dtos.CheckoutDTO;
import com.ancore.ancoregaming.checkout.model.Checkout;
import com.ancore.ancoregaming.checkout.services.SseService;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
  
  @Autowired
  private CheckoutService checkoutService;
  @Value("${stripe.webhook.secret}")
  private String endpointSecret;
  @Autowired
  private SseService sseService;
  private final ModelMapper modelMapper = new ModelMapper();
  
  @GetMapping("/user")
  public ApiEntityResponse<List<CheckoutDTO>> getUserCheckouts(@AuthenticationPrincipal UserDetails userDetails) throws BadRequestException {
    if (userDetails == null || userDetails.getUsername() == null) {
      throw new BadRequestException("UNAUTHENTICATED");
    }
    List<Checkout> checkouts = this.checkoutService.getUserCheckouts(userDetails);
    var checkoutType = new TypeToken<List<CheckoutDTO>>() {};
    List<CheckoutDTO> checkoutDTO = modelMapper.map(checkouts, checkoutType.getType());
    ApiResponse<List<CheckoutDTO>> response = new ApiResponse<>(checkoutDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }
  
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
  
  @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<Object>> stream (@AuthenticationPrincipal UserDetails userDetails) throws BadRequestException {
    if (userDetails != null && userDetails.getUsername() != null) {
      return this.sseService.addClient(userDetails.getUsername());
    };
   return Flux.empty();
  }
}
