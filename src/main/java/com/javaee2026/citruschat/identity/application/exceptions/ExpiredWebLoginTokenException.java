package com.javaee2026.citruschat.identity.application.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class ExpiredWebLoginTokenException extends BusinessException {

	public ExpiredWebLoginTokenException() {
		super(ErrorCode.EXPIRED_WEB_LOGIN_TOKEN, ErrorMessages.EXPIRED_WEB_LOGIN_TOKEN);
	}
}
