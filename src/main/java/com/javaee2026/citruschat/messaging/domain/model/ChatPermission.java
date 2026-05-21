package com.javaee2026.citruschat.messaging.domain.model;

import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatPermission {

	@EqualsAndHashCode.Include
	private final PermissionId id;

	private final String code;

	private final String description;

	public ChatPermission(PermissionId id, String code, String description) {
		this.id = requireNonNull(id, ErrorMessages.PERMISSION_ID_CANNOT_BE_NULL);
		this.code = requireNonNull(code, ErrorMessages.PERMISSION_CODE_CANNOT_BE_NULL);
		this.description = description;
	}
}
