package com.ancore.ancoregaming.auth.services;

import com.ancore.ancoregaming.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secretKey;
  @Value("${jwt.expiration}")
  private long secretExpiration;
  @Value("${jwt.refresh-token.expiration}")
  private long secretRefreshExpiration;

  public String generateToken(final User user) {
    return buildToken(user, secretExpiration);
  }

  public String generateRefreshToken(final User user) {
    return buildToken(user, secretRefreshExpiration);
  }

  public String extractUsername(String token) {
    Claims jwtToken = Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
    return jwtToken.getSubject();
  }

  public boolean isTokenValid(String token, User user) {
    String username = extractUsername(token);
    return (username.equals(user.getEmail())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    final Claims jwtToken = Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
    return jwtToken.getExpiration();
  }

  private String buildToken(final User user, final long expiration) {
    return Jwts.builder()
            .claims(Map.of("name", user.getUsername()))
            .subject(user.getEmail())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignKey())
            .compact();
  }

  private SecretKey getSignKey() {
    // Decodifica el secret a base64 y usara el algoritmo SHA para generar el token
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
