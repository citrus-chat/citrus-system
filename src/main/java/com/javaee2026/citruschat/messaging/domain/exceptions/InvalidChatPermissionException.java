package com.javaee2026.citruschat.messaging.domain.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class InvalidChatPermissionException extends BusinessException {
	public InvalidChatPermissionException(String message) {
		super(ErrorCode.INVALID_PERMISSION, message);
	}
}
