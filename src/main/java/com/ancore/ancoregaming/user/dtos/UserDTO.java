package com.ancore.ancoregaming.user.dtos;

import com.ancore.ancoregaming.user.model.Role;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

  private String username;
  private String email;
  private String password;
  private boolean verify;
  private List<Role> roles;
}
