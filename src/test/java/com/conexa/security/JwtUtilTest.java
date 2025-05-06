package com.conexa.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class JwtUtilTest {
    private JwtUtil jwtUtil;

    private final String jwtSecret = "======================starwarapi=Spring===========================";

    private final int expirationMs = 1000 * 60 * 60; // 1 hora

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", expirationMs);
    }

    @Test
    void generateTokenValidToken() {
        String token = jwtUtil.generateToken("sebastian");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsernameCorrectUsername() {
        String token = jwtUtil.generateToken("svidal");
        String username = jwtUtil.extractUsername(token);
        assertEquals("svidal", username);
    }

    @Test
    void validateTokenValid() {
        String token = jwtUtil.generateToken("testuser");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateTokenIsExpired() throws InterruptedException {
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 1);
        String token = jwtUtil.generateToken("svidal");

        Thread.sleep(5);

        assertFalse(jwtUtil.validateToken(token));
    }
}