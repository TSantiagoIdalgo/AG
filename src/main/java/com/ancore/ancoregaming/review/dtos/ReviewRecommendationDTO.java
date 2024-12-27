package com.ancore.ancoregaming.review.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRecommendationDTO {

  private String productId;
  private double percentage;

  public ReviewRecommendationDTO() {
  }

  public ReviewRecommendationDTO(String productId, double percentage) {
    this.productId = productId;
    this.percentage = percentage;
  }

  @Override
  public String toString() {
    return "ReviewRecommendationPercentageDTO{" + "productId=" + productId + ", percentage=" + percentage + '}';
  }

}
