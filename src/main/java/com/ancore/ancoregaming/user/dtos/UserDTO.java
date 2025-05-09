package com.ancore.ancoregaming.user.dtos;

import com.ancore.ancoregaming.user.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserDTO {

  private String username;
  private String email;
  private Date createdAt;
  private List<Role> roles;
}
