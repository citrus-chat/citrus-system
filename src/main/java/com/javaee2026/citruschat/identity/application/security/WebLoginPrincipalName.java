package com.javaee2026.citruschat.identity.application.security;

import java.util.UUID;

public final class WebLoginPrincipalName {

	private static final String PREFIX = "web-login:";

	private WebLoginPrincipalName() {
	}

	public static String fromDeviceId(UUID webDeviceId) {
		return PREFIX + webDeviceId;
	}
}
