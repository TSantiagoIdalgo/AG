package com.ancore.ancoregaming.config.configurations;

import com.ancore.ancoregaming.config.filters.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class JwtConfig {

  private final AuthenticationProvider authenticationProvider;
  private final JwtFilter jwtFilter;
  private final UnauthenticatedConfig unauthenticatedConfig;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req -> req.requestMatchers("/auth/**").permitAll()
            .requestMatchers("/api/checkout/webhook").permitAll()
            .requestMatchers("/api/checkout/sse").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/whitelist/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/review/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/platform/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/genre/**").permitAll()
            .anyRequest().authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthenticatedConfig));

    return http.build();
  }
}
