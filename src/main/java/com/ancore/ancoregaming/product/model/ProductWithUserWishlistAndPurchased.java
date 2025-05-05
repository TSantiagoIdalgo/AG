package com.ancore.ancoregaming.product.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithUserWishlistAndPurchased {
  private Product product;
  private boolean isInWishlist;
  private boolean purchasedByUser;
  private boolean userReviewed;
  
  public ProductWithUserWishlistAndPurchased(Product product, boolean isInWishlist, boolean purchasedByUser, boolean userReviewed) {
    this.product = product;
    this.isInWishlist = isInWishlist;
    this.purchasedByUser = purchasedByUser;
    this.userReviewed = userReviewed;
    
  }
}
