package com.ancore.ancoregaming.checkout.dtos;

import com.ancore.ancoregaming.cart.dtos.CartItemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CheckoutItemsDTO {
  private Long id;
  private CartItemDTO cartItem;
}
