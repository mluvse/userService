package by.mluvse.UserService.serviceTests;


import by.mluvse.UserService.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;
    private SecretKey key;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        String secret = Base64.getEncoder()
                .encodeToString("my-secret-key-for-testing-purposes".getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(jwtService, "jwtSecret", secret);
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    @Test
    void parse_validToken_returnsClaims() {
        String token = Jwts.builder()
                .subject("testuser")
                .claim("userId", 1L)
                .claim("role", "USER")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(key)
                .compact();

        Claims claims = jwtService.parse(token);

        assertEquals("testuser", claims.getSubject());
        assertEquals(1L, claims.get("userId", Long.class));
        assertEquals("USER", claims.get("role", String.class));
    }

    @Test
    void parse_invalidToken_throwsException() {
        assertThrows(Exception.class, () -> jwtService.parse("invalid-token"));
    }
}
