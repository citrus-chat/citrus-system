package com.javaee2026.citruschat.messaging.infrastructure.web.controller;

import com.javaee2026.citruschat.messaging.application.usecases.SendMessageUseCase;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.SendMessageRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.SendMessageResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.mapper.SendMessageWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class SendMessageController {

	private final SendMessageUseCase sendMessageUseCase;

	public SendMessageController(SendMessageUseCase sendMessageUseCase) {
		this.sendMessageUseCase = sendMessageUseCase;
	}

	@PostMapping(ApiRoutes.API_CHAT_MESSAGES)
	public ResponseEntity<ApiResponse<SendMessageResponse>> send(@AuthenticationPrincipal Jwt jwt,
			@PathVariable UUID chatRoomId, @Valid @RequestBody SendMessageRequest request) {
		UUID senderUserId = UUID.fromString(jwt.getSubject());

		sendMessageUseCase.execute(SendMessageWebMapper.toCommand(chatRoomId, request, senderUserId));

		return ApiResponses.created(ApiResponseMessages.MESSAGE_SENT_SUCCESS, SendMessageWebMapper.toResponse());
	}
}
