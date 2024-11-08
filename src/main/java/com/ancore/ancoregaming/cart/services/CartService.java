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
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
    Optional<Cart> userCart = this.findUserCart(userEmail);
    if (userCart.isEmpty()) {
      throw new EntityNotFoundException("User cart not found");
    }

    return userCart.get();
  }

  @Override
  @Transactional
  public Cart increaseProducts(UserDetails userDetails, String productId) {
    String userEmail = userDetails.getUsername();
    User user = userService.findUser(userEmail);
    Product product = productService.findProduct(productId);

    Cart userCart = this.findOrCreateUserCart(userEmail, user);
    CartItem cartItem = this.findOrCreateCartItem(userCart, product);
    this.incrementCartItem(userCart, cartItem, product);

    return userCart;
  }

  @Override
  @Transactional
  public Cart decreaseProduct(UserDetails userDetails, String productId) {
    String userEmail = userDetails.getUsername();
    Product product = productService.findProduct(productId);
    Optional<Cart> userCart = this.findUserCart(userEmail);

    if (userCart.isEmpty()) {
      throw new EntityNotFoundException("User cart not found");
    }
    Optional<CartItem> cartItem = this.cartItemRepository
            .findUnpaidCartItemByCartIdAndProductId(userCart.get().getId(), product.getId());
    if (cartItem.isEmpty()) {
      throw new EntityNotFoundException("CartItem not found");
    }
    this.decreaseCartItem(userCart.get(), cartItem.get(), product);

    return userCart.get();
  }

  @Override
  @Transactional
  public Cart removeProduct(UserDetails userDetails, String productId) {
    String userEmail = userDetails.getUsername();
    Product product = productService.findProduct(productId);
    Optional<Cart> userCartFound = this.findUserCart(userEmail);
    if (userCartFound.isEmpty()) {
      throw new EntityNotFoundException("User cart not found");
    }

    Cart userCart = userCartFound.get();
    Optional<CartItem> cartItem = this.cartItemRepository.findUnpaidCartItemByCartIdAndProductId(userCart.getId(), product.getId());
    if (cartItem.isEmpty()) {
      throw new EntityNotFoundException("Cart item not found");
    }

    userCart.getItems().remove(cartItem.get());  // Elimina el CartItem del Cart
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
    Optional<Cart> cartFound = this.cartRepository.findByUserEmail(userEmail);

    cartFound.ifPresent(cart -> {
      List<CartItem> unpaidItems = cart.getItems()
              .stream()
              .filter(item -> !item.isItemIsPaid())
              .collect(Collectors.toList());
      cart.setItems(unpaidItems);
    });

    return cartFound;
  }

  private CartItem createItem(Cart userCart, Product product) {
    CartItem newCartItem = new CartItem.Builder()
            .setCart(userCart)
            .setCuantity(0)
            .setTotal(BigDecimal.ZERO)
            .setSubtotal(BigDecimal.ZERO)
            .setProduct(product)
            .setItemIsPaid(false)
            .build();
    userCart.getItems().add(newCartItem);

    return newCartItem;
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
    if (userCart.isEmpty()) {
      return createCart(user);
    }
    return userCart.get();
  }

  private CartItem findOrCreateCartItem(Cart cart, Product product) {
    CartItem cartItem = cartItemRepository.findUnpaidCartItemByCartIdAndProductId(cart.getId(), product.getId())
            .orElseGet(() -> createItem(cart, product));

    return cartItem.isItemIsPaid() ? createItem(cart, product) : cartItem;
  }

  private void incrementCartItem(Cart cart, CartItem item, Product product) {
    item.setCuantity(item.getCuantity() + 1);

    item.setSubtotal(item.getSubtotal().add(product.getPrice()));
    item.setTotal(item.getTotal().add(getFinalPrice(product.getPrice(), product.getDiscount())));

    cart.setSubtotal(cart.getSubtotal().add(product.getPrice()));
    cart.setTotal(cart.getTotal().add(getFinalPrice(product.getPrice(), product.getDiscount())));
  }

  private void decreaseCartItem(Cart cart, CartItem item, Product product) {
    int newQuantity = item.getCuantity() - 1;

    if (newQuantity <= 0) {
      cart.getItems().remove(item);  // Elimina el CartItem del Cart
      cart.setSubtotal(cart.getSubtotal().subtract(product.getPrice()));
      cart.setTotal(cart.getTotal().subtract(getFinalPrice(product.getPrice(), product.getDiscount())));
      this.cartItemRepository.deleteById(item.getId());
    } else {
      item.setCuantity(newQuantity);
      item.setSubtotal(item.getSubtotal().subtract(product.getPrice()));
      item.setTotal(item.getTotal().subtract(getFinalPrice(product.getPrice(), product.getDiscount())));

      cart.setSubtotal(cart.getSubtotal().subtract(product.getPrice()));
      cart.setTotal(cart.getTotal().subtract(getFinalPrice(product.getPrice(), product.getDiscount())));
    }
  }

  private BigDecimal getFinalPrice(BigDecimal amount, BigDecimal d) {
    BigDecimal percentage = new BigDecimal("100");
    BigDecimal discount = percentage.subtract(d), multiplyAmount = amount.multiply(discount);
    return multiplyAmount.divide(percentage);
  }
}
