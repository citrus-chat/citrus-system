package com.javaee2026.citruschat.messaging.infrastructure.web.controller;

import com.javaee2026.citruschat.messaging.application.usecases.GetChatRoomMessagesUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.GetCurrentUserChatRoomsUseCase;
//import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRoomSummaryResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatQueryController {

	private final GetCurrentUserChatRoomsUseCase getCurrentUserChatRoomsUseCase;
	private final GetChatRoomMessagesUseCase getChatRoomMessagesUseCase;

	public ChatQueryController(GetCurrentUserChatRoomsUseCase getCurrentUserChatRoomsUseCase,
			GetChatRoomMessagesUseCase getChatRoomMessagesUseCase) {
		this.getCurrentUserChatRoomsUseCase = getCurrentUserChatRoomsUseCase;
		this.getChatRoomMessagesUseCase = getChatRoomMessagesUseCase;
	}

	// @GetMapping(ApiRoutes.API_CHAT_ROOMS_ME)
	// public ResponseEntity<ApiResponse<List<ChatRoomSummaryResponse>>>
	// getMyChatRooms(Authentication authentication) {
	// UUID userId = UUID.fromString(authentication.getName());

	// List<ChatRoomSummaryResponse> response =
	// getCurrentUserChatRoomsUseCase.execute(userId).stream()
	// .map(ChatQueryWebMapper::toChatRoomSummaryResponse).toList();

	// return ApiResponses.ok("Chat rooms retrieved successfully", response);
	// }

	// @GetMapping(ApiRoutes.API_CHAT_ROOM_MESSAGES)
	// public ResponseEntity<ApiResponse<List<ChatMessageResponse>>>
	// getChatRoomMessages(@PathVariable UUID chatRoomId,
	// @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue =
	// "50") int size,
	// Authentication authentication) {
	// UUID userId = UUID.fromString(authentication.getName());
	//
	// List<ChatMessageResponse> response =
	// getChatRoomMessagesUseCase.execute(chatRoomId, userId, page, size).stream()
	// .map(ChatQueryWebMapper::toChatMessageResponse).toList();
	//
	// return ApiResponses.ok("Chat room messages retrieved successfully",
	// response);
	// }
}
