package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;

import java.time.Instant;
import java.util.UUID;

public record UserDeviceResponse(UUID id,

		@JsonProperty("public_key") String publicKey,

		@JsonProperty("device_name") String deviceName,

		@JsonProperty("device_type") DeviceType deviceType,

		@JsonProperty("last_seen") Instant lastSeen,

		@JsonProperty("created_at") Instant createdAt) {
}