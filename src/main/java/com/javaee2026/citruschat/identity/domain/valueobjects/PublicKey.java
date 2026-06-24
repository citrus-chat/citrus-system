package com.javaee2026.citruschat.identity.domain.valueobjects;

import java.util.Base64;

public record PublicKey(String value) {

	// P-256 uncompressed EC point
	private static final int P256_PUBLIC_KEY_LENGTH = 65;

	public PublicKey {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("Public key is required");
		}

		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(value);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid public key base64 format", e);
		}

		if (decoded.length != P256_PUBLIC_KEY_LENGTH) {
			throw new IllegalArgumentException(
					"Invalid P-256 public key length. Expected 65 bytes, got " + decoded.length);
		}

		// opcional: validar formato EC point (0x04)
		if (decoded[0] != 0x04) {
			throw new IllegalArgumentException("Invalid P-256 public key format (expected uncompressed point)");
		}
	}

	public byte[] asBytes() {
		return Base64.getDecoder().decode(value);
	}
}
