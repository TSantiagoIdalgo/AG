package com.ancore.ancoregaming.checkout.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CheckoutItemsDTO {
  public Long id;
  public CheckoutCartItemDTO cartItem;
}
