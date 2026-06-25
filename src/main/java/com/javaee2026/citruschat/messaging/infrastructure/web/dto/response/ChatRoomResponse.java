package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatRoomResponse(UUID id, String type, String name, UUID createdBy,
		List<ChatParticipantResponse> participants, Instant createdAt, Instant updatedAt, Instant deletedAt) {
}
