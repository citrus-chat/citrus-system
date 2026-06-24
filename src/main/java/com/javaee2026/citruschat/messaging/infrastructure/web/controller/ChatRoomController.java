package com.javaee2026.citruschat.messaging.infrastructure.web.controller;

import com.javaee2026.citruschat.messaging.application.commands.SyncMessagesCommand;
import com.javaee2026.citruschat.messaging.application.results.CreateChatRoomResult;
import com.javaee2026.citruschat.messaging.application.results.SyncChatRoomResult;
import com.javaee2026.citruschat.messaging.application.results.SyncMessagesResult;
import com.javaee2026.citruschat.messaging.application.results.UploadConversationKeyResult;
import com.javaee2026.citruschat.messaging.application.usecases.CreateChatRoomUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.SyncChatRoomUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.SyncMessagesUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.UploadConversationKeyUseCase;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.CreateChatRoomRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.UploadConversationKeyRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.CreateChatRoomResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.SyncChatRoomResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.SyncMessagesResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.UploadConversationKeyResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.mapper.CreateChatRoomWebMapper;
import com.javaee2026.citruschat.messaging.infrastructure.web.mapper.SyncChatRoomWebMapper;
import com.javaee2026.citruschat.messaging.infrastructure.web.mapper.SyncMessagesWebMapper;
import com.javaee2026.citruschat.messaging.infrastructure.web.mapper.UploadConversationKeyWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
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
public class ChatRoomController {

	private final CreateChatRoomUseCase createChatRoomUseCase;
	private final SyncChatRoomUseCase syncChatRoomUseCase;
	private final SyncMessagesUseCase syncMessagesUseCase;
	private final UploadConversationKeyUseCase uploadConversationKeyUseCase;

	public ChatRoomController(CreateChatRoomUseCase createChatRoomUseCase, SyncChatRoomUseCase syncChatRoomUseCase,
			SyncMessagesUseCase syncMessagesUseCase, UploadConversationKeyUseCase uploadConversationKeyUseCase) {
		this.createChatRoomUseCase = createChatRoomUseCase;
		this.syncChatRoomUseCase = syncChatRoomUseCase;
		this.syncMessagesUseCase = syncMessagesUseCase;
		this.uploadConversationKeyUseCase = uploadConversationKeyUseCase;
	}

	@PostMapping(ApiRoutes.API_CHAT_ROOMS_CREATE)
	public ResponseEntity<ApiResponse<CreateChatRoomResponse>> send(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody CreateChatRoomRequest request) {
		UUID creatorId = UUID.fromString(jwt.getSubject());

		CreateChatRoomResult result = createChatRoomUseCase
				.execute(CreateChatRoomWebMapper.toCommand(request, creatorId));

		return ApiResponses.created(ApiResponseMessages.CHAT_ROOM_CREATION_SUCCESS,
				CreateChatRoomWebMapper.toResponse(result));
	}

	@GetMapping(ApiRoutes.API_CHAT_ROOMS_SYNC)
	public ResponseEntity<ApiResponse<SyncChatRoomResponse>> sync(@RequestParam UUID deviceId) {

		SyncChatRoomResult result = syncChatRoomUseCase.execute(new DeviceId(deviceId));

		return ApiResponses.ok(ApiResponseMessages.CHAT_ROOM_RETRIEVED_SUCCESS,
				SyncChatRoomWebMapper.toResponse(result));
	}

	@GetMapping(ApiRoutes.API_CHAT_ROOM_MESSAGES_SYNC)
	public ResponseEntity<ApiResponse<SyncMessagesResponse>> syncMessages(@PathVariable UUID chatroomId,
			@RequestParam(required = false) UUID lastMessageId) {

		SyncMessagesResult result = syncMessagesUseCase.execute(new SyncMessagesCommand(new ChatRoomId(chatroomId),
				lastMessageId != null ? new MessageId(lastMessageId) : null));

		return ApiResponses.ok(ApiResponseMessages.MESSAGES_RETRIEVED_SUCCESS,
				SyncMessagesWebMapper.toResponse(result));
	}

	@PostMapping(ApiRoutes.API_CHAT_ROOM_CONVERSATION_KEY)
	public ResponseEntity<ApiResponse<UploadConversationKeyResponse>> uploadConversationKey(
			@Valid @RequestBody UploadConversationKeyRequest request) {

		UploadConversationKeyResult result = uploadConversationKeyUseCase
				.execute(UploadConversationKeyWebMapper.toCommand(request));

		return ApiResponses.created(ApiResponseMessages.CONVERSATION_KEY_UPLOADED_SUCCESS,
				UploadConversationKeyWebMapper.toResponse(result));
	}
}
