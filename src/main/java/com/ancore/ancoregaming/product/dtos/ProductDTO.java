package com.ancore.ancoregaming.product.dtos;

import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.model.Platform;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

  public UUID id;
  public String name;
  public String description;
  public List<Platform> platforms;
  public String developer;
  public List<Genre> genres;
  public List<String> tags;
  public boolean disabled;
  public int stock;
  public BigDecimal price;
  public BigDecimal discount;
  private List<String> images;
  private String backgroundImage;
  private String trailer;
  private String mainImage;

  @Override
  public String toString() {
    return "ProductDTO{" + "name=" + name + ", platforms=" + platforms + ", developer=" + developer + ", genres=" + genres + ", disabled=" + disabled + ", stock=" + stock + ", price=" + price + ", discount=" + discount + '}';
  }

}
