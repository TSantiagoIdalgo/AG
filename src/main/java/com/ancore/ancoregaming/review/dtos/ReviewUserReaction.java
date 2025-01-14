package com.ancore.ancoregaming.review.dtos;

import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.review.model.Review;
import com.ancore.ancoregaming.review.model.ReviewReaction;
import com.ancore.ancoregaming.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
  private List<ReviewReaction> reactions = new ArrayList<>();
  private Date createdAt;
  private ReactionType reactionType;
  
  public ReviewUserReaction(UUID id) {
    this.id = id;
  }
  
  public ReviewUserReaction(UUID id, String title, String comment, boolean recommended, Product product, User user, List<ReviewReaction> reactions, Date createdAt, ReactionType reactionType) {
    this.id = id;
    this.title = title;
    this.comment = comment;
    this.recommended = recommended;
    this.product = product;
    this.user = user;
    this.reactions = reactions;
    this.createdAt = createdAt;
    this.reactionType = reactionType;
  }
  
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
