package com.ancore.ancoregaming.cart.dtos;

import com.ancore.ancoregaming.product.dtos.ProductDTO;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {

  private UUID id;
  private ProductDTO product;
  private int cuantity;
  private double price;
  private boolean itemIsPaid;

  public CartItemDTO() {
  }

  public CartItemDTO(UUID id, ProductDTO product, int cuantity, double price, boolean itemIsPaid) {
    this.id = id;
    this.product = product;
    this.cuantity = cuantity;
    this.price = price;
    this.itemIsPaid = itemIsPaid;
  }

  @Override
  public String toString() {
    return "CartItemDTO{" + "id=" + id + ", product=" + product + ", cuantity=" + cuantity + ", price=" + price + ", itemIsPaid=" + itemIsPaid + '}';
  }

}
