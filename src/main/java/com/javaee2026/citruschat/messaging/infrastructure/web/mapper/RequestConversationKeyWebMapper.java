package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.identity.domain.valueobjects.PublicKey;
import com.javaee2026.citruschat.messaging.application.commands.RequestConversationKeyCommand;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.RequestConversationKeyRequest;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

public final class RequestConversationKeyWebMapper {

	private RequestConversationKeyWebMapper() {
	}

	public static RequestConversationKeyCommand toCommand(RequestConversationKeyRequest request) {
		return new RequestConversationKeyCommand(new ChatRoomId(request.conversationId()),
				new DeviceId(request.deviceId()), new PublicKey(request.publicKey()));
	}
}
