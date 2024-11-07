package com.ancore.ancoregaming.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Payment {

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
  @Column(nullable = false)
  private String userEmail;
  @Column(nullable = false)
  private UUID cartId;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  private Payment(Builder builder) {
    this.stripePaymentId = builder.stripePaymentId;
    this.total = builder.total;
    this.subTotal = builder.subtotal;
    this.currency = builder.currency;
    this.paymentStatus = builder.paymentStatus;
    this.userEmail = builder.userEmail;
    this.cartId = builder.cartId;
    this.createdAt = new Date();
  }

  public static class Builder {

    private String stripePaymentId;
    private BigDecimal subtotal;
    private BigDecimal total;
    private String currency;
    private String paymentStatus;
    private String userEmail;
    private UUID cartId;

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

    public Builder setUserEmail(String userEmail) {
      this.userEmail = userEmail;
      return this;
    }

    public Builder setCartId(UUID cartId) {
      this.cartId = cartId;
      return this;
    }

    public Payment build() {
      return new Payment(this);
    }
  }
}
