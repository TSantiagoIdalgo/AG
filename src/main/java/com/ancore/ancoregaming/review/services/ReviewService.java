package com.ancore.ancoregaming.review.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ancore.ancoregaming.review.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.model.CartItem;
import com.ancore.ancoregaming.cart.repositories.ICartItemRepository;
import com.ancore.ancoregaming.cart.repositories.ICartRepository;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.services.product.IProductService;
import com.ancore.ancoregaming.review.model.Review;
import com.ancore.ancoregaming.review.model.ReviewReaction;
import com.ancore.ancoregaming.review.repositories.IReviewReactionRepository;
import com.ancore.ancoregaming.review.repositories.IReviewRepository;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.services.user.IUserService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ReviewService implements IReviewService {

  @Autowired
  private IReviewRepository reviewRepository;
  @Autowired
  private IProductService productService;
  @Autowired
  private IUserService userService;
  @Autowired
  private IReviewReactionRepository reviewReactionRepository;
  @Autowired
  private ICartRepository cartRepository;
  @Autowired
  private ICartItemRepository cartItemRepository;

  @Override
  public Page<Review> findAllReviews(ReviewFilter filter) {
    Specification<Review> spec = ReviewSpecification.orderByCreatedAt(filter.isOrderByCreatedAt());

    Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize());
    
    return reviewRepository.findAll(spec, pageable);
  }

  @Override
  public Review findReview(String reviewId) {
    Optional<Review> review = this.reviewRepository.findById(UUID.fromString(reviewId));
    if (review.isEmpty()) {
      throw new EntityNotFoundException("Review are empty");
    }

    return review.get();
  }
  
  @Override
  public List<Review> findUserReview(UserDetails userDetails) {
    List<Review> userReviews = this.reviewRepository.findReviewByUserEmail(userDetails.getUsername());
    if (userReviews.isEmpty()) {
      throw new EntityNotFoundException("User has not reviews");
    }
    return userReviews;
  }
  
  @Override
  public List<Review> findProductReviews(String productId, boolean recommended) {
    List<Review> reviews = this.reviewRepository.findReviewsOrderedByLikes(recommended, UUID.fromString(productId));
    if (reviews.isEmpty()) {
      throw new EntityNotFoundException("The product has no reviews");
    }

    return reviews;
  }
  
  @Override
  public List<ReviewUserReaction> findProductReviewsWithUserReaction(String productId, boolean recommended, String userId) {
    List<Object[]> reviews = this.reviewRepository.findReviewsOrderedByLikesWithUserReaction(recommended, UUID.fromString(productId), userId);
    if (reviews.isEmpty()) throw new EntityNotFoundException("The product has no reviews");

    return reviews.stream()
        .map(res -> {
          Review review = (Review) res[0];
          ReactionType reaction = (ReactionType) res[1];
          return ReviewUserReaction.builder()
              .id(review.getId())
              .reactions(review.getReactions())
              .title(review.getTitle())
              .createdAt(review.getCreatedAt())
              .comment(review.getComment())
              .reactionType(reaction)
              .product(review.getProduct())
              .user(review.getUser())
              .recommended(review.isRecommended())
              .build();
        })
        .toList();
  }
  
  @Override
  public Review createReview(String productId, ReviewDTO reviewDTO, UserDetails userDetails) {
    User user = this.userService.findUser(userDetails.getUsername());
    Product product = this.productService.findProduct(productId);

    Review reviewFound = this.reviewRepository.findByProductIdAndUserEmail(product.getId(), user.getEmail());
    if (reviewFound != null) {
      throw new EntityExistsException("There is already a review from this user for this product");
    }

    Optional<Cart> userCart = this.cartRepository.findByUserEmailWithPaidItems(user.getEmail());
    if (userCart.isEmpty()) {
      throw new EntityNotFoundException("User paid cart not found");
    }
    List<CartItem> paidCartItem = this.cartItemRepository
        .findPaidCartItemByCartIdAndProductId(userCart.get().getId(), product.getId());
    if (paidCartItem.isEmpty()) {
      throw new EntityNotFoundException("Paid item not found");
    }
    Review review = new Review.Builder()
        .setTitle(reviewDTO.getTitle())
        .setComment(reviewDTO.getComment())
        .setRating(reviewDTO.isRecommended())
        .setProduct(paidCartItem.get(0).getProduct())
        .setUser(user)
        .build();
    this.reviewRepository.save(review);
    return review;
  }

  @Override
  public Review updateReview(String reviewId, UpdateReviewDTO updateReviewDTO) {
    Review review = this.findReview(reviewId);
    for (Method method : updateReviewDTO.getClass().getMethods()) {
      if (method.getName().startsWith("get") && method.getReturnType().equals(Optional.class)) {
        try {
          Optional<?> value = (Optional<?>) method.invoke(updateReviewDTO);
          if (value.isPresent()) {
            value.ifPresent(val -> setReviewField(review, method.getName().substring(3), val));
          }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          throw new RuntimeException("Error actualizando campos de la review: " + e.getMessage());
        }
      }
    }
    return this.reviewRepository.save(review);
  }

  @Override
  public ReviewReaction addReaction(String userId, String reviewId, ReactionType reactionType) {
    Optional<ReviewReaction> existingReaction = reviewReactionRepository.findByUserEmailAndReviewId(userId,
        UUID.fromString(reviewId));
    if (existingReaction.isPresent()) {
      if (existingReaction.get().getReactionType().equals(reactionType)) {
        this.reviewReactionRepository.delete(existingReaction.get());
      } else {
        existingReaction.get().setReactionType(reactionType);
        this.reviewReactionRepository.save(existingReaction.get());
      }
      
      return existingReaction.get();
    }

    User user = this.userService.findUser(userId);
    Review review = this.findReview(reviewId);
    ReviewReaction reaction = new ReviewReaction(user, review, reactionType);
    return this.reviewReactionRepository.save(reaction);
  }

  @Override
  public Review deleteReview(String reviewId) {
    Review review = this.findReview(reviewId);
    this.reviewRepository.delete(review);
    return review;
  }

  @Override
  public ReviewRecommendationDTO getRecommendationPercentage(String productId) {
    Long reviewsCount = this.reviewRepository.countReviewsByProductId(UUID.fromString(productId));
    if (reviewsCount == 0) throw  new EntityNotFoundException("There are no reviews for this product");
    Double average = this.reviewRepository.findRecommendationPercentageByProductId(UUID.fromString(productId));
    
    return new ReviewRecommendationDTO(productId, (average * 100), reviewsCount);
  }

  private void setReviewField(Review review, String fieldName, Object value) {
    try {
      if (value instanceof List || value == null) {
        return;
      }
      Method setter = review.getClass().getMethod("set" + fieldName, value.getClass());
      setter.invoke(review, value);
    } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException
        | InvocationTargetException e) {
      throw new RuntimeException("Error asignando el campo " + fieldName + "  " + e.getMessage());
    }
  }

  @Override
  public long getReviewsCount() {
    return this.reviewRepository.count();
  }

}
