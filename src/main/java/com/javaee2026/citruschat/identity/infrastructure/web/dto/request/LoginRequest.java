package com.javaee2026.citruschat.identity.infrastructure.web.dto.request;

import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record LoginRequest(
		@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

		@NotBlank(message = "Password is required") String password,

		UUID deviceId, String deviceName, DeviceType deviceType,

		String publicIdentityKey,

		SignedPreKeyRequest signedPreKey,

		List<OneTimePreKeyRequest> oneTimePreKeys) {
}
