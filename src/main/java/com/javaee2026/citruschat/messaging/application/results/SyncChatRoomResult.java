package com.javaee2026.citruschat.messaging.application.results;

import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;

import java.util.List;

public record SyncChatRoomResult(List<ChatRoom> chatRooms) {
}
