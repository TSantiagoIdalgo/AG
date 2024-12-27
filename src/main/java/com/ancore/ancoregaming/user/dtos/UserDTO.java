package com.ancore.ancoregaming.user.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

  private String username;
  private String email;
  private boolean verify;
}
