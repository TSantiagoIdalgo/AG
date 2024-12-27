package com.ancore.ancoregaming.review.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import com.ancore.ancoregaming.review.dtos.ReactionType;
import com.ancore.ancoregaming.review.dtos.ReviewDTO;
import com.ancore.ancoregaming.review.dtos.ReviewFilter;
import com.ancore.ancoregaming.review.dtos.UpdateReviewDTO;
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
    Page<Review> pagedResult = reviewRepository.findAll(spec, pageable);

    return pagedResult;
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
  public List<Review> findProductReviews(String productId, boolean recommended) {
    List<Review> reviews = this.reviewRepository.findReviewsOrderedByLikes(recommended, UUID.fromString(productId));
    if (reviews.isEmpty()) {
      throw new EntityNotFoundException("The product has no reviews");
    }

    return reviews;
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
          if (value != null) {
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
  public Review addReaction(String userId, String reviewId, ReactionType reactionType) {
    Optional<ReviewReaction> existingReaction = reviewReactionRepository.findByUserEmailAndReviewId(userId,
        UUID.fromString(reviewId));
    if (existingReaction.isPresent()) {
      throw new IllegalStateException("User has already reacted to this review");
    }

    User user = this.userService.findUser(userId);
    Review review = this.findReview(reviewId);

    ReviewReaction reaction = new ReviewReaction(user, review, reactionType);

    this.reviewReactionRepository.save(reaction);
    return review;
  }

  @Override
  public Review deleteReview(String reviewId) {
    Review review = this.findReview(reviewId);
    this.reviewRepository.delete(review);
    return review;
  }

  @Override
  public Double getRecommendationPercentage(String productId) {
    return this.reviewRepository.findRecommendationPercentageByProductId(UUID.fromString(productId));
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
