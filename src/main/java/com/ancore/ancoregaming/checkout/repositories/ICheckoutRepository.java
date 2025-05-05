package com.ancore.ancoregaming.checkout.repositories;

import com.ancore.ancoregaming.checkout.model.ProductWithCheckouts;
import com.ancore.ancoregaming.checkout.model.Checkout;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICheckoutRepository extends JpaRepository<Checkout, UUID> {
  @Query("SELECT c FROM Checkout c WHERE c.user.email = :email ORDER BY c.createdAt DESC")
  public List<Checkout> findByUserEmail(String email);
  
  @Query("SELECT c FROM Checkout c ORDER BY c.createdAt DESC limit :pageSize offset :pageNumber")
  List<Checkout> findAllOrdered(@Param("pageSize") int pageSize, @Param("pageNumber") int pageNumber);
  
  @Query("""
    SELECT new com.ancore.ancoregaming.checkout.model.ProductWithCheckouts(
      p, c)
    FROM Product p
    LEFT JOIN p.cartItems ci
    LEFT JOIN ci.checkoutItems cki
    LEFT JOIN cki.checkout c
""")
  List<ProductWithCheckouts> findProductWithCartItemsAndCheckouts();
  
}
