package com.ancore.ancoregaming.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class CreateUserDTO {

  @NotNull(message = "Username cannot be null")
  @Size(min = 3, max = 50)
  private String username;
  @NotNull(message = "Email cannot be null")
  @Email(message = "Email should be valid")
  private String email;
  @NotNull(message = "Password cannot be null")
  private String password;

  public CreateUserDTO(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  
  @Override
  public String toString() {
    return "UserDTO{" + "username=" + username + ", email=" + email + ", password=" + password + '}';
  }

}
