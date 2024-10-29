package com.ancore.ancoregaming.review.controllers;

import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.review.dtos.ReviewDTO;
import com.ancore.ancoregaming.review.dtos.UpdateReviewDTO;
import com.ancore.ancoregaming.review.model.Review;
import com.ancore.ancoregaming.review.services.IReviewService;
import jakarta.validation.Valid;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
public class ReviewController {

  @Autowired
  private IReviewService reviewService;
  private final ModelMapper modelMapper = new ModelMapper();

  @GetMapping("/")
  public ResponseEntity<ApiResponse<List<ReviewDTO>>> getAllReviews() {
    List<Review> reviews = this.reviewService.findAllReviews();
    List<ReviewDTO> reviewsDTO = modelMapper.map(
            reviews,
            new TypeToken<List<ReviewDTO>>() {
            }.getType());
    ApiResponse<List<ReviewDTO>> response = new ApiResponse<>(HttpStatus.OK, reviewsDTO, null);
    return ResponseEntity.status(200).body(response);
  }

  @GetMapping("/{reviewId}")
  public ResponseEntity<ApiResponse<ReviewDTO>> getReview(@PathVariable String reviewId) {
    Review review = this.reviewService.findReview(reviewId);
    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(HttpStatus.OK, reviewDTO, null);
    return ResponseEntity.status(200).body(response);
  }

  @PostMapping("/{productId}")
  public ResponseEntity<ApiResponse<ReviewDTO>> createReview(
          @PathVariable String productId,
          @Valid @RequestBody ReviewDTO reviewDTO,
          @AuthenticationPrincipal UserDetails user) {
    Review newReview = this.reviewService.createReview(productId, reviewDTO, user);
    ReviewDTO newReviewDTO = modelMapper.map(newReview, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(HttpStatus.OK, newReviewDTO, null);
    return ResponseEntity.status(200).body(response);
  }

  @PatchMapping("/{reviewId}")
  public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@PathVariable String reviewId, @RequestBody UpdateReviewDTO updateReviewDTO) {
    Review review = this.reviewService.updateReview(reviewId, updateReviewDTO);
    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(HttpStatus.OK, reviewDTO, null);
    return ResponseEntity.status(200).body(response);
  }

  @DeleteMapping("/{reviewId}")
  public ResponseEntity<ApiResponse<ReviewDTO>> deleteReview(@PathVariable String reviewId) {
    Review review = this.reviewService.deleteReview(reviewId);
    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
    ApiResponse<ReviewDTO> response = new ApiResponse<>(HttpStatus.OK, reviewDTO, null);
    return ResponseEntity.status(200).body(response);

  }
}
