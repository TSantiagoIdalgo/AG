package com.ancore.ancoregaming.config;

import com.ancore.ancoregaming.user.IUserRepository;
import com.ancore.ancoregaming.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Configuration es una annotation que indica que esta clase va a tener metodos que van a retornar un Bean, es decir, el resultado de los metodos Spring los guardara en su Context
@EnableWebSecurity // Habilita Spring Security en el proyecto
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private final IUserRepository userRepo;

  @Bean // Este annotation le indica a Spring que la instancia que devuelve el metodo, la guarde en su context
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(request
                    -> request.requestMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() throws EntityNotFoundException {
    return (String userEmail) -> {
      User user = userRepo.findById(userEmail).orElseThrow(() -> new EntityNotFoundException("User not found"));

      List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

      return org.springframework.security.core.userdetails.User.builder()
              .username(user.getEmail())
              .password(user.getPassword())
              .authorities(authorities)
              .build();
    };
  }

  // Se declara PasswordEncoder para asi definir que tipo de encriptacion va a tener las passwords
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();

  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
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
