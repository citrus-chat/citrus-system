package com.javaee2026.citruschat.messaging.application.commands;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;

public record GetChatRolesCommand(ChatRoomId chatRoomId) {
}
