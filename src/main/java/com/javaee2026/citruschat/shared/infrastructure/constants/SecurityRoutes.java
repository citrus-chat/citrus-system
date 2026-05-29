package com.javaee2026.citruschat.shared.infrastructure.constants;

public final class SecurityRoutes {

	private SecurityRoutes() {
	}

	public static final String[] PUBLIC_HTTP_ROUTES = {ApiRoutes.WS_ENDPOINT_PATTERN, ApiRoutes.API_AUTH_LOGIN,
			ApiRoutes.API_AUTH_VALIDATE_ACCOUNT};

	public static final String[] ADMIN_ROUTES = {"/api/v1/admin/**"};
}
