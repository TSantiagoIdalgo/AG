package com.ancore.ancoregaming.checkout.dtos;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class CheckoutItemDTO {
  public UUID id;
  public CheckoutProductDTO product;
  public int quantity;
  public BigDecimal total;
  public BigDecimal subtotal;
  public boolean itemIsPaid;
  
}
