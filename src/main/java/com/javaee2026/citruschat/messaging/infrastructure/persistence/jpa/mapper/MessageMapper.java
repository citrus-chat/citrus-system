package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.messaging.domain.factory.MessageFactory;
import com.javaee2026.citruschat.messaging.domain.model.Message;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.MessageJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

public final class MessageMapper {

	private final MessageFactory messageFactory;

	public MessageMapper(MessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}

	public Message toDomain(MessageJpaEntity entity) {
		return messageFactory.reconstitute(new MessageId(entity.getId()), new ChatRoomId(entity.getChatRoomId()),
				new UserId(entity.getSenderUserId()), new DeviceId(entity.getSenderDeviceId()),
				entity.getReplyToMessageId() != null ? new MessageId(entity.getReplyToMessageId()) : null,
				EncryptedContentJpaMapper.toDomain(entity.getContent()), entity.getCreatedAt(), entity.getEditedAt(),
				entity.getDeletedAt());
	}

	public static MessageJpaEntity toJpa(Message message) {
		MessageJpaEntity entity = new MessageJpaEntity();

		entity.setId(message.getId().value());
		entity.setChatRoomId(message.getChatRoomId().value());
		entity.setSenderUserId(message.getSenderUserId().value());
		entity.setSenderDeviceId(message.getSenderDeviceId().value());

		entity.setReplyToMessageId(
				message.getReplyToMessageId() != null ? message.getReplyToMessageId().value() : null);

		entity.setContent(EncryptedContentJpaMapper.toEmbeddable(message.getContent())); // toEmbeddable == toJpa in
																							// other classes.

		entity.setCreatedAt(message.getCreatedAt());
		entity.setEditedAt(message.getEditedAt());
		entity.setDeletedAt(message.getDeletedAt());

		return entity;
	}
}
