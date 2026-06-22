package com.javaee2026.citruschat.messaging.infrastructure.web.dto.request;

import java.util.UUID;

public record SyncChatRoomRequest(UUID deviceId) {
}
