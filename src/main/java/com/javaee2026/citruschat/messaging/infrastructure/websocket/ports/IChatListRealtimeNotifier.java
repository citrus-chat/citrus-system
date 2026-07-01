package com.javaee2026.citruschat.messaging.infrastructure.websocket.ports;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;

public interface IChatListRealtimeNotifier {
	void notifyChatroomCreated(ChatRoomId chatRoomId);
}
