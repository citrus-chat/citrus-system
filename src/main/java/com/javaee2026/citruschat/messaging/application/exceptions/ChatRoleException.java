package com.javaee2026.citruschat.messaging.application.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class ChatRoleException extends BusinessException {

	public ChatRoleException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}
