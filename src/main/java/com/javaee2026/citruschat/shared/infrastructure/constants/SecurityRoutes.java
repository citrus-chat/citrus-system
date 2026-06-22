package com.javaee2026.citruschat.shared.infrastructure.constants;

public final class SecurityRoutes {

	private SecurityRoutes() {
	}

	public static final String[] PUBLIC_HTTP_ROUTES = {ApiRoutes.WS_ENDPOINT_PATTERN, ApiRoutes.API_AUTH_LOGIN,
			ApiRoutes.API_AUTH_VALIDATE_ACCOUNT, ApiRoutes.API_DOCS_SWAGGER_BASE, ApiRoutes.API_DOCS_SWAGGER,
			ApiRoutes.API_DOCS_V3_BASE, ApiRoutes.API_DOCS_V3, ApiRoutes.API_USER_AVATAR_IMAGE_PATTERN,};

	public static final String[] ADMIN_ROUTES = {"/api/v1/admin/**"};
}
