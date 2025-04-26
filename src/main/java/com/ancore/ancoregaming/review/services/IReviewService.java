package com.ancore.ancoregaming.review.services;

import java.util.List;

import com.ancore.ancoregaming.review.dtos.*;
import com.ancore.ancoregaming.review.model.ReviewReaction;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import com.ancore.ancoregaming.review.model.Review;

public interface IReviewService {

  public Page<Review> findAllReviews(ReviewFilter filter);

  public Review findReview(String reviewId);
  
  public List<Review> findUserReview(UserDetails userDetails);

  public List<Review> findProductReviews(String productId);
  
  public List<ReviewUserReaction> findProductReviewsWithUserReaction(String productId, String userId);

  public Review createReview(String productId, ReviewDTO reviewDTO, UserDetails userDetails);

  public Review updateReview(String reviewId, UpdateReviewDTO updateReviewDTO);

  public Review deleteReview(String reviewId);

  public ReviewReaction addReaction(String userId, String reviewId, ReactionType reactionType);

  public ReviewRecommendationDTO getRecommendationPercentage(String productId);

  public long getReviewsCount();
}
