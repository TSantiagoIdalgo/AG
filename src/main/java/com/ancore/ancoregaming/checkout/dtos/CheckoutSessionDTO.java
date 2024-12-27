package com.ancore.ancoregaming.checkout.dtos;

public class CheckoutSessionDTO {

  public String id;

  public CheckoutSessionDTO() {
  }

  public CheckoutSessionDTO(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
