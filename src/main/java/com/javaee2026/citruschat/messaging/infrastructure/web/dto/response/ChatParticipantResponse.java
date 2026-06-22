package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatParticipantResponse(UUID id, UUID chatRoomId, UUID userId, List<UUID> roleIds, Instant joinedAt,
		Instant leftAt, UUID lastReadMessageId) {
}
