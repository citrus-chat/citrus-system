package com.javaee2026.citruschat.shared.infrastructure.constants;

public final class ApiRoutes {

	private ApiRoutes() {
	}

	// ===================== BASE API Routes ====================
	private static final String API_BASE = "/api/v1"; // => /api/v1
	private static final String API_ADMIN_BASE = API_BASE + "/admin"; // => /api/v1/admin
	private static final String API_AUTH_BASE = API_BASE + "/auth"; // => /api/v1/auth
	private static final String API_MESSAGES_BASE = API_BASE + "/messages"; // => /api/v1/auth
	private static final String API_CHATROOM_BASE = API_BASE + "/chatroom";
	// ===================== END BASE API Routes ====================

	// ===================== ADMIN API Routes ====================
	public static final String API_ADMIN_USERS = API_ADMIN_BASE + "/users"; // => /api/v1/admin/users
	public static final String API_ADMIN_ACCESS = API_ADMIN_BASE + "/access"; // => /api/v1/admin/access
	// ===================== END ADMIN API Routes ====================

	// ===================== AUTH API Routes ====================
	public static final String API_AUTH_VALIDATE_ACCOUNT = API_AUTH_BASE + "/validate-account"; // =>
																								// /api/v1/auth/validate-account
	public static final String API_AUTH_LOGIN = API_AUTH_BASE + "/login"; // => /api/v1/auth/login
	public static final String API_AUTH_ME = API_AUTH_BASE + "/me"; // => /api/v1/auth/me
	// ===================== END API Routes ====================

	// ===================== MESSAGES API Routes ====================
	public static final String API_MESSAGES = API_MESSAGES_BASE; // => /api/v1/messages
	// ===================== END API Routes ====================

	// ===================== CHAT_ROOM API ROUTES ====================
	public static final String API_CHAT_ROOMS = API_CHATROOM_BASE;
	public static final String API_CHAT_ROOMS_CREATE = API_CHATROOM_BASE + "/create"; // => /api/v1/chatroom/create
	// ===================== END API ROUTES ====================
}
