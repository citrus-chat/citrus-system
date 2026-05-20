package com.javaee2026.citruschat.messaging.infrastructure.web.dto.request;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateChatRoomRequest(@NotNull ChatRoomType chatRoomType, String name, List<UUID> participantIds) {
}
