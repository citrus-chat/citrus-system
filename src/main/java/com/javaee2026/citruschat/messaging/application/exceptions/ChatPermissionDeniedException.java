package com.javaee2026.citruschat.messaging.application.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class ChatPermissionDeniedException extends BusinessException {

	public ChatPermissionDeniedException() {
		super(ErrorCode.CHAT_PERMISSION_DENIED, "You do not have permission to perform this action.");
	}
}
