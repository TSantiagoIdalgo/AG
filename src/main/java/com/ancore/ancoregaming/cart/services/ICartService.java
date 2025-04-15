package com.ancore.ancoregaming.cart.services;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.user.model.User;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;

public interface ICartService {

  public Cart increaseProducts(User user, Product product) throws BadRequestException;
  
  public Long getQuantityProductsCart (UserDetails userDetails);

  public Cart decreaseProduct(User user, Product product);

  public Cart removeProduct(User user, Product product);

  public Cart getUserPaidProducts(UserDetails userDetails);

  public Cart getCartById(String cartId);

  public Cart getUserCart(UserDetails userDetails);
}
