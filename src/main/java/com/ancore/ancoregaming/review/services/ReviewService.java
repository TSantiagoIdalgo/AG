package com.ancore.ancoregaming.review.services;

import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.services.product.IProductService;
import com.ancore.ancoregaming.review.dtos.ReviewDTO;
import com.ancore.ancoregaming.review.dtos.UpdateReviewDTO;
import com.ancore.ancoregaming.review.model.Review;
import com.ancore.ancoregaming.review.repositories.IReviewRepository;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.services.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ReviewService implements IReviewService {

  @Autowired
  private IReviewRepository reviewRepository;
  @Autowired
  private IProductService productService;
  @Autowired
  private IUserService userService;

  @Override
  public List<Review> findAllReviews() {
    List<Review> reviews = this.reviewRepository.findAll();

    if (reviews.isEmpty()) {
      throw new EntityNotFoundException("Reviews are empty");
    }

    return reviews;
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
  public List<Review> findProductReviews(String productId) {
    List<Review> reviews = this.reviewRepository.findByProductId(UUID.fromString(productId));
    if (reviews.isEmpty()) {
      throw new EntityNotFoundException("The product has no reviews");
    }

    return reviews;
  }

  @Override
  public Review createReview(String productId, ReviewDTO reviewDTO, UserDetails userDetails) {
    User user = this.userService.findUser(userDetails.getUsername());
    Product product = this.productService.findProduct(productId);
    Review review = new Review.Builder()
            .setTitle(reviewDTO.getTitle())
            .setComment(reviewDTO.getComment())
            .setRating(reviewDTO.getRating())
            .setProduct(product)
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
  public Review deleteReview(String reviewId) {
    Review review = this.findReview(reviewId);
    this.reviewRepository.delete(review);
    return review;
  }

  private void setReviewField(Review review, String fieldName, Object value) {
    try {
      if (value instanceof List || value == null) {
        return;
      }
      Method setter = review.getClass().getMethod("set" + fieldName, value.getClass());
      setter.invoke(review, value);
    } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
      throw new RuntimeException("Error asignando el campo " + fieldName + "  " + e.getMessage());
    }
  }
}
