package com.ancore.ancoregaming.checkout.model;
import com.ancore.ancoregaming.product.model.Product;
import lombok.Getter;
import lombok.Setter;
// com.ancore.ancoregaming.checkout.dtos.ProductWithCheckoutsDTO

@Getter
@Setter
public class ProductWithCheckouts {
  
  public Product product;
  public Checkout checkout;
  
  public ProductWithCheckouts() {
  }
  
  public ProductWithCheckouts(Product product, Checkout checkout) {
    this.product = product;
    this.checkout = checkout;
  }
}
