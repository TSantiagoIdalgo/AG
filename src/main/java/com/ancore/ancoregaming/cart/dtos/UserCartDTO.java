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
  private BigDecimal subtotal;
  private BigDecimal total;
  private List<CartItemDTO> items;

  public UserCartDTO() {
  }

  public UserCartDTO(UUID id, BigDecimal subtotal, BigDecimal total, List<CartItemDTO> items) {
    this.id = id;
    this.subtotal = subtotal;
    this.total = total;
    this.items = items;
  }

  @Override
  public String toString() {
    return "UserCartDTO{" + "id=" + id + ", subtotal=" + subtotal + ", total=" + total + ", items=" + items + '}';
  }

}
