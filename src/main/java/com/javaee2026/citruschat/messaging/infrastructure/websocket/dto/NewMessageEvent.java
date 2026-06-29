package com.javaee2026.citruschat.messaging.infrastructure.websocket.dto;

import java.util.UUID;

public record NewMessageEvent(UUID chatRoomId) {
}
