package com.ancore.ancoregaming.product.dtos;

import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.model.Platform;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductDTO {

  public Optional<String> name;

  public Optional<String> description;

  public Optional<List<Platform>> platforms;

  public Optional<String> developer;

  public Optional<List<Genre>> genres;

  public Optional<List<String>> tags;

  public Optional<Boolean> disabled;

  public Optional<Integer> stock;

  public Optional<BigDecimal> price;

  public Optional<BigDecimal> discount;

  public Optional<List<String>> images;

  @Override
  public String toString() {
    return "UpdateProductDTO{" + "name=" + name + ", description=" + description + ", platforms=" + platforms + ", developer=" + developer + ", genres=" + genres + ", tags=" + tags + ", disabled=" + disabled + ", stock=" + stock + ", price=" + price + ", discount=" + discount + '}';
  }

}
