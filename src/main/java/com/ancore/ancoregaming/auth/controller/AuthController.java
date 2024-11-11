package com.ancore.ancoregaming.auth.controller;

import com.ancore.ancoregaming.auth.services.AuthService;
import com.ancore.ancoregaming.auth.dtos.JwtResponse;
import com.ancore.ancoregaming.auth.dtos.LoginDTO;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.user.dtos.CreateUserDTO;
import com.ancore.ancoregaming.user.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody CreateUserDTO user) {
    User userResponse = authService.createUser(user);
    ApiResponse<User> response = new ApiResponse<>(HttpStatus.CREATED, userResponse, null);
    return ResponseEntity.status(201).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<?>> login(HttpServletResponse res, @Valid @RequestBody final LoginDTO login) {
    JwtResponse loginResponse = authService.login(login);
    Cookie jwtCookie = getCookie("access_token", loginResponse.access_token());
    Cookie refreshJwtCookie = getCookie("refresh_token", loginResponse.refresh_token());

    res.addCookie(jwtCookie);
    res.addCookie(refreshJwtCookie);

    ApiResponse<?> response = new ApiResponse<>(HttpStatus.OK, null, null);
    return ResponseEntity.status(200).body(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<?>> logout(HttpServletResponse res) {
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

    ApiResponse<?> response = new ApiResponse<>(HttpStatus.OK, null, null);
    return ResponseEntity.status(200).body(response);
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
