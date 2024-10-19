package com.ancore.ancoregaming.auth;

import com.ancore.ancoregaming.auth.dtos.JwtResponse;
import com.ancore.ancoregaming.auth.dtos.LoginDTO;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.common.ResponseMessage;
import com.ancore.ancoregaming.user.dtos.UserDTO;
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
  public ResponseEntity<ApiResponse<JwtResponse>> register(@Valid @RequestBody final UserDTO user) {
    JwtResponse tokenResponse = authService.createUser(user);
    ApiResponse<JwtResponse> response = new ApiResponse<>(ResponseMessage.CREATED, tokenResponse, null);
    return ResponseEntity.status(201).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody final LoginDTO login) {
    JwtResponse loginResponse = authService.login(login);
    ApiResponse<JwtResponse> response = new ApiResponse<>(ResponseMessage.OK, loginResponse, null);
    return ResponseEntity.status(200).body(response);
  }
}
