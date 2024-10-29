package com.ancore.ancoregaming.review.repositories;

import com.ancore.ancoregaming.review.model.Review;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReviewRepository extends JpaRepository<Review, UUID> {

  List<Review> findByProductId(UUID productId);
}
