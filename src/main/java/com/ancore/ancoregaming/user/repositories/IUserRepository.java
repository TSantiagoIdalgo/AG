package com.ancore.ancoregaming.user.repositories;

import com.ancore.ancoregaming.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {

  User findByEmail(String userEmail);
  
  User findByUsername(String username);

  @Query("SELECT u FROM User u WHERE u.email = :email AND u.verify = true")
  Optional<User> findUserWithVerifyTrue(@Param("email") String email);
  
  @Query("""
      SELECT COUNT(u) FROM User u
      LEFT JOIN u.roles r
      WHERE r.name = 'ROLE_USER'
      """)
  int countUsersWithRoleUser();
}
