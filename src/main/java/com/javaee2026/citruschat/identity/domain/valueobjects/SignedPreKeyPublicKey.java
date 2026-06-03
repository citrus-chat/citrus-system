package com.javaee2026.citruschat.identity.domain.valueobjects;

import java.util.Base64;
import java.util.Objects;

public record SignedPreKeyPublicKey(String value) {

	public SignedPreKeyPublicKey {
		Objects.requireNonNull(value, "Signed prekey public key cannot be null");

		if (value.isBlank()) {
			throw new IllegalArgumentException("Signed prekey public key cannot be blank");
		}

		try {
			Base64.getDecoder().decode(value);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Invalid signed prekey public key");
		}
	}

	@Override
	public String toString() {
		return value;
	}
}
