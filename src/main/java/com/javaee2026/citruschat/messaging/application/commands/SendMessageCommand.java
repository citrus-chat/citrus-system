package com.javaee2026.citruschat.messaging.application.commands;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

public record SendMessageCommand(UserId senderUserId, MessageId messageId, ChatRoomId chatRoomId,
		DeviceId senderDeviceId, MessageId replyMessageId, Integer keyVersion, String iv, String ciphertext) {
}
