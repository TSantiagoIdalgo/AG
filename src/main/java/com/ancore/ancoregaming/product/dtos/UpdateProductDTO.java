package com.ancore.ancoregaming.product.dtos;

import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.model.Platform;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ancore.ancoregaming.product.model.Requirements;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductDTO {

  public Optional<String> name;

  public Optional<String> description;

  public Optional<List<Platform>> platforms;
  
  private Optional<String> pegi;

  public Optional<String> developer;
  
  public Optional<String> distributor;

  public Optional<List<Genre>> genres;

  public Optional<List<String>> tags;

  public Optional<Boolean> disabled;

  public Optional<Integer> stock;

  public Optional<BigDecimal> price;

  public Optional<BigDecimal> discount;

  public Optional<List<String>> images;
  
  public Optional<LocalDate> release_date;
  
  public Optional<List<Requirements>> requirements;

  @Override
  public String toString() {
    return "UpdateProductDTO{" + "name=" + name + ", description=" + description + ", platforms=" + platforms + ", developer=" + developer + ", genres=" + genres + ", tags=" + tags + ", disabled=" + disabled + ", stock=" + stock + ", price=" + price + ", discount=" + discount + '}';
  }

}
