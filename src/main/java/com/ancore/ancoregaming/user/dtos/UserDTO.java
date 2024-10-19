package com.ancore.ancoregaming.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDTO {

  @NotNull(message = "Username cannot be null")
  @Size(min = 3, max = 50)
  public String username;
  @NotNull(message = "Email cannot be null")
  @Email(message = "Email should be valid")
  public String email;
  @NotNull(message = "Passwor cannot be null")
  public String password;
}
