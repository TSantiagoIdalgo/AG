package com.ancore.ancoregaming.review.dtos;

import com.ancore.ancoregaming.product.dtos.RequirementsDTO;
import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.model.Platform;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ReviewProductDTO {
  public UUID id;
  public String name;
  public BigDecimal price;
  public BigDecimal discount;
  public String trailer;
  public String mainImage;
  
  public ReviewProductDTO() {
  }
  
  public ReviewProductDTO(UUID id, String name, BigDecimal price, BigDecimal discount, String trailer, String mainImage) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.discount = discount;
    this.trailer = trailer;
    this.mainImage = mainImage;
  }
  
  @Override
  public String toString() {
    return "ReviewProductDTO{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", price=" + price +
        ", discount=" + discount +
        ", trailer='" + trailer + '\'' +
        ", mainImage='" + mainImage + '\'' +
        '}';
  }
}
