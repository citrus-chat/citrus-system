package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.util.UUID;

public record ChatPermissionResponse(UUID permissionId, String code, String description) {
}
