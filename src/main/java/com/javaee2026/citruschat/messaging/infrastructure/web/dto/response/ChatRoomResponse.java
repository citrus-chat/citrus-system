package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ChatRoomResponse(UUID id, String type, String name, UUID createdBy, Instant createdAt, Instant updatedAt,
		Instant deletedAt) {
}
