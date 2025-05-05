package com.ancore.ancoregaming.review.dtos;

import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReviewDTO {

  private Optional<String> title = Optional.empty();
  private Optional<String> comment = Optional.empty();;
  private Optional<Boolean> recommended = Optional.empty();;

  public UpdateReviewDTO() {
  }

  public UpdateReviewDTO(Optional<String> title, Optional<String> comment, Optional<Boolean> recommended) {
    this.title = title;
    this.comment = comment;
    this.recommended = recommended;
  }
  
  @Override
  public String toString() {
    return "UpdateReviewDTO{" +
        "title='" + title + '\'' +
        ", comment='" + comment + '\'' +
        ", recommended=" + recommended +
        '}';
  }
}
