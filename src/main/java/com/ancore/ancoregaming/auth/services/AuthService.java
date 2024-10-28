package com.ancore.ancoregaming.auth.services;

import com.ancore.ancoregaming.auth.dtos.JwtResponse;
import com.ancore.ancoregaming.auth.dtos.LoginDTO;
import com.ancore.ancoregaming.user.dtos.CreateUserDTO;
import com.ancore.ancoregaming.user.model.Role;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.repositories.IUserRepository;
import com.ancore.ancoregaming.user.services.role.RoleService;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final IUserRepository userRepository;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

  public User createUser(CreateUserDTO user) {
    String passwordHash = passwordEncoder.encode(user.getPassword());
    List<Role> roles = new ArrayList<>();
    Role role = roleService.findRoleByName("ROLE_ADMIN");
    if (role == null) {
      Role newRole = roleService.createRole("ROLE_ADMIN");
      roles.add(newRole);
    }
    User newUser = new User(user.getUsername(), user.getEmail(), passwordHash, false, roles);
    this.userRepository.save(newUser);

    return newUser;
  }

  public JwtResponse login(LoginDTO login) throws EntityNotFoundException {
    authManager.authenticate(new UsernamePasswordAuthenticationToken(login.email(), login.password()));
    User user = userRepository.findById(login.email()).orElseThrow(() -> new EntityNotFoundException("User not found"));

    String token = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    return new JwtResponse(token, refreshToken);
  }

  public JwtResponse refresh(final String refreshToken) {
    if (refreshToken == null) {
      throw new IllegalArgumentException("Invalid bearer token");
    }

    String email = jwtService.extractUsername(refreshToken);
    if (email == null) {
      throw new IllegalArgumentException("Invalid refresh token");
    }

    User user = userRepository.findById(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

    if (!jwtService.isTokenValid(refreshToken, user)) {
      throw new IllegalArgumentException("Invalid refresh token");
    }

    final String accessToken = jwtService.generateToken(user);
    return new JwtResponse(accessToken, refreshToken);
  }

}
