package com.ancore.ancoregaming.review.repositories;

import com.ancore.ancoregaming.review.model.Review;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IReviewRepository extends JpaRepository<Review, UUID> {

  List<Review> findByProductId(UUID productId);

  @Query("SELECT (COUNT(r) * 1.0) / (SELECT COUNT(r2) FROM Review r2 WHERE r2.product.id = :productId) "
          + "FROM Review r "
          + "WHERE r.product.id = :productId AND r.recommended = true")
  Double findRecommendationPercentageByProductId(@Param("productId") UUID productId);
}
