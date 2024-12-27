package com.ancore.ancoregaming.config.filters;

import java.io.IOException;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriTemplate;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAuthorizationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain chain)
      throws ServletException, IOException {
    if (request.getServletPath().contains("/auth")) {
      chain.doFilter(request, response);
      return;
    }

    String requestPath = request.getRequestURI();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() && requestPath.contains("user")) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof UserDetails userDetails) {
        String username = userDetails.getUsername();
        boolean isAdmin = userDetails
            .getAuthorities()
            .stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        UriTemplate template = new UriTemplate("/user/{userId}");
        Map<String, String> pathVariables = template.match(requestPath);
        String requestedUserId = pathVariables.get("userId");

        if (!isAdmin && !username.equals(requestedUserId)) {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          response.getWriter().write("You do not have permission to access this information.");
          return;
        }
      }
    }

    chain.doFilter(request, response);
  }

}
