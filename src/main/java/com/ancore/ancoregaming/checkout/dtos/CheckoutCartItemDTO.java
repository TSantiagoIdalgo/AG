package com.ancore.ancoregaming.checkout.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class CheckoutCartItemDTO {
  public UUID id;
  public int quantity;
  public BigDecimal total;
  public BigDecimal subtotal;
  public boolean itemIsPaid;
  public String paymentStatus;
  public Date paidAt;
  public ProductCheckoutDTO product;
}
