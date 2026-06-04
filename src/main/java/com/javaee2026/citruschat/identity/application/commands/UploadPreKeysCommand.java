package com.javaee2026.citruschat.identity.application.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;
import java.util.UUID;

public record UploadPreKeysCommand(UUID deviceId,

		@NotBlank String publicIdentityKey,

		SignedPreKeyCommand signedPreKey,

		@NotEmpty List<OneTimePreKeyCommand> oneTimePreKeys) {
	public record SignedPreKeyCommand(@PositiveOrZero int keyId,

			@NotBlank String publicKey,

			@NotBlank String signature) {
	}

	public record OneTimePreKeyCommand(@PositiveOrZero int keyId,

			@NotBlank String publicKey) {
	}
}
