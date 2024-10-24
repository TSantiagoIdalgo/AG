package com.ancore.ancoregaming.user.services.role;

import com.ancore.ancoregaming.user.model.Role;
import java.util.List;

public interface IRoleService {

  public Role createRole(String name);

  public List<Role> findAllRoles();

  public Role findRoleByName(String name);
}
