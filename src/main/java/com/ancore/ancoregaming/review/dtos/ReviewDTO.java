package com.ancore.ancoregaming.review.dtos;

import com.ancore.ancoregaming.user.dtos.UserDTO;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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
