package com.ancore.ancoregaming.auth.controller;

import com.ancore.ancoregaming.user.dtos.UserDTO;
import com.ancore.ancoregaming.user.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ancore.ancoregaming.auth.dtos.JwtResponse;
import com.ancore.ancoregaming.auth.dtos.LoginDTO;
import com.ancore.ancoregaming.auth.services.AuthService;
import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.user.dtos.CreateUserDTO;
import com.ancore.ancoregaming.user.model.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final UserService userService;
  private final ModelMapper modelMapper = new ModelMapper();
  
  @Autowired
  public AuthController(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }
  
  @PostMapping("/register")
  public ApiEntityResponse<User> register(@Valid @RequestBody CreateUserDTO user) {
    User userResponse = authService.createUser(user);
    ApiResponse<User> response = new ApiResponse<>(userResponse, null);
    return ApiEntityResponse.of(HttpStatus.CREATED, response);
  }

  @PostMapping("/login")
  public ApiEntityResponse<UserDTO> login(HttpServletResponse res, @Valid @RequestBody final LoginDTO login) {
    JwtResponse loginResponse = authService.login(login);
    Cookie jwtCookie = getCookie("access_token", loginResponse.access_token());
    Cookie refreshJwtCookie = getCookie("refresh_token", loginResponse.refresh_token());

    res.addCookie(jwtCookie);
    res.addCookie(refreshJwtCookie);
    
    User user = userService.findUser(login.email());
    UserDTO userDTO = modelMapper.map(user, UserDTO.class);

    ApiResponse<UserDTO> response = new ApiResponse<UserDTO>(userDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @PostMapping("/logout")
  public ApiEntityResponse<?> logout(HttpServletResponse res) {
    String[] cookiesToRemove = { "access_token", "refresh_token" };

    for (String cookieName : cookiesToRemove) {
      Cookie cookie = new Cookie(cookieName, null);
      cookie.setHttpOnly(true);
      cookie.setSecure(false);
      cookie.setPath("/");
      cookie.setMaxAge(0);
      res.addCookie(cookie);
    }
    SecurityContextHolder.clearContext();

    ApiResponse<?> response = new ApiResponse<>();
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  private Cookie getCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setHttpOnly(true); // Aumenta la seguridad evitando acceso desde JavaScript
    cookie.setSecure(false); // Solo se enviará por HTTPS
    cookie.setMaxAge(24 * 60 * 60); // Duración de 1 día
    cookie.setPath("/");

    return cookie;
  }
}
