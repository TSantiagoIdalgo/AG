package com.ancore.ancoregaming.checkout.dtos;
import com.ancore.ancoregaming.user.dtos.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class CheckoutDTO {
  private UUID id;
  private String stripePaymentId;
  private BigDecimal subTotal;
  private BigDecimal total;
  private String currency;
  private String paymentStatus;
  private UserDTO user;
  
  private List<CheckoutItemsDTO> checkoutItems;
  private Date createdAt;
}
