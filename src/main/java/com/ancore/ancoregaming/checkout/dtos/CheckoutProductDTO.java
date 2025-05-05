package com.ancore.ancoregaming.checkout.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CheckoutProductDTO {
  public UUID id;
  public String name;
  public BigDecimal price;
  public BigDecimal discount;
  public String trailer;
  public String mainImage;
  
  public CheckoutProductDTO() {
  }
  
  public CheckoutProductDTO(UUID id, String name, BigDecimal price, BigDecimal discount, String trailer, String mainImage) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.discount = discount;
    this.trailer = trailer;
    this.mainImage = mainImage;
  }
  
  @Override
  public String toString() {
    return "CheckoutProductDTO{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", price=" + price +
        ", discount=" + discount +
        ", trailer='" + trailer + '\'' +
        ", mainImage='" + mainImage + '\'' +
        '}';
  }
}

