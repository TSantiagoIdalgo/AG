package com.ancore.ancoregaming.checkout.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.ancore.ancoregaming.user.model.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

  @OneToMany(mappedBy = "checkout")
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

}
