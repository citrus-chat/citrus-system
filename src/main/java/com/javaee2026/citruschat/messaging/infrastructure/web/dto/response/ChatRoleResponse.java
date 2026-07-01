package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatRoleResponse(UUID id, UUID chatRoomId, String name, Integer priority,
		List<ChatPermissionResponse> chatPermissions, Instant createdAt, Instant updatedAt) {
}
