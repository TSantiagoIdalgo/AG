package com.ancore.ancoregaming.review.dtos;

import com.ancore.ancoregaming.user.dtos.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewReactionDTO {

  private Long id;

  private UserDTO user;

  private ReactionType reactionType; // LIKE o DISLIKE

  private String uniqueKey;

  public ReviewReactionDTO() {
  }

  public ReviewReactionDTO(Long id, UserDTO user, ReactionType reactionType, String uniqueKey) {
    this.id = id;
    this.user = user;
    this.reactionType = reactionType;
    this.uniqueKey = uniqueKey;
  }

  @Override
  public String toString() {
    return "ReviewReactionDTO{" + "id=" + id + ", user=" + user + ", reactionType=" + reactionType + ", uniqueKey=" + uniqueKey + '}';
  }

}
