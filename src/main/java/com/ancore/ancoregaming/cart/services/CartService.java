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

  // TODOS
  // ARREGLAR EL INCREASE: No suma el total al cart y agrega dos veces el CartItem cuando no hay cart
  // ARREGLAR EL DECREASE: No resta el total al cart y si es 1 no borra el CartItem
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
      throw new EntityNotFoundException("The user has no paid products");
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
    incrementCartItem(cartItem, product);
    this.cartItemRepository.save(cartItem);
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
    CartItem cartItem = findOrCreateCartItem(userCart, product);
    decreaseCartItem(cartItem, product);
    this.cartItemRepository.save(cartItem);

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

    this.cartItemRepository.delete(cartItem.get());

    return userCart;
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
            .setCuantity(1)
            .setPrice(product.getPrice())
            .setProduct(product)
            .setItemIsPaid(false)
            .build();
    this.cartItemRepository.save(newCartItem);
    userCart.getItems().add(newCartItem);
    userCart.getTotal().add(newCartItem.getPrice());
    this.cartRepository.save(userCart);

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

  private void incrementCartItem(CartItem item, Product product) {
    item.setCuantity(item.getCuantity() + 1);
    item.setPrice(item.getPrice().add(product.getPrice()));
  }

  private void decreaseCartItem(CartItem item, Product product) {
    int newQuantity = item.getCuantity() - 1;

    if (newQuantity <= 0) {
      cartItemRepository.delete(item);
    } else {
      item.setCuantity(newQuantity);
      item.setPrice(item.getPrice().subtract(product.getPrice()));
    }
  }

}
