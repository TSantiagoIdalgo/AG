package com.ancore.ancoregaming.review.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ancore.ancoregaming.review.model.Review;

@Repository
public interface IReviewRepository extends JpaRepository<Review, UUID>, JpaSpecificationExecutor<Review> {

        List<Review> findByProductId(UUID productId);
        
        @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
        Long countReviewsByProductId(@Param("productId") UUID productId);
        
        @Query("SELECT (COUNT(r) * 1.0) / (SELECT COUNT(r2) FROM Review r2 WHERE r2.product.id = :productId) "
                        + "FROM Review r "
                        + "WHERE r.product.id = :productId AND r.recommended = true")
        Double findRecommendationPercentageByProductId(@Param("productId") UUID productId);

        @Query("SELECT r FROM Review r " +
                        "LEFT JOIN r.reactions rr " +
                        "WHERE r.recommended = :recommended AND r.product.id = :productId " +
                        "GROUP BY r.id " +
                        "ORDER BY SUM(CASE WHEN rr.reactionType = 'LIKE' THEN 1 ELSE 0 END) DESC, " +
                        "         SUM(CASE WHEN rr.reactionType = 'DISLIKE' THEN 1 ELSE 0 END) ASC")
        List<Review> findReviewsOrderedByLikes(
                        @Param("recommended") final boolean recommended,
                        @Param("productId") UUID productId);

        Review findByProductIdAndUserEmail(UUID productId, String userEmail);
        
        
        @Query("SELECT r, " +
            "       (SELECT rr.reactionType " +
            "        FROM ReviewReaction rr " +
            "        WHERE rr.review.id = r.id AND rr.user.email = :userId) AS userReaction " +
            "FROM Review r " +
            "LEFT JOIN r.reactions rr " +
            "WHERE r.recommended = :recommended AND r.product.id = :productId " +
            "GROUP BY r.id " +
            "ORDER BY SUM(CASE WHEN rr.reactionType = 'LIKE' THEN 1 ELSE 0 END) DESC, " +
            "         SUM(CASE WHEN rr.reactionType = 'DISLIKE' THEN 1 ELSE 0 END) ASC")
        List<Object[]> findReviewsOrderedByLikesWithUserReaction(
            @Param("recommended") final boolean recommended,
            @Param("productId") UUID productId,
            @Param("userId") String userId);
        
}
