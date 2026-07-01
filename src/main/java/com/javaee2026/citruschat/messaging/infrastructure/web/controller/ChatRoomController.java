package com.javaee2026.citruschat.messaging.infrastructure.web.controller;

import com.javaee2026.citruschat.messaging.application.commands.*;
import com.javaee2026.citruschat.messaging.application.results.*;
import com.javaee2026.citruschat.messaging.application.usecases.*;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.CreateChatRoleRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.CreateChatRoomRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.UpdateChatRoomRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.UpdateChatRoleRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.UpdateParticipantRolesRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.UploadConversationKeyRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.*;
import com.javaee2026.citruschat.messaging.infrastructure.web.mapper.*;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
public class ChatRoomController {

	private final CreateChatRoomUseCase createChatRoomUseCase;
	private final SyncChatRoomUseCase syncChatRoomUseCase;
	private final SyncMessagesUseCase syncMessagesUseCase;
	private final UploadConversationKeyUseCase uploadConversationKeyUseCase;
	private final GetParticipantPermissionsUseCase getParticipantPermissionsUseCase;
	private final UpdateChatRoomUseCase updateChatRoomUseCase;
	private final GetConversationKeysRequestsUseCase getConversationKeysRequestsUseCase;
	private final UpdateParticipantRolesUseCase updateParticipantRolesUseCase;
	private final GetChatRolesUseCase getChatRolesUseCase;
	private final GetChatRoleUseCase getChatRoleUseCase;
	private final CreateChatRoleUseCase createChatRoleUseCase;
	private final UpdateChatRoleUseCase updateChatRoleUseCase;
	private final DeleteChatRoleUseCase deleteChatRoleUseCase;
	private final GetAvailableChatPermissionsUseCase getAvailableChatPermissionsUseCase;

	public ChatRoomController(CreateChatRoomUseCase createChatRoomUseCase, SyncChatRoomUseCase syncChatRoomUseCase,
			SyncMessagesUseCase syncMessagesUseCase, UploadConversationKeyUseCase uploadConversationKeyUseCase,
			GetParticipantPermissionsUseCase getParticipantPermissionsUseCase,
			UpdateChatRoomUseCase updateChatRoomUseCase,
			GetConversationKeysRequestsUseCase getConversationKeysRequestsUseCase,
			UpdateParticipantRolesUseCase updateParticipantRolesUseCase, GetChatRolesUseCase getChatRolesUseCase,
			GetChatRoleUseCase getChatRoleUseCase, CreateChatRoleUseCase createChatRoleUseCase,
			UpdateChatRoleUseCase updateChatRoleUseCase, DeleteChatRoleUseCase deleteChatRoleUseCase,
			GetAvailableChatPermissionsUseCase getAvailableChatPermissionsUseCase) {
		this.createChatRoomUseCase = createChatRoomUseCase;
		this.syncChatRoomUseCase = syncChatRoomUseCase;
		this.syncMessagesUseCase = syncMessagesUseCase;
		this.uploadConversationKeyUseCase = uploadConversationKeyUseCase;
		this.getParticipantPermissionsUseCase = getParticipantPermissionsUseCase;
		this.updateChatRoomUseCase = updateChatRoomUseCase;
		this.getConversationKeysRequestsUseCase = getConversationKeysRequestsUseCase;
		this.updateParticipantRolesUseCase = updateParticipantRolesUseCase;
		this.getChatRolesUseCase = getChatRolesUseCase;
		this.getChatRoleUseCase = getChatRoleUseCase;
		this.createChatRoleUseCase = createChatRoleUseCase;
		this.updateChatRoleUseCase = updateChatRoleUseCase;
		this.deleteChatRoleUseCase = deleteChatRoleUseCase;
		this.getAvailableChatPermissionsUseCase = getAvailableChatPermissionsUseCase;
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
	public ResponseEntity<ApiResponse<SyncMessagesResponse>> syncMessages(@AuthenticationPrincipal Jwt jwt,
			@PathVariable UUID chatroomId, @RequestParam(required = false) Instant lastCreatedAt) {

		SyncMessagesResult result = syncMessagesUseCase.execute(new SyncMessagesCommand(new ChatRoomId(chatroomId),
				new UserId(UUID.fromString(jwt.getSubject())), lastCreatedAt));

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

	@PatchMapping(ApiRoutes.API_CHAT_ROOM_UPDATE_NAME)
	public ResponseEntity<ApiResponse<UpdateChatRoomResponse>> updateName(@AuthenticationPrincipal Jwt jwt,
			@PathVariable UUID chatroomId, @Valid @RequestBody UpdateChatRoomRequest request) {

		UUID requesterId = UUID.fromString(jwt.getSubject());
		UpdateChatRoomResult result = updateChatRoomUseCase
				.execute(UpdateChatRoomWebMapper.toCommand(request, chatroomId, requesterId));

		return ApiResponses.ok(ApiResponseMessages.CHAT_ROOM_UPDATED_SUCCESS,
				UpdateChatRoomWebMapper.toResponse(result));
	}

	@GetMapping(ApiRoutes.API_CHAT_ROOM_PENDING_CONVERSATION_KEY)
	public ResponseEntity<ApiResponse<List<PendingConversationKeyRequestResponse>>> getPendingRequests(
			@RequestParam UUID conversationId) {
		List<PendingConversationKeyRequestResult> result = getConversationKeysRequestsUseCase.execute(conversationId);

		return ApiResponses.ok(ApiResponseMessages.CONVERSATION_KEYS_RETRIEVED_SUCCESSFULLY,
				ConversationKeyRequestWebMapper.toResponse(result));
	}

	@PatchMapping(ApiRoutes.API_CHAT_ROOM_PARTICIPANT_ROLES)
	public ResponseEntity<ApiResponse<UpdateParticipantRolesResponse>> updateParticipantRoles(
			@AuthenticationPrincipal Jwt jwt, @PathVariable UUID chatroomId, @PathVariable UUID participantId,
			@Valid @RequestBody UpdateParticipantRolesRequest request) {

		UUID requesterUserId = UUID.fromString(jwt.getSubject());
		UpdateParticipantRolesResult result = updateParticipantRolesUseCase.execute(
				UpdateParticipantRolesWebMapper.toCommand(request, chatroomId, participantId, requesterUserId));

		return ApiResponses.ok(ApiResponseMessages.PARTICIPANT_ROLES_UPDATED_SUCCESS,
				UpdateParticipantRolesWebMapper.toResponse(result));
	}

	@GetMapping(ApiRoutes.API_CHAT_ROOM_ROLES)
	public ResponseEntity<ApiResponse<ChatRolesResponse>> getChatRoles(@PathVariable UUID chatroomId) {
		GetChatRolesResult result = getChatRolesUseCase.execute(new GetChatRolesCommand(new ChatRoomId(chatroomId)));

		return ApiResponses.ok(ApiResponseMessages.CHAT_ROLES_RETRIEVED_SUCCESS, ChatRoleWebMapper.toResponse(result));
	}

	@GetMapping(ApiRoutes.API_CHAT_ROOM_ROLE_BY_ID)
	public ResponseEntity<ApiResponse<ChatRoleResponse>> getChatRole(@PathVariable UUID chatroomId,
			@PathVariable UUID roleId) {
		GetChatRoleResult result = getChatRoleUseCase
				.execute(new GetChatRoleCommand(new ChatRoomId(chatroomId), new RoleId(roleId)));

		return ApiResponses.ok(ApiResponseMessages.CHAT_ROLE_RETRIEVED_SUCCESS,
				ChatRoleResponseMapper.toResponse(result.role()));
	}

	@PostMapping(ApiRoutes.API_CHAT_ROOM_ROLES)
	public ResponseEntity<ApiResponse<ChatRoleResponse>> createChatRole(@AuthenticationPrincipal Jwt jwt,
			@PathVariable UUID chatroomId, @Valid @RequestBody CreateChatRoleRequest request) {
		UUID requesterUserId = UUID.fromString(jwt.getSubject());
		CreateChatRoleResult result = createChatRoleUseCase
				.execute(ChatRoleWebMapper.toCommand(request, chatroomId, requesterUserId));

		return ApiResponses.created(ApiResponseMessages.CHAT_ROLE_CREATED_SUCCESS,
				ChatRoleResponseMapper.toResponse(result.role()));
	}

	@PatchMapping(ApiRoutes.API_CHAT_ROOM_ROLE_BY_ID)
	public ResponseEntity<ApiResponse<ChatRoleResponse>> updateChatRole(@AuthenticationPrincipal Jwt jwt,
			@PathVariable UUID chatroomId, @PathVariable UUID roleId,
			@Valid @RequestBody UpdateChatRoleRequest request) {
		UUID requesterUserId = UUID.fromString(jwt.getSubject());
		UpdateChatRoleResult result = updateChatRoleUseCase
				.execute(ChatRoleWebMapper.toCommand(request, chatroomId, roleId, requesterUserId));

		return ApiResponses.ok(ApiResponseMessages.CHAT_ROLE_UPDATED_SUCCESS,
				ChatRoleResponseMapper.toResponse(result.role()));
	}

	@DeleteMapping(ApiRoutes.API_CHAT_ROOM_ROLE_BY_ID)
	public ResponseEntity<ApiResponse<Void>> deleteChatRole(@AuthenticationPrincipal Jwt jwt,
			@PathVariable UUID chatroomId, @PathVariable UUID roleId,
			@RequestParam(required = false) UUID replacementRoleId) {
		UUID requesterUserId = UUID.fromString(jwt.getSubject());
		deleteChatRoleUseCase
				.execute(ChatRoleWebMapper.toCommand(chatroomId, roleId, requesterUserId, replacementRoleId));

		return ResponseEntity.ok(ApiResponse.success(ApiResponseMessages.CHAT_ROLE_DELETED_SUCCESS));
	}

	@GetMapping(ApiRoutes.API_CHAT_PERMISSIONS)
	public ResponseEntity<ApiResponse<ChatPermissionsResponse>> getAvailableChatPermissions() {
		GetAvailableChatPermissionsResult result = getAvailableChatPermissionsUseCase
				.execute(new GetAvailableChatPermissionsCommand());

		return ApiResponses.ok(ApiResponseMessages.CHAT_PERMISSIONS_RETRIEVED_SUCCESS,
				ChatRoleWebMapper.toResponse(result));
	}
}
