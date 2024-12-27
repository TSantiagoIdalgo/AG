package com.ancore.ancoregaming.cart.dtos;

import com.ancore.ancoregaming.product.dtos.ProductDTO;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {

  private UUID id;
  private ProductDTO product;
  private int quantity;
  private BigDecimal total;

  private BigDecimal subtotal;
  private boolean itemIsPaid;

  public CartItemDTO() {
  }

  public CartItemDTO(UUID id, ProductDTO product, int quantity, BigDecimal total, BigDecimal subtotal, boolean itemIsPaid) {
    this.id = id;
    this.product = product;
    this.quantity = quantity;
    this.total = total;
    this.subtotal = subtotal;
    this.itemIsPaid = itemIsPaid;
  }

  @Override
  public String toString() {
    return "CartItemDTO{" + "id=" + id + ", product=" + product + ", cuantity=" + quantity + ", total=" + total + ", subtotal=" + subtotal + ", itemIsPaid=" + itemIsPaid + '}';
  }

}
