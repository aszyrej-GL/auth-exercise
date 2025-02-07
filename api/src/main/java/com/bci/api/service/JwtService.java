package com.bci.api.service;

import com.bci.api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Service
public class JwtService {

    private final String secretKey;

    public JwtService(@Value("${application.security.jwt.secret-key}") final String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(final User user) {
        final String uuid = user.getUuid();
        final String email = user.getEmail();
        return Jwts.builder()
                .id(uuid)
                .claim("uuid", uuid)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserUuidFromAuthHeader(final String authHeader) {
        if (Objects.isNull(authHeader) || !authHeader.strip().startsWith("Bearer")) {
            throw new InsufficientAuthenticationException("Invalid Token");
        }
        final String token = authHeader.strip().substring(7);
        return extractUuid(token);
    }

    private String extractUuid(String token) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        final String uuid = (String) claims.get("uuid");
        if (Objects.isNull(uuid) || uuid.isBlank()) {
            throw new InsufficientAuthenticationException("Invalid Token");
        }
        return uuid;
    }
}
