package com.ancore.ancoregaming.checkout.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.ancore.ancoregaming.user.model.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Checkout {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(nullable = false)
  private String stripePaymentId;
  @Column(nullable = false)
  private BigDecimal subTotal;
  @Column(nullable = false)
  private BigDecimal total;
  private String currency;
  @Column(nullable = false)
  private String paymentStatus;

  @ManyToOne
  private User user;

  @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL)
  private List<CheckoutItems> checkoutItems;
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;
  
 
  
  private Checkout(Builder builder) {
    this.stripePaymentId = builder.stripePaymentId;
    this.total = builder.total;
    this.subTotal = builder.subtotal;
    this.currency = builder.currency;
    this.paymentStatus = builder.paymentStatus;
    this.user = builder.user;
    this.checkoutItems = builder.items;
    this.createdAt = new Date();
  }

  public static class Builder {

    private final String stripePaymentId;
    private BigDecimal subtotal;
    private BigDecimal total;
    private String currency;
    private String paymentStatus;
    private User user;
    private List<CheckoutItems> items;

    public Builder(String stripePaymentId) {
      this.stripePaymentId = stripePaymentId;
    }

    public Builder setTotal(BigDecimal total) {
      this.total = total;
      return this;
    }

    public Builder setSubtotal(BigDecimal subtotal) {
      this.subtotal = subtotal;
      return this;
    }

    public Builder setCurrency(String currency) {
      this.currency = currency;
      return this;
    }

    public Builder setPaymentStatus(String paymentStatus) {
      this.paymentStatus = paymentStatus;
      return this;
    }

    public Builder setUser(User user) {
      this.user = user;
      return this;
    }

    public Builder setItems(List<CheckoutItems> items) {
      this.items = items;
      return this;
    }

    public Checkout build() {
      return new Checkout(this);
    }
  }
  
  
  @Override
  public String toString() {
    return "Checkout{" +
        "id=" + id +
        ", stripePaymentId='" + stripePaymentId + '\'' +
        ", subTotal=" + subTotal +
        ", total=" + total +
        ", currency='" + currency + '\'' +
        ", paymentStatus='" + paymentStatus + '\'' +
        ", checkoutItems=" + checkoutItems +
        ", createdAt=" + createdAt +
        '}';
  }
}
