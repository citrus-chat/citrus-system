package com.javaee2026.citruschat.messaging.infrastructure.websocket.domain;

import com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.NewChatroomEvent;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.ports.IChatListRealtimeNotifier;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class WebSocketChatListRealtimeNotifier implements IChatListRealtimeNotifier {

	private final SimpMessageSendingOperations messagingTemplate;

	public WebSocketChatListRealtimeNotifier(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void notifyChatroomCreated(ChatRoomId chatRoomId) {

		messagingTemplate.convertAndSend("/topic/chatrooms", new NewChatroomEvent());
	}
}
