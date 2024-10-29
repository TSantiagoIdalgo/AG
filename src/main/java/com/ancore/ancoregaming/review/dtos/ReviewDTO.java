package com.ancore.ancoregaming.review.dtos;

import com.ancore.ancoregaming.user.dtos.UserDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

  @NotNull
  private String title;
  @NotNull
  private String comment;
  @NotNull
  @Positive
  private double rating;
  private UserDTO user;

  public ReviewDTO() {
  }

  public ReviewDTO(String title, String comment, double rating, UserDTO user) {
    this.title = title;
    this.comment = comment;
    this.rating = rating;
    this.user = user;
  }

  @Override
  public String toString() {
    return "ReviewDTO{" + ", title=" + title + ", comment=" + comment + ", rating=" + rating + ", user=" + user + '}';
  }

}
