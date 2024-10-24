package com.ancore.ancoregaming.user.services.role;

import com.ancore.ancoregaming.user.model.Role;
import com.ancore.ancoregaming.user.repositories.IRoleRepository;
import jakarta.persistence.EntityExistsException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {

  @Autowired
  private IRoleRepository roleRepository;

  @Override
  public Role createRole(String name) {
    Role roleFound = this.findRoleByName(name);
    if (roleFound != null) {
      throw new EntityExistsException("Role already exist");
    }
    Role newRole = new Role(name);
    this.roleRepository.save(newRole);

    return newRole;
  }

  @Override
  public List<Role> findAllRoles() {
    return this.roleRepository.findAll();
  }

  @Override
  public Role findRoleByName(String name) {
    return this.roleRepository.findByName(name);
  }

}
