package com.javaee2026.citruschat.shared.domain.valueobjects;

import com.javaee2026.citruschat.messaging.domain.exceptions.InvalidMessageException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;

import java.util.UUID;

public class MessageId {

	private final UUID value;

	public MessageId(UUID value) {
		System.out.println("Creando messageID: " + value);
		if (value == null) {
			throw new InvalidMessageException(ErrorMessages.MESSAGE_ID_CANNOT_BE_NULL);
		}
		this.value = value;
	}

	public static MessageId newId() {
		return new MessageId(UUID.randomUUID());
	}

	public UUID value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MessageId messageId))
			return false;
		return value.equals(messageId.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
