package com.ancore.ancoregaming.review.model;

import com.ancore.ancoregaming.review.dtos.ReactionType;
import com.ancore.ancoregaming.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ReviewReaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "review_id", nullable = false)
  private Review review;

  @Enumerated(EnumType.STRING)
  private ReactionType reactionType; // LIKE o DISLIKE

  // Clave única para evitar duplicados
  @Column(name = "user_review_unique", unique = true)
  private String uniqueKey;

  @PrePersist
  public void setUniqueKey() {
    this.uniqueKey = user.getEmail() + "_" + review.getId();
  }

  public ReviewReaction() {
  }

  public ReviewReaction(User user, Review review, ReactionType reactionType) {
    this.user = user;
    this.review = review;
    this.reactionType = reactionType;
  }

}
