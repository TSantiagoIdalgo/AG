package com.ancore.ancoregaming.review.dtos;

import java.util.Date;
import java.util.List;

import com.ancore.ancoregaming.product.dtos.ProductDTO;
import com.ancore.ancoregaming.user.dtos.UserDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

  private String id;
  @NotNull
  private String title;
  @NotNull
  private String comment;
  @NotNull
  private boolean recommended;
  private UserDTO user;
  private List<ReviewReactionDTO> reactions;
  private Date createdAt;
  private ProductDTO product;

  public ReviewDTO() {
  }

  public ReviewDTO(String title, String comment, boolean recommended, UserDTO user) {
    this.title = title;
    this.comment = comment;
    this.recommended = recommended;
    this.user = user;
  }

  @Override
  public String toString() {
    return "ReviewDTO{" + ", title=" + title + ", comment=" + comment + ", recommended=" + recommended + ", user="
        + user + '}';
  }

}
