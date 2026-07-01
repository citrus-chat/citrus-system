package com.javaee2026.citruschat.messaging.application.commands;

import com.javaee2026.citruschat.identity.domain.valueobjects.PublicKey;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

public record RequestConversationKeyCommand(ChatRoomId conversationId, DeviceId requesterDeviceId,
		PublicKey requesterPublicKey) {
}
