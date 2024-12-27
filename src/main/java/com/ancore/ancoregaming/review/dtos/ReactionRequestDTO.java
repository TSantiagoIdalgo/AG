package com.ancore.ancoregaming.review.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionRequestDTO {

  private String reviewId;
  private ReactionType reactionType;
}
