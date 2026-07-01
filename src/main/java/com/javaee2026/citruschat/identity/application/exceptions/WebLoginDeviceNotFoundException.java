package com.javaee2026.citruschat.identity.application.exceptions;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class WebLoginDeviceNotFoundException extends BusinessException {

	public WebLoginDeviceNotFoundException() {
		super(ErrorCode.WEB_LOGIN_DEVICE_NOT_FOUND, ErrorMessages.WEB_LOGIN_DEVICE_NOT_FOUND);
	}
}
