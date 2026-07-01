package com.javaee2026.citruschat.identity.application.constants;

public final class WebLoginSocketContract {

	private WebLoginSocketContract() {
	}

	public static final String TOKEN_HEADER = "X-Web-Login-Token";
	public static final String CLIENT_USER_QUEUE = "/user/queue/web-login";
	public static final String SERVER_USER_QUEUE = "/queue/web-login";
}
