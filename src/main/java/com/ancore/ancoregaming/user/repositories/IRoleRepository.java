package com.ancore.ancoregaming.user.repositories;

import com.ancore.ancoregaming.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {

  public Role findByName(String name);
}
