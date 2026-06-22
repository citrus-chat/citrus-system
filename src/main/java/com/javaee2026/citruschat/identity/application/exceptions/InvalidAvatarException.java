package com.javaee2026.citruschat.identity.application.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class InvalidAvatarException extends BusinessException {

	public InvalidAvatarException(String message) {
		super(ErrorCode.INVALID_AVATAR, message);
	}
}
