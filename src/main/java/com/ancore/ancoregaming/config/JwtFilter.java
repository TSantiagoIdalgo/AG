/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ancore.ancoregaming.config;

import com.ancore.ancoregaming.auth.JwtService;
import com.ancore.ancoregaming.user.IUserRepository;
import com.ancore.ancoregaming.user.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author tomas
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final IUserRepository userRepo;
  private final UserDetailsService userDeailsService;

  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
          throws ServletException, IOException {
    if (request.getServletPath().contains("/auth")) {
      chain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.toLowerCase().startsWith("bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    final String jwtToken = authHeader.substring(7);
    final String username = jwtService.extractUsername(jwtToken);
    if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(request, response);
      return;
    }

    UserDetails userDetails = this.userDeailsService.loadUserByUsername(username);
    Optional<User> user = userRepo.findById(userDetails.getUsername());

    if (!jwtService.isTokenValid(jwtToken, user.get())) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());

    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);

    chain.doFilter(request, response);
  }
}
