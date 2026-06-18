package com.javaee2026.citruschat.identity.infrastructure.web.dto.request;

import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record DeviceRequest(
		@NotBlank(message = "DeviceId is required") @NotEmpty(message = "DeviceId cannot be empty") UUID deviceId,

		@NotBlank(message = "DeviceName is required") String deviceName,

		@NotBlank(message = "DeviceType is required") DeviceType deviceType,

		@NotBlank(message = "PublicKey is required") String publicKey) {
}
