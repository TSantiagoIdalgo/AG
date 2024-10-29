package com.ancore.ancoregaming.cart.repositories;

import com.ancore.ancoregaming.cart.model.Cart;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ICartRepository extends JpaRepository<Cart, UUID> {

  public Cart findByUserEmail(String userEmail);

  @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i WHERE c.id = :cartId AND i.itemIsPaid = true")
  public Optional<Cart> findByIdWithPaidItems(@Param("cartId") UUID cartId);

  @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i WHERE c.id = :cartId AND i.itemIsPaid = false")
  public Optional<Cart> findUserCart(@Param("cartId") UUID cartId);
}
