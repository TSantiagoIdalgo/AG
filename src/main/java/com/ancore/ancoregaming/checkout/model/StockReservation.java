package com.ancore.ancoregaming.checkout.model;

import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import java.util.UUID;

@Entity
public class StockReservation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(nullable = false)
  private int reservedQuantity;

  @ManyToOne
  private User user;

  @ManyToOne
  private Product product;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Instant expirationTime;

  public StockReservation() {
  }

  public StockReservation(int reservedQuantity, User user, Product product, Instant expirationTime) {
    this.reservedQuantity = reservedQuantity;
    this.user = user;
    this.product = product;
    this.expirationTime = expirationTime;
  }

  public UUID getId() {
    return id;
  }

  public int getReservedQuantity() {
    return reservedQuantity;
  }

  public User getUser() {
    return user;
  }

  public Product getProduct() {
    return product;
  }

  public Instant getExpirationTime() {
    return expirationTime;
  }

}
