package com.javaee2026.citruschat.messaging.application.results;

import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyDistribution;

import java.util.List;

public record SyncChatRoomResult(List<ChatRoom> chatRooms, List<ConversationKeyDistribution> conversationKeys) {
}
