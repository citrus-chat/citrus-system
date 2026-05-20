package com.javaee2026.citruschat.messaging.domain.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class InvalidDeviceException extends BusinessException {
	public InvalidDeviceException(String message) {
		super(ErrorCode.INVALID_DEVICE, message);
	}
}
