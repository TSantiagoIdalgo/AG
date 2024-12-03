package com.ancore.ancoregaming.review.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.ancore.ancoregaming.review.dtos.ReactionType;
import com.ancore.ancoregaming.review.dtos.ReviewDTO;
import com.ancore.ancoregaming.review.dtos.UpdateReviewDTO;
import com.ancore.ancoregaming.review.model.Review;

public interface IReviewService {

  public List<Review> findAllReviews();

  public Review findReview(String reviewId);

  public List<Review> findProductReviews(String productId, boolean recommended);

  public Review createReview(String productId, ReviewDTO reviewDTO, UserDetails userDetails);

  public Review updateReview(String reviewId, UpdateReviewDTO updateReviewDTO);

  public Review deleteReview(String reviewId);

  public Review addReaction(String userId, String reviewId, ReactionType reactionType);

  public Double getRecommendationPercentage(String productId);

  public long getReviewsCount();
}
