package com.ancore.ancoregaming.review.controllers;

import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.review.dtos.ReactionRequestDTO;
import com.ancore.ancoregaming.review.dtos.ReviewDTO;
import com.ancore.ancoregaming.review.dtos.ReviewRecommendationDTO;
import com.ancore.ancoregaming.review.dtos.UpdateReviewDTO;
import com.ancore.ancoregaming.review.model.Review;
import com.ancore.ancoregaming.review.services.IReviewService;
import jakarta.validation.Valid;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
public class ReviewController {

  @Autowired
  private IReviewService reviewService;
  private final ModelMapper modelMapper = new ModelMapper();

  @GetMapping("/")
  public ApiEntityResponse<List<ReviewDTO>> getAllReviews(@RequestParam boolean recommended) {
    List<Review> reviews = this.reviewService.findAllReviews(recommended);
    List<ReviewDTO> reviewsDTO = modelMapper.map(
        reviews,
        new TypeToken<List<ReviewDTO>>() {
        }.getType());
    ApiResponse<List<ReviewDTO>> response = new ApiResponse<>(HttpStatus.OK, reviewsDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/product/{productId}")
  public ApiEntityResponse<List<ReviewDTO>> getAllProductReviews(@PathVariable String productId) {
    List<Review> reviews = this.reviewService.findProductReviews(productId);
    List<ReviewDTO> reviewsDTO = modelMapper.map(
        reviews,
        new TypeToken<List<ReviewDTO>>() {
        }.getType());
    ApiResponse<List<ReviewDTO>> response = new ApiResponse<>(HttpStatus.OK, reviewsDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/{reviewId}")
  public ApiEntityResponse<ReviewDTO> getReview(@PathVariable String reviewId) {
    Review review = this.reviewService.findReview(reviewId);
    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(HttpStatus.OK, reviewDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/recommendation/{productId}")
  public ApiEntityResponse<ReviewRecommendationDTO> getReviewRecommendation(@PathVariable String productId) {
    double percentage = this.reviewService.getRecommendationPercentage(productId);
    ReviewRecommendationDTO reviewRecommendationDTO = new ReviewRecommendationDTO(productId, (percentage * 100));
    ApiResponse<ReviewRecommendationDTO> response = new ApiResponse<>(HttpStatus.OK, reviewRecommendationDTO, null);

    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @PostMapping("/{productId}")
  public ApiEntityResponse<ReviewDTO> createReview(@PathVariable String productId,
      @Valid @RequestBody ReviewDTO reviewDTO, @AuthenticationPrincipal UserDetails user) {
    Review newReview = this.reviewService.createReview(productId, reviewDTO, user);
    ReviewDTO newReviewDTO = modelMapper.map(newReview, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(HttpStatus.OK, newReviewDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @PatchMapping("/{reviewId}")
  public ApiEntityResponse<ReviewDTO> updateReview(@PathVariable String reviewId,
      @RequestBody UpdateReviewDTO updateReviewDTO) {
    Review review = this.reviewService.updateReview(reviewId, updateReviewDTO);
    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(HttpStatus.OK, reviewDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @DeleteMapping("/{reviewId}")
  public ApiEntityResponse<ReviewDTO> deleteReview(@PathVariable String reviewId) {
    Review review = this.reviewService.deleteReview(reviewId);
    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(HttpStatus.OK, reviewDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);

  }

  @PostMapping("/reaction/")
  public ApiEntityResponse<ReviewDTO> addReviewReaction(@RequestBody ReactionRequestDTO reviewReactionDTO,
      @AuthenticationPrincipal UserDetails user) {
    Review review = this.reviewService.addReaction(user.getUsername(), reviewReactionDTO.getReviewId(),
        reviewReactionDTO.getReactionType());
    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(HttpStatus.OK, reviewDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }
}
