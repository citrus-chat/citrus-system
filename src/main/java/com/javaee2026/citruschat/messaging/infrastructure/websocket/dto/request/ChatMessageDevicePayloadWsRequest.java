package com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.request;

import java.util.UUID;

public record ChatMessageDevicePayloadWsRequest(UUID targetDeviceId, String encryptedPayload) {
}
