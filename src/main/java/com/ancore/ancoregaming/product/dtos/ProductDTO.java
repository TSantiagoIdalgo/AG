package com.ancore.ancoregaming.product.dtos;

import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.model.Platform;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class ProductDTO {

  @NotNull(message = "Name is required")
  public String name;

  @NotNull(message = "Description is required")
  public String description;

  @NotNull(message = "Platforms are required")
  @Size(min = 1)
  public List<Platform> platforms;

  @NotNull(message = "Developer name is required")
  public String developer;

  @NotNull(message = "Product genres are required")
  @Size(min = 1)
  public List<Genre> genres;

  public boolean disabled;

  @NotNull(message = "Product stock is required")
  @Positive(message = "The stock of the product must be positive")
  public int stock;

  @NotNull(message = "Product price is required")
  @Positive(message = "The price of the product must be positive")
  public BigDecimal price;

  @Positive(message = "The discount must be positive")
  public BigDecimal discount;

  @Override
  public String toString() {
    return "ProductDTO{" + "name=" + name + ", platforms=" + platforms + ", developer=" + developer + ", genres=" + genres + ", disabled=" + disabled + ", stock=" + stock + ", price=" + price + ", discount=" + discount + '}';
  }

}
