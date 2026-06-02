package com.javaee2026.citruschat.identity.domain.valueobjects;

import java.util.Base64;
import java.util.Objects;

public record OneTimePreKeyPublicKey(String value) {

	public OneTimePreKeyPublicKey {
		Objects.requireNonNull(value, "One-time prekey public key cannot be null");

		if (value.isBlank()) {
			throw new IllegalArgumentException("One-time prekey public key cannot be blank");
		}

		try {
			Base64.getDecoder().decode(value);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Invalid one-time prekey public key");
		}
	}

	@Override
	public String toString() {
		return value;
	}
}
