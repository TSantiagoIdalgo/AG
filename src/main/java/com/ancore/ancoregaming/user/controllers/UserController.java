package com.ancore.ancoregaming.user.controllers;

import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.common.ResponseMessage;
import com.ancore.ancoregaming.user.services.user.IUserService;
import com.ancore.ancoregaming.user.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private IUserService userService;

  @Secured("ROLE_ADMIN")
  @GetMapping("/")
  public ResponseEntity<ApiResponse<List<User>>> findAllUsers() {
    List<User> users = this.userService.findUsers();
    ApiResponse<List<User>> response = new ApiResponse<>(ResponseMessage.OK, users, null);

    return ResponseEntity.status(200).body(response);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse<User>> findUser(@PathVariable String userId) {
    User userFound = this.userService.findUser(userId);

    ApiResponse<User> response = new ApiResponse<>(ResponseMessage.OK, userFound, null);

    return ResponseEntity.status(200).body(response);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<ApiResponse<User>> deleteUser(@PathVariable String userId) {
    User userDeleted = this.userService.destroyUser(userId);
    ApiResponse<User> response = new ApiResponse<>(ResponseMessage.OK, userDeleted, null);

    return ResponseEntity.status(200).body(response);
  }

  @PutMapping("/{userId}")
  public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable String userId, @RequestBody User user) {
    User userUpdated = this.userService.updateUser(userId, user);
    ApiResponse<User> response = new ApiResponse<>(ResponseMessage.OK, userUpdated, null);

    return ResponseEntity.status(200).body(response);
  }

  @GetMapping("/verify")
  public ResponseEntity<ApiResponse<User>> verifyUser(@RequestParam String token) {
    User user = this.userService.verifyUser(token);
    ApiResponse<User> response = new ApiResponse<>(ResponseMessage.OK, user, null);

    return ResponseEntity.status(200).body(response);
  }
}
