package com.javaee2026.citruschat.messaging.infrastructure.websocket.controller;

import com.javaee2026.citruschat.identity.application.usecases.ValidateUserDeviceOwnershipUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.SendMessageUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.ValidateChatParticipantUseCase;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

	private final SendMessageUseCase sendMessageUseCase;
	private final SimpMessageSendingOperations messagingTemplate;
	private final ValidateUserDeviceOwnershipUseCase validateUserDeviceOwnershipUseCase;
	private final ValidateChatParticipantUseCase validateChatParticipantUseCase;

	public ChatWebSocketController(SendMessageUseCase sendMessageUseCase,
			SimpMessageSendingOperations messagingTemplate,
			ValidateUserDeviceOwnershipUseCase validateUserDeviceOwnershipUseCase,
			ValidateChatParticipantUseCase validateChatParticipantUseCase) {
		this.sendMessageUseCase = sendMessageUseCase;
		this.messagingTemplate = messagingTemplate;
		this.validateUserDeviceOwnershipUseCase = validateUserDeviceOwnershipUseCase;
		this.validateChatParticipantUseCase = validateChatParticipantUseCase;
	}

	// @MessageMapping(ApiRoutes.WS_CHAT_SEND_MESSAGE)
	// public void sendMessage(@Payload ChatMessageWsRequest request, Authentication
	// authentication) {
	// if (authentication == null || !authentication.isAuthenticated()) {
	// return;
	// }
	//
	// if (isInvalidRequest(request)) {
	// return;
	// }
	//
	// UUID senderUserId = UUID.fromString(authentication.getName());
	//
	// if (!validateUserDeviceOwnershipUseCase.execute(senderUserId,
	// request.senderDeviceId())) {
	// return;
	// }
	//
	// if (!validateChatParticipantUseCase.execute(request.chatRoomId(),
	// senderUserId)) {
	// return;
	// }
	//
	// SendMessageCommand command = ChatWebSocketMapper.toCommand(request,
	// senderUserId);
	//
	// SendMessageResult result = sendMessageUseCase.execute(command);
	//
	// ChatMessageWsResponse response = ChatWebSocketMapper.fromResult(result,
	// senderUserId);
	//
	// messagingTemplate.convertAndSend(ApiRoutes.WS_CHATROOM_TOPIC_PREFIX +
	// request.chatRoomId(), response);
	// }

	// private boolean isInvalidRequest(ChatMessageWsRequest request) {
	// return request == null || request.chatRoomId() == null ||
	// request.senderDeviceId() == null
	// || request.payloads() == null || request.payloads().isEmpty()
	// || request.payloads().stream().anyMatch(payload -> payload == null ||
	// payload.targetDeviceId() == null
	// || payload.encryptedPayload() == null ||
	// payload.encryptedPayload().isBlank());
	// }
}
