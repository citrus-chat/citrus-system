package com.javaee2026.citruschat.messaging.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateChatRoomRequest(@NotBlank String name) {
}
