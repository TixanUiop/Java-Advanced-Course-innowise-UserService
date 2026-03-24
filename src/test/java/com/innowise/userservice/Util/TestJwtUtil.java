package com.innowise.userservice.Util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class TestJwtUtil {

    private static final String SECRET = "com-innowise-authentication-service-secret-key";
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String generateTestToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(key)
                .compact();
    }
}
