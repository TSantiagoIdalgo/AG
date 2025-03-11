package com.ancore.ancoregaming.review.controllers;

import java.util.List;

import com.ancore.ancoregaming.review.dtos.*;
import com.ancore.ancoregaming.review.model.ReviewReaction;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.review.model.Review;
import com.ancore.ancoregaming.review.services.IReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/review")
public class ReviewController {

  private final IReviewService reviewService;
  private final ModelMapper modelMapper = new ModelMapper();

  @Autowired
  public ReviewController(IReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @GetMapping("/")
  public ApiEntityResponse<Page<ReviewDTO>> getAllReviews(@ModelAttribute ReviewFilter filterDTO) {
    Page<Review> reviews = this.reviewService.findAllReviews(filterDTO);
    Page<ReviewDTO> reviewsDTO = reviews.map((review) -> modelMapper.map(review, ReviewDTO.class));

    ApiResponse<Page<ReviewDTO>> response = new ApiResponse<>(reviewsDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/product/{productId}")
  public ApiEntityResponse<List<ReviewDTO>> getAllProductReviews(@PathVariable String productId,
      @RequestParam boolean recommended,
      @AuthenticationPrincipal UserDetails user) {
    if (user != null) {
      List<ReviewUserReaction> reviews = this.reviewService.findProductReviewsWithUserReaction(productId, recommended, user.getUsername());

      List<ReviewDTO> reviewsDTO = modelMapper.map(
          reviews,
          new TypeToken<List<ReviewDTO>>() {
          }.getType());
      ApiResponse<List<ReviewDTO>> response = new ApiResponse<>(reviewsDTO, null);
      return ApiEntityResponse.of(HttpStatus.OK, response);
    }
    List<Review> reviews = this.reviewService.findProductReviews(productId, recommended);
    List<ReviewDTO> reviewsDTO = modelMapper.map(
        reviews,
        new TypeToken<List<ReviewDTO>>() {
        }.getType());
    ApiResponse<List<ReviewDTO>> response = new ApiResponse<>(reviewsDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/{reviewId}")
  public ApiEntityResponse<ReviewDTO> getReview(@PathVariable String reviewId) {
    Review review = this.reviewService.findReview(reviewId);
    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(reviewDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/recommendation/{productId}")
  public ApiEntityResponse<ReviewRecommendationDTO> getReviewRecommendation(@PathVariable String productId) {
    ReviewRecommendationDTO reviewRecommendationDTO = this.reviewService.getRecommendationPercentage(productId);
    ApiResponse<ReviewRecommendationDTO> response = new ApiResponse<>(reviewRecommendationDTO, null);

    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @PostMapping("/{productId}")
  public ApiEntityResponse<ReviewDTO> createReview(@PathVariable String productId,
      @Valid @RequestBody ReviewDTO reviewDTO, @AuthenticationPrincipal UserDetails user) {
    Review newReview = this.reviewService.createReview(productId, reviewDTO, user);
    ReviewDTO newReviewDTO = modelMapper.map(newReview, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(newReviewDTO, null);
    return ApiEntityResponse.of(HttpStatus.CREATED, response);
  }

  @PatchMapping("/{reviewId}")
  public ApiEntityResponse<ReviewDTO> updateReview(@PathVariable String reviewId,
      @RequestBody UpdateReviewDTO updateReviewDTO) {
    Review review = this.reviewService.updateReview(reviewId, updateReviewDTO);
    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(reviewDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @DeleteMapping("/{reviewId}")
  public ApiEntityResponse<ReviewDTO> deleteReview(@PathVariable String reviewId) {
    Review review = this.reviewService.deleteReview(reviewId);
    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(reviewDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);

  }

  @PostMapping("/reaction/")
  public ApiEntityResponse<ReviewReactionDTO> addReviewReaction(@RequestBody ReactionRequestDTO reviewReactionDTO,
      @AuthenticationPrincipal UserDetails user) {
    ReviewReaction reaction = this.reviewService.addReaction(user.getUsername(), reviewReactionDTO.getReviewId(),
        reviewReactionDTO.getReactionType());
    ReviewReactionDTO reactionDTO = modelMapper.map(reaction, ReviewReactionDTO.class);
    ApiResponse<ReviewReactionDTO> response = new ApiResponse<>(reactionDTO, null);
    return ApiEntityResponse.of(HttpStatus.CREATED, response);
  }

  @GetMapping("/count")
  public ApiEntityResponse<ReviewCount> getReviewsCount() {
    long count = this.reviewService.getReviewsCount();
    ReviewCount reviewCount = new ReviewCount(count);
    ApiResponse<ReviewCount> response = new ApiResponse<>(reviewCount, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  public record ReviewCount(long count) {
  }
}
