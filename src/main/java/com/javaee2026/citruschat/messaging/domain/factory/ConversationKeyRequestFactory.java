package com.javaee2026.citruschat.messaging.domain.factory;

import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyRequest;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.time.Instant;

public final class ConversationKeyRequestFactory {

	private ConversationKeyRequestFactory() {
	}

	public static ConversationKeyRequest create(ChatRoomId conversationId, UserId targetUserId, DeviceId targetDeviceId,
			String targetPublicKey) {
		return new ConversationKeyRequest(null, conversationId, targetUserId, targetDeviceId, targetPublicKey,
				Instant.now());
	}

}
