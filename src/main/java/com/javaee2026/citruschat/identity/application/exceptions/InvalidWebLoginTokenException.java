package com.javaee2026.citruschat.identity.application.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class InvalidWebLoginTokenException extends BusinessException {

	public InvalidWebLoginTokenException() {
		super(ErrorCode.INVALID_WEB_LOGIN_TOKEN, ErrorMessages.INVALID_WEB_LOGIN_TOKEN);
	}
}
