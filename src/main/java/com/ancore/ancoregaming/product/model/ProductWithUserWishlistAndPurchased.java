package com.ancore.ancoregaming.product.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithUserWishlistAndPurchased {
  private Product product;
  private boolean isInWishlist;
  private boolean purchasedByUser;
  
  public ProductWithUserWishlistAndPurchased(Product product, boolean isInWishlist, boolean purchasedByUser) {
    this.product = product;
    this.isInWishlist = isInWishlist;
    this.purchasedByUser = purchasedByUser;
  }
}
