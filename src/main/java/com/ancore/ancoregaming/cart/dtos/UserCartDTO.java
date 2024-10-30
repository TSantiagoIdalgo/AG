package com.ancore.ancoregaming.cart.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCartDTO {

  private UUID id;
  private BigDecimal total;
  private List<CartItemDTO> items;

  @Override
  public String toString() {
    return "UserCartDTO{" + "id=" + id + ", total=" + total + ", items=" + items + '}';
  }

}
