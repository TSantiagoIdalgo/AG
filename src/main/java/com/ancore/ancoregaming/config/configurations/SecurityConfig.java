package com.ancore.ancoregaming.config.configurations;

import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.repositories.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Habilita Spring Security en el proyecto
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private final IUserRepository userRepo;

  @Bean // Este annotation le indica a Spring que la instancia que devuelve el metodo la
        // guarde en su contexto
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request -> request.requestMatchers("/auth/**").permitAll()
            .requestMatchers("/webhook").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/**").permitAll()
            .anyRequest().authenticated());
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() throws EntityNotFoundException {
    return (String userEmail) -> {
      User user = userRepo.findUserWithVerifyTrue(userEmail)
          .orElseThrow(() -> new EntityNotFoundException("User not found"));

      List<GrantedAuthority> authorities = user.getRoles().stream()
          .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

      return org.springframework.security.core.userdetails.User.builder()
          .username(user.getEmail())
          .password(user.getPassword())
          .authorities(authorities)
          .build();
    };
  }

  // Se declara PasswordEncoder para asi definir que tipo de encriptacion va a
  // tener las passwords
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();

  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }
}
