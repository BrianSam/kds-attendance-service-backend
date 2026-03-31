package com.example.kds_attendance_service_backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt.secret}")
    private  String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSignedKey(){
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userID,String role){
        return Jwts.builder()
                .setSubject(String.valueOf(userID))
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ NEW: extract userId
    public Long extractUserId(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    // ✅ NEW: extract role
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // ✅ NEW: validate token
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT :{} ",e.getMessage());
            return false;
        }
    }

}
