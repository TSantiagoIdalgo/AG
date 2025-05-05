package com.ancore.ancoregaming.product.repositories;

import com.ancore.ancoregaming.product.model.ProductWithUserWishlistAndPurchased;
import com.ancore.ancoregaming.product.model.Product;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
  @Query("""
    SELECT new com.ancore.ancoregaming.product.model.ProductWithUserWishlistAndPurchased(
        p,
        CASE
            WHEN EXISTS (
                SELECT 1
                FROM Whitelist w
                JOIN w.whitelistItems wi
                WHERE w.user.email = :userEmail AND wi.product = p
            ) THEN true ELSE false
        END,
        CASE
            WHEN EXISTS (
                SELECT 1
                FROM Cart c
                JOIN c.items ci
                WHERE c.user.email = :userEmail AND ci.product = p AND ci.itemIsPaid = true
            ) THEN true ELSE false
        END,
        CASE
            WHEN EXISTS (
                SELECT 1
                FROM Review r
                WHERE r.user.email = :userEmail AND r.product = p
            ) THEN true ELSE false
        END
    )
    FROM Product p
    WHERE p.id = :productId
""")
  ProductWithUserWishlistAndPurchased findProductWithUserWishlistAndPurchase(
      @Param("userEmail") String userEmail,
      @Param("productId") UUID productId
  );
  
}
