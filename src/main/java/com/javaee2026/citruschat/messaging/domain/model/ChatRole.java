package com.javaee2026.citruschat.messaging.domain.model;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoleDefault;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatRole {

	@EqualsAndHashCode.Include
	private final RoleId id;

	private final ChatRoomId chatRoomId;
	private final Set<ChatPermission> rolePermissions;
	private final String name;
	private final Integer priority;

	private final Instant createdAt;

	private ChatRole(RoleId id, ChatRoomId chatRoomId, Set<ChatPermission> rolePermissions, String name,
			Integer priority, Instant createdAt) {
		this.id = requireNonNull(id, ErrorMessages.ROLE_ID_CANNOT_BE_NULL);
		this.chatRoomId = chatRoomId; // This can be null ONLY IF IT'S A GLOBAL ROLE
		this.rolePermissions = rolePermissions;
		this.name = requireNonNull(name, "Name cannot be null");
		this.priority = priority; // 0-100. Donde 100 es la 'jerarquía' máxima
		this.createdAt = requireNonNull(createdAt, "CreatedAt cannot be null");
	}

	public static ChatRole reconstitute(RoleId id, ChatRoomId chatRoomId, Set<ChatPermission> rolePermissions,
			String name, Integer priority, Instant createdAt) {
		return new ChatRole(id, chatRoomId, rolePermissions, name, priority, createdAt);
	}

	public static ChatRole create(ChatRoomId chatRoomId, Set<ChatPermission> rolePermissions, String name,
			Integer priority) {
		return new ChatRole(RoleId.newId(), chatRoomId, rolePermissions, name, priority, Instant.now());
	}

	public static ChatRole createDefault(ChatRoomId chatRoomId, ChatRoleDefault roleDefault,
			Set<ChatPermission> rolePermissions, Integer priority) {
		if (rolePermissions == null || rolePermissions.isEmpty()) {
			throw new IllegalArgumentException(roleDefault.toString() + " role must have at least one permission");
		}
		return new ChatRole(RoleId.newId(), chatRoomId, rolePermissions, roleDefault.toString(), priority,
				Instant.now());
	}

	public boolean hasPermission(String permission) {
		return rolePermissions.stream().anyMatch(chatPermission -> chatPermission.getCode().equals(permission));
	}
}
