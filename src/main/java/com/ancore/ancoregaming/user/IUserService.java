package com.ancore.ancoregaming.user;

import com.ancore.ancoregaming.user.model.User;
import java.util.List;

public interface IUserService {

  public List<User> findUsers();

  public User findUser();

  public User destroyUser(String userId);

  public User updateUser(String userId, User user);
}
