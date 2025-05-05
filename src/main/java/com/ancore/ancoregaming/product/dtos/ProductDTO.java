package com.ancore.ancoregaming.product.dtos;

import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.model.Platform;
import java.math.BigDecimal;
import java.time.LocalDate;
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
  public List<RequirementsDTO> requirements;
  public boolean disabled;
  public int stock;
  public BigDecimal price;
  public BigDecimal discount;
  public List<String> images;
  public String backgroundImage;
  public String trailer;
  public String mainImage;
  public LocalDate release_date;
  public String distributor;
  public String pegi;
  
  public ProductDTO() {
  }
  
  public ProductDTO(UUID id, String name, String description, List<Platform> platforms, String developer, List<Genre> genres, List<String> tags, List<RequirementsDTO> requirements, boolean disabled, int stock, BigDecimal price, BigDecimal discount, List<String> images, String backgroundImage, String trailer, String mainImage, LocalDate release_date, String distributor, String pegi) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.platforms = platforms;
    this.developer = developer;
    this.genres = genres;
    this.tags = tags;
    this.requirements = requirements;
    this.disabled = disabled;
    this.stock = stock;
    this.price = price;
    this.discount = discount;
    this.images = images;
    this.backgroundImage = backgroundImage;
    this.trailer = trailer;
    this.mainImage = mainImage;
    this.release_date = release_date;
    this.distributor = distributor;
    this.pegi = pegi;
  }
  
  @Override
  public String toString() {
    return "ProductDTO{" + "name=" + name + ", platforms=" + platforms + ", developer=" + developer + ", genres=" + genres + ", disabled=" + disabled + ", stock=" + stock + ", price=" + price + ", discount=" + discount + '}';
  }

}
