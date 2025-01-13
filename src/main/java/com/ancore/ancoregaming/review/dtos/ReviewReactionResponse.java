package com.ancore.ancoregaming.review.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ReviewReactionResponse {
  private String id;
  private boolean recommended;
  private List<ReviewReactionUserDTO> reactions;
  private Date createdAt;
}
