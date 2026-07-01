package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.util.List;
import java.util.UUID;

public record UpdateParticipantRolesResponse(UUID participantId, UUID chatRoomId, UUID userId, List<UUID> roleIds,
		List<ChatPermissionResponse> permissions) {
}
