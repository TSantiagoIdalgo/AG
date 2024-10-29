package com.ancore.ancoregaming.cart.services;

import com.ancore.ancoregaming.cart.model.Cart;
import org.springframework.security.core.userdetails.UserDetails;

public interface ICartService {

  public Cart increaseProducts(UserDetails userDetails, String productId);

  public Cart decreaseProduct(UserDetails userDetails, String productId);

  public Cart removeProduct(UserDetails userDetails, String productId);

  public Cart getUserPaidProducts(UserDetails userDetails);

  public Cart getUserCart(UserDetails userDetails);
}
