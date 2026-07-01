package com.javaee2026.citruschat.messaging.domain.policy.permissions;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoleDefault;

public final class ChatRoleDefaultPriority {

	private ChatRoleDefaultPriority() {
	}

	public static int priority(ChatRoleDefault role) {
		return switch (role) {
			case OWNER -> 100;
			case ADMIN -> 80;
			case MEMBER -> 10;
		};
	}
}
