package com.ancore.ancoregaming.payment.dtos;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutSessionRequest {

  private UUID cartId;
  private String successUrl;
  private String cancelUrl;

  public CheckoutSessionRequest() {
  }

  public CheckoutSessionRequest(UUID cartId, String successUrl, String cancelUrl) {
    this.cartId = cartId;
    this.successUrl = successUrl;
    this.cancelUrl = cancelUrl;
  }

}
