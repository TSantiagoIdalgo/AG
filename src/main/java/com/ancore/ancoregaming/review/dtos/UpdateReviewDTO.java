package com.ancore.ancoregaming.review.dtos;

import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReviewDTO {

  private Optional<String> title;
  private Optional<String> comment;
  private Optional<Double> rating;

  public UpdateReviewDTO() {
  }

  public UpdateReviewDTO(Optional<String> title, Optional<String> comment, Optional<Double> rating) {
    this.title = title;
    this.comment = comment;
    this.rating = rating;
  }

}
