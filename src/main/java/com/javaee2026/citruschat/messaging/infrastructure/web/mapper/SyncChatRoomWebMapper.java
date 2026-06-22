package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.commands.SyncChatRoomCommand;
import com.javaee2026.citruschat.messaging.application.results.SyncChatRoomResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.SyncChatRoomRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRoomResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.SyncChatRoomResponse;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

import java.util.List;

public final class SyncChatRoomWebMapper {
	private SyncChatRoomWebMapper() {
	}

	public static SyncChatRoomCommand toCommand(SyncChatRoomRequest request) {
		return new SyncChatRoomCommand(new DeviceId(request.deviceId()));
	}

	public static SyncChatRoomResponse toResponse(SyncChatRoomResult result) {

		List<ChatRoomResponse> chatRooms = result.chatRooms().stream().map(ChatRoomResponseMapper::toResponse).toList();

		return new SyncChatRoomResponse(chatRooms);
	}
}
