package com.javaee2026.citruschat.shared.domain.valueobjects;

import com.javaee2026.citruschat.messaging.domain.exceptions.InvalidChatRoomException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;

import java.util.UUID;

public class ChatRoomId {
	private final UUID value;

	public ChatRoomId(UUID value) {
		if (value == null) {
			throw new InvalidChatRoomException(ErrorMessages.CHATROOM_ID_CANNOT_BE_NULL);
		}
		this.value = value;
	}

	public static ChatRoomId newId() {
		return new ChatRoomId(UUID.randomUUID());
	}

	public UUID value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ChatRoomId chatRoomId))
			return false;
		return value.equals(chatRoomId.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
