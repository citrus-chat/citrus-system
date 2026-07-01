package com.javaee2026.citruschat.identity.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWebLoginTokenRequest(@NotNull(message = "WebDeviceId is required") UUID webDeviceId,

		@NotBlank(message = "DeviceName is required") String deviceName,

		@NotBlank(message = "PublicKey is required") String publicKey) {
}
