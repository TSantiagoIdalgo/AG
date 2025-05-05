package com.ancore.ancoregaming.checkout.dtos;

import com.ancore.ancoregaming.user.dtos.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class ProductCheckoutsDTO {
  public UUID id;
  public String stripePaymentId;
  public BigDecimal subTotal;
  public BigDecimal total;
  public String currency;
  public String paymentStatus;
  public UserDTO user;
  public Date createdAt;
  
  public ProductCheckoutsDTO() {
  }
  
  public ProductCheckoutsDTO(UUID id, String stripePaymentId, BigDecimal subTotal, BigDecimal total, String currency, String paymentStatus, UserDTO user, Date createdAt) {
    this.id = id;
    this.stripePaymentId = stripePaymentId;
    this.subTotal = subTotal;
    this.total = total;
    this.currency = currency;
    this.paymentStatus = paymentStatus;
    this.user = user;
    this.createdAt = createdAt;
  }
}
