package com.javaee2026.citruschat.messaging.domain.model;

import com.javaee2026.citruschat.messaging.domain.exceptions.InvalidMessageException;
import com.javaee2026.citruschat.messaging.domain.valueobjects.EncryptedContent;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message {

	@EqualsAndHashCode.Include
	private final MessageId id;

	private final ChatRoomId chatRoomId;
	private final UserId senderUserId;
	private final DeviceId senderDeviceId;
	private final MessageId replyToMessageId;

	private final EncryptedContent content;

	private final Instant createdAt;
	private Instant editedAt;
	private Instant deletedAt;

	public Message(MessageId id, ChatRoomId chatRoomId, UserId senderUserId, DeviceId senderDeviceId,
			MessageId replyToMessageId, EncryptedContent content, Instant createdAt) {
		this(id, chatRoomId, senderUserId, senderDeviceId, replyToMessageId, content, createdAt, null, null);
	}

	public Message(MessageId id, ChatRoomId chatRoomId, UserId senderUserId, DeviceId senderDeviceId,
			MessageId replyToMessageId, EncryptedContent content, Instant createdAt, Instant editedAt,
			Instant deletedAt) {
		this.id = requireNonNull(id, ErrorMessages.MESSAGE_ID_CANNOT_BE_NULL);
		this.chatRoomId = requireNonNull(chatRoomId, ErrorMessages.CHATROOM_ID_CANNOT_BE_NULL);
		this.senderUserId = requireNonNull(senderUserId, ErrorMessages.USER_ID_CANNOT_BE_NULL);
		this.senderDeviceId = requireNonNull(senderDeviceId, ErrorMessages.DEVICE_ID_CANNOT_BE_NULL);
		this.replyToMessageId = replyToMessageId; // This can be null
		this.content = requireNonNull(content, ErrorMessages.ENCRYPTED_CONTENT_CANNOT_BE_NULL);
		this.createdAt = requireNonNull(createdAt, ErrorMessages.CREATED_AT_CANNOT_BE_NULL);
		this.editedAt = editedAt;
		this.deletedAt = deletedAt;
	}

	public void markEdited() {
		if (isDeleted()) {
			throw new InvalidMessageException(ErrorMessages.MESSAGE_CANNOT_BE_EDITED);
		}

		this.editedAt = Instant.now();
	}

	public void delete() {
		if (isDeleted()) {
			throw new InvalidMessageException(ErrorMessages.MESSAGE_ALREADY_DELETED);
		}

		this.deletedAt = Instant.now();
	}

	public boolean isDeleted() {
		return deletedAt != null;
	}
}
