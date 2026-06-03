package com.javaee2026.citruschat.identity.domain.valueobjects;

import java.util.Base64;
import java.util.Objects;

public record PublicIdentityKey(String value) {

	public PublicIdentityKey {
		Objects.requireNonNull(value, "Public identity key cannot be null");

		if (value.isBlank()) {
			throw new IllegalArgumentException("Public identity key cannot be blank");
		}

		try {
			Base64.getDecoder().decode(value);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Invalid public identity key format");
		}
	}

	@Override
	public String toString() {
		return value;
	}
}
