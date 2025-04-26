package com.ancore.ancoregaming.review.dtos;

import java.util.Date;
import java.util.List;

import com.ancore.ancoregaming.user.dtos.UserDTO;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ReviewDTO {

  private String id;
  @NotNull
  private String title;
  private String comment;
  private boolean recommended;
  private UserDTO user;
  private List<ReviewReactionDTO> reactions;
  private Date createdAt;
  private ReviewProductDTO product;
  private ReactionType reactionType;

  public ReviewDTO() {
  }

  public ReviewDTO(@NotNull String title, String comment, boolean recommended, UserDTO user) {
    this.title = title;
    this.comment = comment;
    this.recommended = recommended;
    this.user = user;
  }
  
  @Override
  public String toString() {
    return "ReviewDTO{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", comment='" + comment + '\'' +
        ", recommended=" + recommended +
        ", user=" + user +
        ", reactions=" + reactions +
        ", createdAt=" + createdAt +
        ", product=" + product +
        ", reactionType=" + reactionType +
        '}';
  }
}
