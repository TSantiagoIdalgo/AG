package com.ancore.ancoregaming.product.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithUserWishlistDTO {
  private ProductDTO product;
  private boolean isInWishlist;
  
  public ProductWithUserWishlistDTO() {
  }
  
  public ProductWithUserWishlistDTO(ProductDTO product, boolean isInWishlist) {
    this.product = product;
    this.isInWishlist = isInWishlist;
  }
}
