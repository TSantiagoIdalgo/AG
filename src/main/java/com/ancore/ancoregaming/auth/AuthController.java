package com.ancore.ancoregaming.auth;

import com.ancore.ancoregaming.auth.dtos.JwtResponse;
import com.ancore.ancoregaming.auth.dtos.LoginDTO;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.common.ResponseMessage;
import com.ancore.ancoregaming.user.dtos.UserDTO;
import com.ancore.ancoregaming.user.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody UserDTO user) {
    User userResponse = authService.createUser(user);
    ApiResponse<User> response = new ApiResponse<>(ResponseMessage.CREATED, userResponse, null);
    return ResponseEntity.status(201).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<?>> login(HttpServletResponse res, @Valid @RequestBody final LoginDTO login) {
    JwtResponse loginResponse = authService.login(login);
    Cookie jwtCookie = getCookie("access_token", loginResponse.access_token());
    Cookie refreshJwtCookie = getCookie("refresh_token", loginResponse.refresh_token());

    res.addCookie(jwtCookie);
    res.addCookie(refreshJwtCookie);

    ApiResponse response = new ApiResponse<>(ResponseMessage.OK, null, null);
    return ResponseEntity.status(200).body(response);
  }

  private Cookie getCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setHttpOnly(true); // Aumenta la seguridad evitando acceso desde JavaScript
    cookie.setSecure(true); // Solo se enviará por HTTPS
    cookie.setMaxAge(24 * 60 * 60); // Duración de 1 día
    cookie.setPath("/");

    return cookie;
  }
}
