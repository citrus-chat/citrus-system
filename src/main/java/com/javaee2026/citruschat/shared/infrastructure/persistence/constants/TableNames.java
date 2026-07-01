package com.javaee2026.citruschat.shared.infrastructure.persistence.constants;

public final class TableNames {

	private TableNames() {
	}

	public static final class Messaging {
		public static final String MESSAGES = "messages";
		public static final String CHAT_ROOMS = "chat_rooms";
		public static final String CONVERSATION_KEY_DISTRIBUTIONS = "conversation_key_distributions";
	};

	public static final class Identity {
		public static final String USERS = "users";
		public static final String USER_DEVICES = "user_devices";
		public static final String POSITIONS = "positions";
		public static final String USER_ORGANIZATION = "user_organization";
		public static final String USER_PROFILES = "user_profiles";
		public static final String WEB_LOGIN_TOKENS = "web_login_tokens";
	}
}
