package com.ancore.ancoregaming.checkout.model;

import com.ancore.ancoregaming.product.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductWithCheckoutList {
  public Product product;
  public List<Checkout> checkouts;
  
  public ProductWithCheckoutList(Product product, List<Checkout> checkouts) {
    this.product = product;
    this.checkouts = checkouts;
  }
}
