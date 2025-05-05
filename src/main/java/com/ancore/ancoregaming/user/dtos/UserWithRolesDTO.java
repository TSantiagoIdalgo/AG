package com.ancore.ancoregaming.user.dtos;

import com.ancore.ancoregaming.user.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserWithRolesDTO extends UserDTO{
  
  private List<Role> roles;
  public UserWithRolesDTO() {
    super();
  }
}
