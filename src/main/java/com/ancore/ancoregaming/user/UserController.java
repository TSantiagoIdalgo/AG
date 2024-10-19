package com.ancore.ancoregaming.user;

import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.common.ResponseMessage;
import com.ancore.ancoregaming.user.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private IUserService userService;

  @GetMapping("/")
  public ResponseEntity<ApiResponse<List<User>>> findAllUsers() {
    List<User> users = this.userService.findUsers();
    ApiResponse<List<User>> response = new ApiResponse<>(ResponseMessage.OK, users, null);

    return ResponseEntity.status(200).body(response);
  }
}
