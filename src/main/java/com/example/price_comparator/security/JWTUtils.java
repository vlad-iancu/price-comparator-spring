package com.example.price_comparator.security;

import java.util.Date;

import javax.crypto.SecretKey;

import com.example.price_comparator.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTUtils {
    
    private static final String SECRET_KEY_VALUE = System.getenv("JWT_SECRET_KEY");
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_VALUE.getBytes());
    private static final long EXPIRATION_MILLIS = 3600000; // 1 hour

    // Generate JWT
    public static String generateToken(Long id) {
        long nowMillis = System.currentTimeMillis();
        Date issuedAt = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + EXPIRATION_MILLIS);

        return Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extract all claims
    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract username
    public static String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract issued at
    public static Date extractIssuedAt(String token) {
        return extractAllClaims(token).getIssuedAt();
    }

    // Extract expiration
    public static Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // Validate if token is still valid
    public static boolean isTokenValid(String token) {
        try {
            return extractExpiration(token).after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public static User getUserFromJwtUser(org.springframework.security.oauth2.jwt.Jwt jwtUser) {
        Long id = Long.valueOf(jwtUser.getSubject());
        User user = new User("", "");
        user.setId(id);
        return user;
    }
}
