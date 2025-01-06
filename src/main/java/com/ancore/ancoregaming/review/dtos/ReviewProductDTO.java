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
  private String trailer;
  private String mainImage;
}
