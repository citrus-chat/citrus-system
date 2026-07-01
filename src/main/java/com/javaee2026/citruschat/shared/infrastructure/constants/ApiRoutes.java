package com.javaee2026.citruschat.shared.infrastructure.constants;

public final class ApiRoutes {

	private ApiRoutes() {
	}

	// ===================== BASE API Routes ====================
	private static final String API_BASE = "/api/v1"; // => /api/v1
	private static final String API_ADMIN_BASE = API_BASE + "/admin"; // => /api/v1/admin
	private static final String API_AUTH_BASE = API_BASE + "/auth"; // => /api/v1/auth
	private static final String API_WEB_LOGIN_TOKENS_BASE = API_BASE + "/web-login-tokens";
	private static final String API_MESSAGES_BASE = API_BASE + "/messages"; // => /api/v1/auth
	private static final String API_CHATROOM_BASE = API_BASE + "/chatroom";
	private static final String API_USER_BASE = API_BASE + "/users";
	private static final String API_DEVICE_BASE = API_BASE + "/devices";
	// ===================== END BASE API Routes ====================

	// ===================== ADMIN API Routes ====================
	public static final String API_ADMIN_USERS = API_ADMIN_BASE + "/users"; // => /api/v1/admin/users
	public static final String API_ADMIN_ACCESS = API_ADMIN_BASE + "/access"; // => /api/v1/admin/access
	// ===================== END ADMIN API Routes ====================

	// ===================== AUTH API Routes ====================
	public static final String API_AUTH_VALIDATE_ACCOUNT = API_AUTH_BASE + "/validate-account"; // =>
	// /api/v1/auth/validate-account
	public static final String API_AUTH_LOGIN = API_AUTH_BASE + "/login"; // => /api/v1/auth/login
	public static final String API_AUTH_LOGOUT = API_AUTH_BASE + "/logout"; // => /api/v1/auth/logout
	public static final String API_AUTH_ME = API_AUTH_BASE + "/me"; // => /api/v1/auth/me
	public static final String API_AUTH_DEVICES = API_AUTH_BASE + "/devices";
	// ===================== END API Routes ====================

	// ===================== WEB LOGIN TOKEN API Routes ====================
	public static final String API_WEB_LOGIN_TOKENS = API_WEB_LOGIN_TOKENS_BASE;
	public static final String API_WEB_LOGIN_TOKENS_CONFIRM = API_WEB_LOGIN_TOKENS_BASE + "/confirm";
	// ===================== END WEB LOGIN TOKEN API Routes ====================

	// ===================== DOCS API Routes ====================
	public static final String API_DOCS_SWAGGER_BASE = "/swagger-ui.html";
	public static final String API_DOCS_SWAGGER = "/swagger-ui/**";
	public static final String API_DOCS_V3_BASE = "/v3/api-docs";
	public static final String API_DOCS_V3 = "/v3/api-docs/**";
	// ===================== END API Routes ====================

	// ===================== MESSAGES API Routes ====================
	public static final String API_MESSAGES = API_MESSAGES_BASE; // => /api/v1/messages
	public static final String API_MESSAGES_SEND = "/api/v1/messages/send"; // => /api/v1/messages
	// ===================== END API Routes ====================

	// ===================== CHAT_ROOM API ROUTES ====================
	public static final String API_CHAT_ROOMS = API_CHATROOM_BASE;
	public static final String API_CHAT_ROOMS_CREATE = API_CHATROOM_BASE + "/create"; // => /api/v1/chatroom/create
	public static final String API_CHAT_ROOMS_SYNC = API_CHATROOM_BASE + "/sync"; // => /api/v1/chatroom/sync
	public static final String API_CHAT_ROOM_MESSAGES_SYNC = API_CHATROOM_BASE + "/{chatroomId}/sync/messages"; // =>
	public static final String API_CHAT_ROOM_CONVERSATION_KEY = API_CHATROOM_BASE + "/conversation-keys"; // =>
	public static final String API_CHAT_ROOM_PARTICIPANT_PERMISSION = API_CHATROOM_BASE
			+ "/{chatroomId}/participant/{participantId}/permission"; // =>
																		// /api/v1/chatroom/{chatroomId}/participant/{participantId}/permission

	public static final String API_CHAT_ROOM_UPDATE_NAME = API_CHATROOM_BASE + "/{chatroomId}/name";
	public static final String API_CHAT_ROOM_PENDING_CONVERSATION_KEY = API_CHAT_ROOM_CONVERSATION_KEY + "/pending";
	// /api/v1/chatroom/{chatroomId}/participant/{participantId}/permission
	public static final String API_CHAT_ROOM_PARTICIPANT_ROLES = API_CHATROOM_BASE
			+ "/{chatroomId}/participant/{participantId}/roles"; // =>
	// /api/v1/chatroom/{chatroomId}/participant/{participantId}/roles
	public static final String API_CHAT_ROOM_ROLES = API_CHATROOM_BASE + "/{chatroomId}/roles";
	public static final String API_CHAT_ROOM_ROLE_BY_ID = API_CHATROOM_BASE + "/{chatroomId}/roles/{roleId}";
	public static final String API_CHAT_PERMISSIONS = API_CHATROOM_BASE + "/permissions";
	// ===================== END API ROUTES ====================

	// ===================== USER API ROUTES =======================
	public static final String API_USER_ME = API_USER_BASE + "/me"; // => /api/v1/users/me
	public static final String API_USER_BY_ID = API_USER_BASE + "/{id}"; // => /api/v1/users/{id}
	public static final String API_USERS_SEARCH = API_USER_BASE; // => /api/v1/users
	public static final String API_USERS_ORG = API_USER_BASE + "/org";
	public static final String API_USER_UPDATE = API_USER_BASE + "/{id}"; // => /api/v1/users/{id}
	public static final String API_USER_DELETE = API_USER_BASE + "/{id}"; // => /api/v1/users/{id}
	public static final String API_USER_CREATE = API_USER_BASE; // => /api/v1/users
	public static final String API_USER_KEYS = API_USER_BASE + "/{userId}/keys"; // => /api/v1/users/{id}/keys
	public static final String API_USER_ME_AVATAR = API_USER_BASE + "/me/avatar";
	public static final String API_USER_AVATAR_IMAGE = API_USER_BASE + "/avatars/{filename}";
	public static final String API_USER_AVATAR_IMAGE_BASE = API_USER_BASE + "/avatars";
	public static final String API_USER_AVATAR_IMAGE_PATTERN = API_USER_AVATAR_IMAGE_BASE + "/**";
	public static final String API_USER_ME_PROFILE = API_USER_BASE + "/me/profile";
	public static final String API_USER_PUBLIC_PROFILE = API_USER_BASE + "/{userId}/profile"; // =>/api/v1/users/{userId}/
	// ===================== END API Routes ====================

	// ===================== DEVICE API ROUTES =======================
	public static final String API_DEVICE_KEYS = API_DEVICE_BASE + "/{deviceId}/keys";
	public static final String API_DEVICE_REQUEST_KEYS = API_DEVICE_BASE + "/request-key";
	// ===================== END API ROUTES ====================

	// ===================== WEBSOCKET ROUTES ====================
	public static final String WS_ENDPOINT = "/ws";
	public static final String WS_ENDPOINT_PATTERN = WS_ENDPOINT + "/**";

	public static final String WS_TOPIC_BASE = "/topic";
	public static final String WS_QUEUE_BASE = "/queue";
	public static final String WS_APP_PREFIX = "/app";
	public static final String WS_USER_PREFIX = "/user";

	public static final String WS_CHATROOM_TOPIC_PREFIX = WS_TOPIC_BASE + "/chatrooms/";
	// ===================== END WEBSOCKET ROUTES ====================
}
