package com.ancore.ancoregaming.auth;

import com.ancore.ancoregaming.auth.dtos.JwtResponse;
import com.ancore.ancoregaming.auth.dtos.LoginDTO;
import com.ancore.ancoregaming.user.IRoleRepository;
import com.ancore.ancoregaming.user.IUserRepository;
import com.ancore.ancoregaming.user.dtos.UserDTO;
import com.ancore.ancoregaming.user.model.Role;
import com.ancore.ancoregaming.user.model.User;
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
  private final IRoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

  public User createUser(UserDTO user) {
    String passwordHash = passwordEncoder.encode(user.getPassword());
    List<Role> roles = new ArrayList<>();
    Role role = new Role("USER");
    this.roleRepository.save(role);
    roles.add(role);
    User newUser = new User(user.getUsername(), user.getEmail(), passwordHash, false, roles);
    // Send email to user
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
