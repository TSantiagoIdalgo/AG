package com.ancore.ancoregaming.user.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.user.dtos.UpdateUserDTO;
import com.ancore.ancoregaming.user.dtos.UserDTO;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.services.user.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private IUserService userService;
  private final ModelMapper modelMapper = new ModelMapper();

  @Secured("ROLE_ADMIN")
  @GetMapping("/")
  public ApiEntityResponse<List<UserDTO>> findAllUsers() {
    List<User> users = this.userService.findUsers();
    List<UserDTO> usersDTO = modelMapper.map(
        users,
        new TypeToken<List<UserDTO>>() {
        }.getType());
    ApiResponse<List<UserDTO>> response = new ApiResponse<>(usersDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/{userId}")
  public ApiEntityResponse<UserDTO> findUser(@PathVariable String userId) {
    User userFound = this.userService.findUser(userId);
    UserDTO userDTO = modelMapper.map(userFound, UserDTO.class);
    ApiResponse<UserDTO> response = new ApiResponse<>(userDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @DeleteMapping("/{userId}")
  public ApiEntityResponse<UserDTO> deleteUser(@PathVariable String userId) {
    User userDeleted = this.userService.destroyUser(userId);
    UserDTO userDTO = modelMapper.map(userDeleted, UserDTO.class);
    ApiResponse<UserDTO> response = new ApiResponse<>(userDTO, null);

    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @PatchMapping("/{userId}")
  public ApiEntityResponse<UserDTO> updateUser(@PathVariable String userId,
      @RequestBody UpdateUserDTO updateUser) {
    User userUpdated = this.userService.updateUser(userId, updateUser);
    UserDTO userDTO = modelMapper.map(userUpdated, UserDTO.class);
    ApiResponse<UserDTO> response = new ApiResponse<>(userDTO, null);

    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/verify")
  public ApiEntityResponse<UserDTO> verifyUser(@RequestParam String token) {
    User user = this.userService.verifyUser(token);
    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
    ApiResponse<UserDTO> response = new ApiResponse<>(userDTO, null);

    return ApiEntityResponse.of(HttpStatus.OK, response);
  }
}
