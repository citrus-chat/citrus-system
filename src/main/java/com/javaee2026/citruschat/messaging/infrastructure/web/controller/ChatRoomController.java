package com.javaee2026.citruschat.messaging.infrastructure.web.controller;

import com.javaee2026.citruschat.messaging.application.commands.GetParticipantPermissionsCommand;
import com.javaee2026.citruschat.messaging.application.commands.SyncMessagesCommand;
import com.javaee2026.citruschat.messaging.application.results.*;
import com.javaee2026.citruschat.messaging.application.usecases.*;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.CreateChatRoomRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.UploadConversationKeyRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.*;
import com.javaee2026.citruschat.messaging.infrastructure.web.mapper.*;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
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
	private final GetParticipantPermissionsUseCase getParticipantPermissionsUseCase;

	public ChatRoomController(CreateChatRoomUseCase createChatRoomUseCase, SyncChatRoomUseCase syncChatRoomUseCase,
			SyncMessagesUseCase syncMessagesUseCase, UploadConversationKeyUseCase uploadConversationKeyUseCase,
			GetParticipantPermissionsUseCase getParticipantPermissionsUseCase) {
		this.createChatRoomUseCase = createChatRoomUseCase;
		this.syncChatRoomUseCase = syncChatRoomUseCase;
		this.syncMessagesUseCase = syncMessagesUseCase;
		this.uploadConversationKeyUseCase = uploadConversationKeyUseCase;
		this.getParticipantPermissionsUseCase = getParticipantPermissionsUseCase;
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

	@GetMapping(ApiRoutes.API_CHAT_ROOM_PARTICIPANT_PERMISSION)
	public ResponseEntity<ApiResponse<ParticipantPermissionsResponse>> getParticipantPermissions(
			@PathVariable UUID chatroomId, @PathVariable UUID participantId) {

		GetParticipantPermissionsResult result = getParticipantPermissionsUseCase.execute(
				new GetParticipantPermissionsCommand(new ChatRoomId(chatroomId), new ParticipantId(participantId)));

		return ApiResponses.ok(ApiResponseMessages.PARTICIPANT_PERMISSIONS_RETRIEVED_SUCCESS,
				GetParticipantPermissionsWebMapper.toResponse(result));
	}
}
