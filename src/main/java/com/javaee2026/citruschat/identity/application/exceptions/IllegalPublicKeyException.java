package com.javaee2026.citruschat.identity.application.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class IllegalPublicKeyException extends BusinessException {

	public IllegalPublicKeyException() {
		super(ErrorCode.ILLEGAL_PUBLIC_KEY, ErrorMessages.EMAIL_ALREADY_IN_USE);
	}
}
