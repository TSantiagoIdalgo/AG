package com.ancore.ancoregaming.auth.services;

import com.ancore.ancoregaming.auth.dtos.JwtResponse;
import com.ancore.ancoregaming.auth.dtos.LoginDTO;
import com.ancore.ancoregaming.email.dtos.EmailDTO;
import com.ancore.ancoregaming.email.services.EmailService;
import com.ancore.ancoregaming.user.dtos.CreateUserDTO;
import com.ancore.ancoregaming.user.model.Role;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.repositories.IUserRepository;
import com.ancore.ancoregaming.user.services.role.RoleService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
  private final EmailService emailService;

  public User createUser(CreateUserDTO user) {
    Optional<User> userOptional = this.userRepository.findById(user.getEmail());
    User userWithUsername = this.userRepository.findByUsername(user.getUsername());
    if (userWithUsername != null) {
      throw new DataIntegrityViolationException("There is already a user with that name");
    } else if (userOptional.isPresent()) {
      throw new EntityExistsException("User already exist");
    }

    String passwordHash = passwordEncoder.encode(user.getPassword());
    List<Role> roles = new ArrayList<>();
    Role role = roleService.findRoleByName("ROLE_ADMIN");
    if (role == null) {
      Role newRole = roleService.createRole("ROLE_ADMIN");
      roles.add(newRole);
    } else {
      roles.add(role);
    }
    User newUser = new User(user.getUsername(), user.getEmail(), passwordHash, false, roles);
    this.userRepository.save(newUser);

    EmailDTO emailDTO = new EmailDTO();
    emailDTO.setAddressee(user.getEmail());
    emailDTO.setUsername(user.getUsername());
    emailDTO.setToken(jwtService.generateToken(newUser));
    this.emailService.sendRegisterMail(emailDTO);

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
