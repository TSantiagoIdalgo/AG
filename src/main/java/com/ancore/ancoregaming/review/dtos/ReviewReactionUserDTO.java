package com.ancore.ancoregaming.review.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ReviewReactionUserDTO extends ReviewReactionDTO{
  private boolean userLiked;
}
