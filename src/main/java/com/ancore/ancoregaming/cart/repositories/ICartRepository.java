package com.ancore.ancoregaming.cart.repositories;

import com.ancore.ancoregaming.cart.model.Cart;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ICartRepository extends JpaRepository<Cart, UUID> {

  @Query("""
      SELECT c FROM Cart c
      JOIN FETCH c.items ci
      WHERE c.user.email = :userEmail AND ci.paymentStatus IS NULL AND ci.itemIsPaid = false
      """)
  Optional<Cart> findByUserEmailAndNoPaymentStatus(@Param("userEmail") String userEmail);
  
  public Optional<Cart> findByUserEmail(@Param("userEmail") String userEmail);
  
  @Query("SELECT COUNT(i) FROM Cart c JOIN c.items i WHERE c.user.email = :userEmail AND i.itemIsPaid = false AND i.paymentStatus IS NULL")
  public Long countUserCartProducts(@Param("userEmail") String userEmail);
  
  @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i WHERE c.user.email = :userEmail AND i.itemIsPaid = false AND i.paymentStatus IS NULL")
  public Cart findByUserEmailAndUnpaidItems(@Param("userEmail") String userEmail);

  @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i WHERE c.user.email = :userEmail AND i.itemIsPaid = true")
  public Optional<Cart> findByUserEmailWithPaidItems(@Param("userEmail") String userEmail);

  @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i WHERE c.id = :cartId AND i.itemIsPaid = false")
  public Cart findCartByIdWithoutItemsUnpaid(@Param("cartId") UUID cartId);
  
  @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i WHERE c.id = :cartId AND i.itemIsPaid = false AND i.activeSessionId = :activeSessionId")
  public Optional<Cart> findCartByIdWithoutItemsUnpaidWithSessionId(@Param("cartId") UUID cartId, @Param("activeSessionId") UUID activeSessionId);
}
