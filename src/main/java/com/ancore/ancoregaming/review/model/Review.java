package com.ancore.ancoregaming.review.model;

import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.UUID;
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
  @Column
  private String comment;
  @Column
  private double rating;

  @ManyToOne
  private Product product;
  @OneToOne
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  public Review() {
  }

  private Review(Builder builder) {
    this.title = builder.title;
    this.comment = builder.comment;
    this.rating = builder.rating;
    this.product = builder.product;
    this.user = builder.user;
  }

  public static class Builder {

    private String title;
    private String comment;
    private double rating;
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

    public Builder setRating(double rating) {
      this.rating = rating;
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
}
