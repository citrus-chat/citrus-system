package com.javaee2026.citruschat.shared.domain.valueobjects;

import com.javaee2026.citruschat.messaging.domain.exceptions.InvalidChatRoleException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;

import java.util.UUID;

public class RoleId {
	private final UUID value;

	public RoleId(UUID value) {
		if (value == null) {
			throw new InvalidChatRoleException(ErrorMessages.ROLE_ID_CANNOT_BE_NULL);
		}
		this.value = value;
	}

	public static RoleId newId() {
		return new RoleId(UUID.randomUUID());
	}

	public UUID value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RoleId roleId))
			return false;
		return value.equals(roleId.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
