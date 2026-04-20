package com.innowise.userservice.Util;

import com.innowise.userservice.entity.enums.AuthRole;
import com.innowise.userservice.exception.InvalidOrExpiredToken;
import com.innowise.userservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class JwtUtilTest {
    private JwtUtil jwtUtil;

    private final String secret = "your-very-long-secret-your-very-long-secret";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret);
    }

    @Test
    void shouldGenerateToken() {
        String token = jwtUtil.generateToken(1L, AuthRole.ADMIN);

        assertThat(token).isNotNull();
    }

    @Test
    void shouldGenerateRefreshToken() {
        String token = jwtUtil.generateRefreshToken(1L);

        Claims claims = jwtUtil.validateToken(token);

        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.get("role")).isNull();
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        String token = jwtUtil.generateToken(1L, AuthRole.ADMIN);

        Claims claims = jwtUtil.validateToken(token);

        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.get("role")).isEqualTo("ADMIN");
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.validateToken("broken.token.here"))
                .isInstanceOf(InvalidOrExpiredToken.class);
    }

    @Test
    void shouldExtractUserId() {
        String token = jwtUtil.generateToken(42L, AuthRole.ADMIN);

        Long userId = jwtUtil.extractUserId(token);

        assertThat(userId).isEqualTo(42L);
    }

    @Test
    void shouldExtractRole() {
        String token = jwtUtil.generateToken(1L, AuthRole.ADMIN);

        String role = jwtUtil.extractRole(token);

        assertThat(role).isEqualTo("ADMIN");
    }
}
