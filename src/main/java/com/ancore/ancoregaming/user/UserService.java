package com.ancore.ancoregaming.user;

import com.ancore.ancoregaming.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

  @Autowired
  private IUserRepository userRepository;

  @Override
  public List<User> findUsers() {
    List<User> users = this.userRepository.findAll();
    if (users.isEmpty()) {
      throw new EntityNotFoundException("There are no users!");
    }

    return users;
  }

  @Override
  public User findUser() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public User destroyUser(String userId) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public User updateUser(String userId, User user) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

}
