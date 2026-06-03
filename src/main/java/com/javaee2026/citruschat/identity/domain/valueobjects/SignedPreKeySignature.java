package com.javaee2026.citruschat.identity.domain.valueobjects;

import java.util.Base64;
import java.util.Objects;

public record SignedPreKeySignature(String value) {

	public SignedPreKeySignature {
		Objects.requireNonNull(value, "Signed prekey signature cannot be null");

		if (value.isBlank()) {
			throw new IllegalArgumentException("Signed prekey signature cannot be blank");
		}

		try {
			Base64.getDecoder().decode(value);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Invalid signed prekey signature");
		}
	}

	@Override
	public String toString() {
		return value;
	}
}
