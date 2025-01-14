package com.ancore.ancoregaming.review.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(length = 64)
  private String title;
  @Column(columnDefinition = "TEXT")
  private String comment;
  @Column
  private boolean recommended;

  @ManyToOne
  private Product product;
  @ManyToOne
  private User user;

  @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReviewReaction> reactions = new ArrayList<>();
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  public Review() {
  }

  private Review(Builder builder) {
    this.title = builder.title;
    this.comment = builder.comment;
    this.recommended = builder.recommended;
    this.product = builder.product;
    this.user = builder.user;
    this.createdAt = new Date();
  }

  public static class Builder {

    private String title;
    private String comment;
    private boolean recommended;
    private Product product;
    private User user;

    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder setComment(String comment) {
      this.comment = comment;
      return this;
    }

    public Builder setRating(boolean recommended) {
      this.recommended = recommended;
      return this;
    }

    public Builder setProduct(Product product) {
      this.product = product;
      return this;
    }

    public Builder setUser(User user) {
      this.user = user;
      return this;
    }

    public Review build() {
      return new Review(this);
    }
  }
  
  @Override
  public String toString() {
    return "Review{" +
        "createdAt=" + createdAt +
        ", recommended=" + recommended +
        ", comment='" + comment + '\'' +
        ", title='" + title + '\'' +
        ", id=" + id +
        '}';
  }
}
