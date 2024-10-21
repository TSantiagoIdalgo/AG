package com.ancore.ancoregaming.config;

import com.ancore.ancoregaming.auth.JwtService;
import com.ancore.ancoregaming.user.IUserRepository;
import com.ancore.ancoregaming.user.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final IUserRepository userRepo;
  private final UserDetailsService userDeailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
          throws ServletException, IOException {
    if (request.getServletPath().contains("/auth")) {
      chain.doFilter(request, response);
      return;
    }

    Optional<Cookie> cookieJwt = Optional.ofNullable(request.getCookies())
            .stream()
            .flatMap(Arrays::stream)
            .filter(cookie -> "access_token".equals(cookie.getName()))
            .findFirst();

    if (cookieJwt.isEmpty()) {
      chain.doFilter(request, response);
      return;
    }

    final String jwtToken = cookieJwt.get().getValue();
    String username = jwtService.extractUsername(jwtToken);

    if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(request, response);
      return;
    }

    Optional<User> user = userRepo.findById(username);
    if (user.isEmpty() || !jwtService.isTokenValid(jwtToken, user.get())) {
      chain.doFilter(request, response);
      return;
    }
    UserDetails userDetails = this.userDeailsService.loadUserByUsername(username);
    setAuthentication(userDetails, request);

    chain.doFilter(request, response);
  }

  private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());

    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authToken);
  }
}
