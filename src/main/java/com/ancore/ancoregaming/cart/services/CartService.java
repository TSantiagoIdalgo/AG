package com.ancore.ancoregaming.cart.services;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.model.CartItem;
import com.ancore.ancoregaming.cart.repositories.ICartItemRepository;
import com.ancore.ancoregaming.cart.repositories.ICartRepository;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

  private final ICartRepository cartRepository;
  private final ICartItemRepository cartItemRepository;
  private final ICartItemService cartItemService;
  
  @Override
  public Cart getUserCartWithoutPaymentStatus(UserDetails userDetails) {
    String userEmail = userDetails.getUsername();
    Optional<Cart> userCart = this.cartRepository.findByUserEmailAndNoPaymentStatus(userEmail);
    if (userCart.isEmpty()) {
      throw new EntityNotFoundException("User cart not found");
    }
    return userCart.get();
  }
  
  @Override
  public Cart getUserCart(UserDetails userDetails) {
    String userEmail = userDetails.getUsername();
    Optional<Cart> userCart = this.findUserCart(userEmail);
    if (userCart.isEmpty()) {
      throw new EntityNotFoundException("User cart not found");
    }

    return userCart.get();
  }
  
  @Override
  public Long getQuantityProductsCart(@NotNull UserDetails userDetails) {
    return this.cartRepository.countUserCartProducts(userDetails.getUsername());
  }
  
  @Override
  @Transactional
  public Cart increaseProducts(User user, Product product) throws BadRequestException {
    Cart userCart = this.findOrCreateUserCart(user.getEmail(), user);
    CartItem cartItem = this.cartItemService.findOrCreateCartItem(userCart, product);
    this.cartItemService.incrementCartItem(userCart, cartItem, product);
    
    
    return userCart;
  }

  @Override
  @Transactional
  public Cart decreaseProduct(@NotNull User user, Product product) {
    Optional<Cart> userCart = this.findUserCart(user.getEmail());
    if (userCart.isEmpty()) {
      throw new EntityNotFoundException("User cart not found");
    }
    Optional<CartItem> cartItem = this.cartItemRepository
        .findUnpaidCartItemByCartIdAndProductId(userCart.get().getId(), product.getId());
    if (cartItem.isEmpty()) {
      throw new EntityNotFoundException("CartItem not found");
    }
    this.cartItemService.decreaseCartItem(userCart.get(), cartItem.get(), product);

    return userCart.get();
  }

  @Override
  @Transactional
  public Cart removeProduct(@NotNull User user, Product product) {
    Optional<Cart> userCartFound = this.findUserCart(user.getEmail());
    if (userCartFound.isEmpty()) {
      throw new EntityNotFoundException("User cart not found");
    }

    Cart userCart = userCartFound.get();
    Optional<CartItem> cartItem = this.cartItemRepository.findUnpaidCartItemByCartIdAndProductId(userCart.getId(),
        product.getId());
    if (cartItem.isEmpty()) {
      throw new EntityNotFoundException("Cart item not found");
    }

    userCart.getItems().remove(cartItem.get()); // Elimina el CartItem del Cart
    product.getCartItems().remove(cartItem.get());
    userCart.setSubtotal(userCart.getSubtotal().subtract(cartItem.get().getSubtotal()));
    userCart.setTotal(userCart.getTotal().subtract(cartItem.get().getTotal()));
    this.cartItemRepository.deleteById(cartItem.get().getId());

    return userCart;
  }

  @Override
  public Cart getUserPaidProducts(UserDetails userDetails) {
    String userEmail = userDetails.getUsername();
    Optional<Cart> cart = this.cartRepository.findByUserEmailWithPaidItems(userEmail);
    if (cart.isEmpty()) {
      throw new EntityNotFoundException("The user has no paid products");
    }

    return cart.get();
  }

  @Override
  public Cart getCartById(String cartId) {
    return this.cartRepository.findById(UUID.fromString(cartId))
        .orElseThrow(() -> new EntityNotFoundException("User cart not found"));
  }

  private Optional<Cart> findUserCart(String userEmail) {
    return this.cartRepository.findByUserEmail(userEmail);
  }

  private Cart createCart(User user) {
    List<CartItem> cartItems = new ArrayList<>();
    Cart userCart = new Cart.Builder()
        .setTotal(BigDecimal.ZERO)
        .setSubtotal(BigDecimal.ZERO)
        .setItems(cartItems)
        .setUser(user)
        .build();
    return this.cartRepository.save(userCart);
  }

  private Cart findOrCreateUserCart(String userEmail, User user) {
    Optional<Cart> userCart = findUserCart(userEmail);
      return userCart.orElseGet(() -> createCart(user));
  }
}
