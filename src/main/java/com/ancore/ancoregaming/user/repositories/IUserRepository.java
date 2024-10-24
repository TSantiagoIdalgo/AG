package com.ancore.ancoregaming.user.repositories;

import com.ancore.ancoregaming.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {

  User findByUsername(String username);
}
