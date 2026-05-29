package com.javaee2026.citruschat.shared.domain.valueobjects;

import com.javaee2026.citruschat.identity.domain.exceptions.InvalidUserException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;

import java.util.UUID;

public final class UserId {

	private final UUID value;

	public UserId(UUID value) {
		if (value == null) {
			throw new InvalidUserException(ErrorMessages.USER_ID_CANNOT_BE_NULL);
		}
		this.value = value;
	}

	public static UserId newId() {
		return new UserId(UUID.randomUUID());
	}

	public UUID value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UserId userId))
			return false;
		return value.equals(userId.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
