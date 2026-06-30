package com.javaee2026.citruschat.messaging.application.commands;

import java.util.UUID;

public record UpdateChatRoomCommand(UUID chatRoomId, UUID requesterId, String name) {
}
