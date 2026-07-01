package com.javaee2026.citruschat.messaging.application.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class ChatParticipantRolesException extends BusinessException {

	public ChatParticipantRolesException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}
