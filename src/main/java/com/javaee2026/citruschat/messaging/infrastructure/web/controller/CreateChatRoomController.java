package com.javaee2026.citruschat.messaging.infrastructure.web.controller;

import com.javaee2026.citruschat.messaging.application.usecases.CreateChatRoomUseCase;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.CreateChatRoomRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.CreateChatRoomResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.mapper.CreateChatRoomWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CreateChatRoomController {

	private final CreateChatRoomUseCase createChatRoomUseCase;

	public CreateChatRoomController(CreateChatRoomUseCase createChatRoomUseCase) {
		this.createChatRoomUseCase = createChatRoomUseCase;
	}

	@PostMapping(ApiRoutes.API_CHAT_ROOMS_CREATE)
	public ResponseEntity<ApiResponse<CreateChatRoomResponse>> send(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody CreateChatRoomRequest request) {
		UUID creatorId = UUID.fromString(jwt.getSubject());

		createChatRoomUseCase.execute(CreateChatRoomWebMapper.toCommand(request, creatorId));

		return ApiResponses.created(ApiResponseMessages.CHAT_ROOM_CREATION_SUCCESS,
				CreateChatRoomWebMapper.toResponse());
	}
}
