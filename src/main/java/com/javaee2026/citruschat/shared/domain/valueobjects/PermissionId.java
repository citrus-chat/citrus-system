package com.javaee2026.citruschat.shared.domain.valueobjects;

import com.javaee2026.citruschat.messaging.domain.exceptions.InvalidChatPermissionException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;

import java.util.UUID;

public class PermissionId {

	private final UUID value;

	public PermissionId(UUID value) {
		if (value == null) {
			throw new InvalidChatPermissionException(ErrorMessages.PERMISSION_ID_CANNOT_BE_NULL);
		}
		this.value = value;
	}

	public static PermissionId newId() {
		return new PermissionId(UUID.randomUUID());
	}

	public UUID value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof PermissionId permissionId))
			return false;
		return value.equals(permissionId.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
