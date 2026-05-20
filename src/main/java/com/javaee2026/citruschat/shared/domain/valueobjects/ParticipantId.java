package com.javaee2026.citruschat.shared.domain.valueobjects;

import com.javaee2026.citruschat.messaging.domain.exceptions.InvalidParticipantException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;

import java.util.UUID;

public class ParticipantId {

	private final UUID value;

	public ParticipantId(UUID value) {
		if (value == null) {
			throw new InvalidParticipantException(ErrorMessages.PARTICIPANT_ID_CANNOT_BE_NULL);
		}
		this.value = value;
	}

	public static ParticipantId newId() {
		return new ParticipantId(UUID.randomUUID());
	}

	public UUID value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ParticipantId participantId))
			return false;
		return value.equals(participantId.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
