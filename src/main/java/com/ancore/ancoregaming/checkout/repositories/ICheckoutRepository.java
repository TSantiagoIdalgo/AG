package com.ancore.ancoregaming.checkout.repositories;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.checkout.model.Checkout;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ancore.ancoregaming.checkout.model.CheckoutItems;
import com.ancore.ancoregaming.product.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICheckoutRepository extends JpaRepository<Checkout, UUID> {
  @Query("SELECT c FROM Checkout c WHERE c.user.email = :email ORDER BY c.createdAt DESC")
  public List<Checkout> findByUserEmail(String email);
  
  @Query("SELECT c FROM Checkout c ORDER BY c.createdAt DESC")
  List<Checkout> findAllOrdered(Pageable pageable);
  
  @Query("""
      SELECT c FROM Checkout c
      LEFT JOIN c.checkoutItems cki
      LEFT JOIN cki.cartItem ci
      WHERE ci.product = :product AND ci.cart = :cart AND ci.itemIsPaid = false
      """)
  Checkout findByCartAndProduct(Product product, Cart cart);
  
  @Query("""
      SELECT cki FROM CheckoutItems cki
      LEFT JOIN cki.cartItem ci
      LEFT JOIN ci.product p
      LEFT JOIN cki.checkout c
      WHERE c.createdAt BETWEEN :startDate AND :endDate
      ORDER BY c.createdAt ASC
      """)
  List<CheckoutItems> findCheckoutItems(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
  
  @Query("""
  SELECT p
  FROM Product p
  JOIN FETCH p.cartItems ci
  WHERE ci.paymentStatus IS NOT NULL AND ci.paidAt IS NOT NULL AND ci.paidAt BETWEEN :startDate AND :endDate
  ORDER BY ci.paidAt ASC
""")
  List<Product> findProductWithCartItemsPaid(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
  
  
  
}
