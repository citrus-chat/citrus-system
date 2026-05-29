package com.javaee2026.citruschat.messaging.infrastructure.websocket.controller;

import com.javaee2026.citruschat.messaging.application.commands.SendMessageCommand;
import com.javaee2026.citruschat.messaging.application.results.SendMessageResult;
import com.javaee2026.citruschat.messaging.application.usecases.SendMessageUseCase;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.request.ChatMessageWsRequest;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.response.ChatMessageWsResponse;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.mapper.ChatWebSocketMapper;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class ChatWebSocketController {

	private final SendMessageUseCase sendMessageUseCase;
	private final SimpMessageSendingOperations messagingTemplate;

	public ChatWebSocketController(SendMessageUseCase sendMessageUseCase,
			SimpMessageSendingOperations messagingTemplate) {
		this.sendMessageUseCase = sendMessageUseCase;
		this.messagingTemplate = messagingTemplate;
	}

	@MessageMapping(ApiRoutes.WS_CHAT_SEND_MESSAGE)
	public void sendMessage(@Payload ChatMessageWsRequest request, Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return;
		}

		if (isInvalidRequest(request)) {
			return;
		}

		UUID senderUserId = UUID.fromString(authentication.getName());

		SendMessageCommand command = ChatWebSocketMapper.toCommand(request, senderUserId);

		SendMessageResult result = sendMessageUseCase.execute(command);

		ChatMessageWsResponse response = ChatWebSocketMapper.fromResult(result, senderUserId);

		messagingTemplate.convertAndSend(ApiRoutes.WS_CHATROOM_TOPIC_PREFIX + request.chatRoomId(), response);
	}

	private boolean isInvalidRequest(ChatMessageWsRequest request) {
		return request == null || request.chatRoomId() == null || request.senderDeviceId() == null
				|| request.payloads() == null || request.payloads().isEmpty()
				|| request.payloads().stream().anyMatch(payload -> payload == null || payload.targetDeviceId() == null
						|| payload.encryptedPayload() == null || payload.encryptedPayload().isBlank());
	}
}
