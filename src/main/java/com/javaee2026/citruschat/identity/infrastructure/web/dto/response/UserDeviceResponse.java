package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import com.javaee2026.citruschat.identity.domain.enums.DeviceType;

import java.time.Instant;
import java.util.UUID;

public record UserDeviceResponse(UUID id, String deviceName, DeviceType deviceType, Instant lastSeen,
		Instant createdAt) {
}
