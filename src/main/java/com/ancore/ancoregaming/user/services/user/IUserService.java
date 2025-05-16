package com.ancore.ancoregaming.user.services.user;

import com.ancore.ancoregaming.user.dtos.UpdateUserDTO;
import com.ancore.ancoregaming.user.model.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IUserService {

  public List<User> findUsers();

  public User findUser(String userId);

  public User destroyUser(String userId);

  public User updateUser(String userId, UpdateUserDTO user);

  public User verifyUser(String token);
  
  int countUsers();
}
