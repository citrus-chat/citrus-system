package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatRoleResponse(UUID id, List<ChatPermissionResponse> chatPermissions, String name, int priority,
		Instant createdAt) {
}
