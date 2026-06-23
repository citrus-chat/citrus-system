package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.util.List;

public record SyncMessagesResponse(List<MessageResponse> messages) {
}
