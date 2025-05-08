package com.ancore.ancoregaming.checkout.dtos;

import com.ancore.ancoregaming.product.model.Platform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@RequiredArgsConstructor
public class ProductCheckoutDTO {
  public UUID id;
  public String name;
  public BigDecimal price;
  public BigDecimal discount;
  public String trailer;
  public String mainImage;
  public List<Platform> platforms;
  private int stock;
  private boolean disabled;
}
