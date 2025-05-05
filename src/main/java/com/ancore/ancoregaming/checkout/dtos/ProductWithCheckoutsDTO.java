package com.ancore.ancoregaming.checkout.dtos;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductWithCheckoutsDTO {
  public ProductCheckoutDTO product;
  public List<ProductCheckoutsDTO> checkouts;
  
  public ProductWithCheckoutsDTO() {
  }
  
  public ProductWithCheckoutsDTO(ProductCheckoutDTO product, List<ProductCheckoutsDTO> checkout) {
    this.product = product;
    this.checkouts = checkout;
  }
}

