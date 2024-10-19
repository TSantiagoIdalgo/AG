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

  public Review(UUID id, String title, String comment, double rating, Product product) {
    this.id = UUID.randomUUID();
    this.title = title;
    this.comment = comment;
    this.rating = rating;
    this.product = product;
  }

}
