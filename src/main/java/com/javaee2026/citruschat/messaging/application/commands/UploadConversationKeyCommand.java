package com.javaee2026.citruschat.messaging.application.commands;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

public record UploadConversationKeyCommand(ChatRoomId conversationId, UserId targetUserId, DeviceId targetDeviceId,
		Integer keyVersion, String ciphertext, String iv) {
}
