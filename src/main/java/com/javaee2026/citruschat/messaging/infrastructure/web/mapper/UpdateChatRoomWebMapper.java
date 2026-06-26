package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.commands.UpdateChatRoomCommand;
import com.javaee2026.citruschat.messaging.application.results.UpdateChatRoomResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.UpdateChatRoomRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.UpdateChatRoomResponse;

import java.util.UUID;

public final class UpdateChatRoomWebMapper {
    private UpdateChatRoomWebMapper() {
    }

    public static UpdateChatRoomCommand toCommand(UpdateChatRoomRequest request,  UUID chatRoomId, UUID requesterId) {
        return new UpdateChatRoomCommand(chatRoomId, requesterId,request.name());
    }

    public static UpdateChatRoomResponse toResponse(UpdateChatRoomResult result) {
        return new UpdateChatRoomResponse(result.id().value(), result.name(), result.avatarUrl(), result.updatedAt());
    }
}
