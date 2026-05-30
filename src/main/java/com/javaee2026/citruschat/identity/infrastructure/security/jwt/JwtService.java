package com.javaee2026.citruschat.identity.infrastructure.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

	private static final String JWT_SECRET = "27305f9519ddc64736b8f7662eecb5ec2c0eca3a12714ee08ea6b648897907a2";

	private static final long EXPIRATION_MILLIS = 1000L * 60 * 60 * 24;

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String userId, String email, String username) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + EXPIRATION_MILLIS);

		return Jwts.builder().subject(userId).claim("email", email).claim("username", username).issuedAt(now)
				.expiration(expiration).signWith(getSigningKey(), Jwts.SIG.HS256).compact();
	}

	public Long getExpirationInSeconds() {
		return EXPIRATION_MILLIS / 1000;
	}
}
