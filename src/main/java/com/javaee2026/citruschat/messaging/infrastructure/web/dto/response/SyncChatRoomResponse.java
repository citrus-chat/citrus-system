package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.util.List;

public record SyncChatRoomResponse(List<ChatRoomResponse> chatRooms,
		List<ConversationKeyDistributionResponse> conversationKeys) {
}
