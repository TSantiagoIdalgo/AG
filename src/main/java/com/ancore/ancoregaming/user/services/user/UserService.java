package com.ancore.ancoregaming.user.services.user;

import com.ancore.ancoregaming.auth.services.JwtService;
import com.ancore.ancoregaming.user.dtos.UpdateUserDTO;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.repositories.IUserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

  @Autowired
  private IUserRepository userRepository;
  @Autowired
  private JwtService jwtService;

  @Override
  public List<User> findUsers() {
    List<User> users = this.userRepository.findAll();
    if (users.isEmpty()) {
      throw new EntityNotFoundException("There are no users!");
    }

    return users;
  }

  @Override
  public User findUser(String userId) {
    Optional<User> user = this.userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new EntityNotFoundException("User not found");
    }

    return user.get();
  }

  @Override
  public User destroyUser(String userId) {
    User user = this.findUser(userId);
    this.userRepository.delete(user);

    return user;
  }

  @Override
  public User updateUser(String userId, UpdateUserDTO updateUser) {
    User userFound = this.findUser(userId);
    if (updateUser.getUsername().isPresent()) {
      userFound.setUsername(updateUser.getUsername().get());
    }
    if (updateUser.getEmail().isPresent()) {
      userFound.setEmail(updateUser.getEmail().get());
    }
    this.userRepository.save(userFound);

    return userFound;
  }

  @Override
  public User verifyUser(String token) {
    String username = this.jwtService.extractUsername(token);
    if (username == null) {
      throw new JwtException("Token is invalid");
    }
    User user = this.findUser(username);
    if (!this.jwtService.isTokenValid(token, user)) {
      throw new JwtException("Token is invalid");
    }

    user.setVerify(true);
    return this.userRepository.save(user);
  }

}
