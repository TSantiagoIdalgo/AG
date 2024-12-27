package com.ancore.ancoregaming.review.repositories;

import com.ancore.ancoregaming.review.model.ReviewReaction;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReviewReactionRepository extends JpaRepository<ReviewReaction, Long> {

  Optional<ReviewReaction> findByUserEmailAndReviewId(String userId, UUID reviewId);

}
