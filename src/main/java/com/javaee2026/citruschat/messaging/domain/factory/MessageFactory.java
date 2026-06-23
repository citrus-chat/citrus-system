package com.javaee2026.citruschat.messaging.domain.factory;

import com.javaee2026.citruschat.messaging.domain.model.Message;
import com.javaee2026.citruschat.messaging.domain.valueobjects.EncryptedContent;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MessageFactory {

	public Message createNew(MessageId messageId, ChatRoomId chatRoomId, UserId senderUserId, DeviceId senderDeviceId,
			MessageId replyToMessageId, EncryptedContent content) {
		return new Message(messageId, chatRoomId, senderUserId, senderDeviceId, replyToMessageId, content,
				Instant.now(), null, null);
	}

	public Message reconstitute(MessageId id, ChatRoomId chatRoomId, UserId senderUserId, DeviceId senderDeviceId,
			MessageId replyToMessageId, EncryptedContent content, Instant createdAt, Instant editedAt,
			Instant deletedAt) {
		return new Message(id, chatRoomId, senderUserId, senderDeviceId, replyToMessageId, content, createdAt, editedAt,
				deletedAt);
	}
}
