package com.ancore.ancoregaming.review.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRecommendationDTO {

  private String productId;
  private double percentage;
  private Long totalReviews;

  public ReviewRecommendationDTO() {
  }
  
  public ReviewRecommendationDTO(String productId, double percentage, Long totalReviews) {
    this.productId = productId;
    this.percentage = percentage;
    this.totalReviews = totalReviews;
  }
  
  @Override
  public String toString() {
    return "ReviewRecommendationPercentageDTO{" + "productId=" + productId + ", percentage=" + percentage + '}';
  }

}
