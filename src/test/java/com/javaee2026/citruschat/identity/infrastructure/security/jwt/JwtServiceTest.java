package com.javaee2026.citruschat.identity.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class JwtServiceTest {

	private static final String JWT_SECRET = "27305f9519ddc64736b8f7662eecb5ec2c0eca3a12714ee08ea6b648897907a2";

	private final JwtService jwtService = new JwtService();

	@Test
	void shouldGenerateValidJwtToken() {
		String token = jwtService.generateToken("91ae5825-9096-4c74-9447-1bf03004c36b",
				"91ae5825-9096-4c74-7654-1bf03004c36b", "test@gmail.com", "test_test");

		assertNotNull(token);
		assertEquals(3, token.split("\\.").length);

		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();

		assertEquals("91ae5825-9096-4c74-9447-1bf03004c36b", claims.getSubject());
		assertEquals("test@gmail.com", claims.get("email", String.class));
		assertEquals("test_test", claims.get("username", String.class));
		assertNotNull(claims.getIssuedAt());
		assertNotNull(claims.getExpiration());
		assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
	}
}
