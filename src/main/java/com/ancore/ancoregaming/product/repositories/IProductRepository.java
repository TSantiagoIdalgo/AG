package com.ancore.ancoregaming.product.repositories;

import com.ancore.ancoregaming.product.model.ProductWithUserWishlist;
import com.ancore.ancoregaming.product.model.Product;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
  @Query("SELECT new com.ancore.ancoregaming.product.model.ProductWithUserWishlist(p, " +
      "CASE WHEN p IN (SELECT wp FROM Whitelist w JOIN w.whitelistItems.product wp WHERE w.user.email = :userEmail) THEN true ELSE false END) " +
      "FROM Product p WHERE p.id = :productId")
  ProductWithUserWishlist findProductWithUserWishlist(@Param("userEmail") String userEmail, @Param("productId") UUID productId);
}
