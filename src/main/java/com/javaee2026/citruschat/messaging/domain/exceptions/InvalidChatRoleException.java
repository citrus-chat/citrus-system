package com.javaee2026.citruschat.messaging.domain.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class InvalidChatRoleException extends BusinessException {
	public InvalidChatRoleException(String message) {
		super(ErrorCode.INVALID_CHATROLE, message);
	}
}
