package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.commands.UploadConversationKeyCommand;
import com.javaee2026.citruschat.messaging.application.results.UploadConversationKeyResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.UploadConversationKeyRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.UploadConversationKeyResponse;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

public final class UploadConversationKeyWebMapper {

	private UploadConversationKeyWebMapper() {
	}

	public static UploadConversationKeyCommand toCommand(UploadConversationKeyRequest request) {

		return new UploadConversationKeyCommand(new ChatRoomId(request.conversationId()),
				new UserId(request.targetUserId()), new DeviceId(request.targetDeviceId()),
				new DeviceId(request.senderDeviceId()), request.keyVersion(), request.ciphertext(), request.iv());
	}

	public static UploadConversationKeyResponse toResponse(UploadConversationKeyResult result) {

		return new UploadConversationKeyResponse(result.id());
	}
}
