package com.varios.toolshub.security;

import com.varios.toolshub.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(@Value("${app.auth.jwt-secret}") String secret,
                      @Value("${app.auth.jwt-expiration-minutes}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(AuthUser user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.id().toString())
                .claim("username", user.username())
                .claim("role", user.role().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
                .signWith(key)
                .compact();
    }

    public AuthUser parse(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return new AuthUser(Long.parseLong(claims.getSubject()), claims.get("username", String.class),
                Role.valueOf(claims.get("role", String.class)));
    }
}
