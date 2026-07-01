package com.javaee2026.citruschat.identity.application.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class UsedWebLoginTokenException extends BusinessException {

	public UsedWebLoginTokenException() {
		super(ErrorCode.USED_WEB_LOGIN_TOKEN, ErrorMessages.USED_WEB_LOGIN_TOKEN);
	}
}
