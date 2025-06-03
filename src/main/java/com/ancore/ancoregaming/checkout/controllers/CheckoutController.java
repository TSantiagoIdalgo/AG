package com.ancore.ancoregaming.checkout.controllers;

import com.ancore.ancoregaming.checkout.dtos.*;
import com.ancore.ancoregaming.checkout.model.Checkout;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.user.repositories.IUserRepository;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.ancore.ancoregaming.checkout.services.CheckoutService;
import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
  
  @Autowired
  private CheckoutService checkoutService;
  @Value("${stripe.webhook.secret}")
  private String endpointSecret;
  @Autowired
  private IUserRepository userRepository;
  private final ModelMapper modelMapper = new ModelMapper();
  
  
  @Secured("ROLE_ADMIN")
  @GetMapping("/")
  public ApiEntityResponse<List<CheckoutDTO>> findAllCheckouts(@RequestParam int pageSize, @RequestParam int pageNumber) {
    var checkouts = this.checkoutService.findAll(pageSize, pageNumber);
    var checkoutType = new TypeToken<List<CheckoutDTO>>() {};
    List<CheckoutDTO> checkoutDTO = modelMapper.map(checkouts, checkoutType.getType());
    ApiResponse<List<CheckoutDTO>> response = new ApiResponse<>(checkoutDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }
  
  @Secured("ROLE_ADMIN")
  @GetMapping("/product")
  public ApiEntityResponse<List<CheckoutProductDTO>> findAllProductCheckouts() {
    List<Product> productsCheckout = this.checkoutService.findProductsCheckout();
    var checkoutType = new TypeToken<List<CheckoutProductDTO>>() {};
    List<CheckoutProductDTO> checkoutDTO = modelMapper.map(productsCheckout, checkoutType.getType());

    ApiResponse<List<CheckoutProductDTO>> response = new ApiResponse<>(checkoutDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }
  
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
    Session session = this.checkoutService.createCheckoutSession(user.getUsername());
    ApiResponse<CheckoutSessionDTO> response = new ApiResponse<>(new CheckoutSessionDTO(session.getUrl()), null);
    return ApiEntityResponse.of(HttpStatus.CREATED, response);
  }
  
  @GetMapping("/find/{checkoutId}")
  public ApiEntityResponse<CheckoutDTO> findCheckout(@PathVariable UUID checkoutId) {
    Checkout checkout = this.checkoutService.findCheckoutById(checkoutId);
    CheckoutDTO checkoutDTO = modelMapper.map(checkout, CheckoutDTO.class);
    ApiResponse<CheckoutDTO> response = new ApiResponse<>(checkoutDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
    
  }
  
  @PostMapping("/webhook")
  public void handleStripeWebhook(@RequestBody String payload,
                                                  @RequestHeader("Stripe-Signature") String sigHeader) throws JsonProcessingException {
    Event event;
    try {
      event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (SignatureVerificationException e) {
      return;
    }
    
    if (event.getType().equals("checkout.session.completed")) {
      this.checkoutService.checkoutSessionComplete(payload);
    } else if(event.getType().equals("payment_intent.payment_failed")) {
      this.checkoutService.checkoutSessionFailed(payload);
    }
  }
}
