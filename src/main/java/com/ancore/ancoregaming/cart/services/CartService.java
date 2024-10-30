package com.ancore.ancoregaming.cart.services;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.model.CartItem;
import com.ancore.ancoregaming.cart.repositories.ICartItemRepository;
import com.ancore.ancoregaming.cart.repositories.ICartRepository;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.services.product.IProductService;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.services.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

  private final ICartRepository cartRepository;
  private final ICartItemRepository cartItemRepository;
  private final IUserService userService;
  private final IProductService productService;

  @Override
  public Cart getUserCart(UserDetails userDetails) {
    String userEmail = userDetails.getUsername();
    Cart userCart = this.findUserCart(userEmail);
    if (userCart == null) {
      throw new EntityNotFoundException("User cart not found");
    }
    Optional<Cart> cart = this.cartRepository.findUserCart(userCart.getId());
    if (cart.isEmpty()) {
      throw new EntityNotFoundException("The user has no products");
    }

    return cart.get();
  }

  @Override
  public Cart increaseProducts(UserDetails userDetails, String productId) {
    String userEmail = userDetails.getUsername();
    User user = userService.findUser(userEmail);
    Product product = productService.findProduct(productId);

    Cart userCart = findOrCreateUserCart(userEmail, user);
    CartItem cartItem = findOrCreateCartItem(userCart, product);
    incrementCartItem(userCart, cartItem, product);

    return userCart;
  }

  @Override
  public Cart decreaseProduct(UserDetails userDetails, String productId) {
    String userEmail = userDetails.getUsername();
    Product product = productService.findProduct(productId);

    Cart userCart = this.findUserCart(userEmail);
    if (userCart == null) {
      throw new EntityNotFoundException("User cart not found");
    }
    CartItem cartItem = this.findOrCreateCartItem(userCart, product);
    this.decreaseCartItem(userCart, cartItem, product);

    return userCart;
  }

  @Override
  public Cart removeProduct(UserDetails userDetails, String productId) {
    String userEmail = userDetails.getUsername();
    Product product = productService.findProduct(productId);
    Cart userCart = this.findUserCart(userEmail);
    if (userCart == null) {
      throw new EntityNotFoundException("User cart not found");
    }

    Optional<CartItem> cartItem = this.cartItemRepository.findByCartIdAndProductId(userCart.getId(), product.getId());
    if (cartItem.isEmpty()) {
      throw new EntityNotFoundException("Cart item not found");
    }

    userCart.setTotal(userCart.getTotal().subtract(cartItem.get().getPrice()));
    this.cartItemRepository.delete(cartItem.get());

    return this.cartRepository.save(userCart);
  }

  private Cart findUserCart(String userEmail) {
    return this.cartRepository.findByUserEmail(userEmail);

  }

  @Override
  public Cart getUserPaidProducts(UserDetails userDetails) {
    String userEmail = userDetails.getUsername();
    Cart userCart = this.findUserCart(userEmail);
    if (userCart == null) {
      throw new EntityNotFoundException("User cart not found");
    }
    Optional<Cart> cart = this.cartRepository.findByIdWithPaidItems(userCart.getId());
    if (cart.isEmpty()) {
      throw new EntityNotFoundException("The user has no paid products");
    }

    return cart.get();
  }

  private CartItem createItem(Cart userCart, Product product) {
    CartItem newCartItem = new CartItem.Builder()
            .setCart(userCart)
            .setCuantity(0)
            .setPrice(BigDecimal.ZERO)
            .setProduct(product)
            .setItemIsPaid(false)
            .build();
    this.cartItemRepository.save(newCartItem);
    userCart.getItems().add(newCartItem);

    return newCartItem;
  }

  private Cart createCart(User user) {
    List<CartItem> cartItems = new ArrayList<>();
    Cart userCart = new Cart.Builder()
            .setTotal(BigDecimal.ZERO)
            .setItems(cartItems)
            .setUser(user)
            .build();
    return this.cartRepository.save(userCart);
  }

  private Cart findOrCreateUserCart(String userEmail, User user) {
    Cart userCart = findUserCart(userEmail);
    if (userCart == null) {
      userCart = createCart(user);
    }
    return userCart;
  }

  private CartItem findOrCreateCartItem(Cart cart, Product product) {
    return cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
            .orElseGet(() -> createItem(cart, product)); // Crea el item si no existe
  }

  private void incrementCartItem(Cart cart, CartItem item, Product product) {
    item.setCuantity(item.getCuantity() + 1);
    item.setPrice(item.getPrice().add(product.getPrice()));
    cart.setTotal(cart.getTotal().add(product.getPrice()));

    this.cartItemRepository.save(item);
    this.cartRepository.save(cart);
  }

  private void decreaseCartItem(Cart cart, CartItem item, Product product) {
    int newQuantity = item.getCuantity() - 1;

    if (newQuantity <= 0) {
      cart.setTotal(cart.getTotal().subtract(product.getPrice()));
      this.cartItemRepository.delete(item);
      this.cartRepository.save(cart);
    } else {
      item.setCuantity(newQuantity);
      item.setPrice(item.getPrice().subtract(product.getPrice()));
      cart.setTotal(cart.getTotal().subtract(product.getPrice()));
      this.cartItemRepository.save(item);
      this.cartRepository.save(cart);
    }
  }

}
