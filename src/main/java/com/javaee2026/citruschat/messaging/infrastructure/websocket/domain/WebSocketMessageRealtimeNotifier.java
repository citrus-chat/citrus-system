package com.javaee2026.citruschat.messaging.infrastructure.websocket.domain;

import com.javaee2026.citruschat.messaging.infrastructure.websocket.ports.IMessageRealtimeNotifier;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.NewMessageEvent;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class WebSocketMessageRealtimeNotifier implements IMessageRealtimeNotifier {

	private final SimpMessageSendingOperations messagingTemplate;

	public WebSocketMessageRealtimeNotifier(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void notifyMessageCreated(ChatRoomId chatRoomId) {

		messagingTemplate.convertAndSend("/topic/chatrooms/" + chatRoomId.value(),
				new NewMessageEvent(chatRoomId.value()));
	}

	@Override
	public void notifyKeysUpdated(ChatRoomId chatRoomId) {

		messagingTemplate.convertAndSend("/topic/chatrooms/" + chatRoomId.value(),
				new NewMessageEvent(chatRoomId.value()));
	}
}
