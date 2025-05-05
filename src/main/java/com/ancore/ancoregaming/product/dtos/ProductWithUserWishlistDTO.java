package com.ancore.ancoregaming.product.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithUserWishlistDTO {
  private ProductDTO product;
  private boolean isInWishlist;
  private boolean purchasedByUser;
  private boolean userReviewed;
  
  public ProductWithUserWishlistDTO() {
  }
  
  public ProductWithUserWishlistDTO(ProductDTO product, boolean isInWishlist, boolean purchasedByUser, boolean userReviewed) {
    this.product = product;
    this.isInWishlist = isInWishlist;
    this.purchasedByUser = purchasedByUser;
    this.userReviewed = userReviewed;
  }
}
