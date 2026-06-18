package com.javaee2026.citruschat.identity.domain.valueobjects;

import java.util.Base64;

public record PublicKey(String value) {

	private static final int X25519_PUBLIC_KEY_LENGTH = 32;

	public PublicKey {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("Public key is required");
		}

		try {
			byte[] decoded = Base64.getDecoder().decode(value);

			if (decoded.length != X25519_PUBLIC_KEY_LENGTH) {
				throw new IllegalArgumentException("Invalid X25519 public key length");
			}

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid public key format", e);
		}
	}

	public byte[] asBytes() {
		return Base64.getDecoder().decode(value);
	}
}
