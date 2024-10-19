package com.ancore.ancoregaming.user;

import com.ancore.ancoregaming.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {

}
