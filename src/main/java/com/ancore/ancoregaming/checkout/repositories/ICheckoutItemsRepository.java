package com.ancore.ancoregaming.checkout.repositories;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.model.CartItem;
import com.ancore.ancoregaming.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ancore.ancoregaming.checkout.model.CheckoutItems;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICheckoutItemsRepository extends JpaRepository<CheckoutItems, Long> {

  @Query("""
      SELECT cki FROM CheckoutItems cki
      LEFT JOIN cki.cartItem ci
      WHERE ci = :cartItem
      """)
  List<CheckoutItems> findByCartItem(CartItem cartItem);
}
