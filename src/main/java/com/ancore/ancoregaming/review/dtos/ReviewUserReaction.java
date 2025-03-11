package com.ancore.ancoregaming.review.dtos;

import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.review.model.ReviewReaction;
import com.ancore.ancoregaming.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ReviewUserReaction {
  private UUID id;
  private String title;
  private String comment;
  private boolean recommended;
  private Product product;
  private User user;
  private List<ReviewReaction> reactions;
  private Date createdAt;
  private ReactionType reactionType;
  
  @Override
  public String toString() {
    return "ReviewUserReaction{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", comment='" + comment + '\'' +
        ", recommended=" + recommended +
        ", reactions=" + reactions +
        ", createdAt=" + createdAt +
        ", reactionType=" + reactionType +
        '}';
  }
}
