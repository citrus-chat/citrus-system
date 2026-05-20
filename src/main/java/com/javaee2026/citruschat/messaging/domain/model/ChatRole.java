package com.javaee2026.citruschat.messaging.domain.model;

import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatRole {

	@EqualsAndHashCode.Include
	private final RoleId id;

	private final ChatRoomId chatRoomId;

	private final String name;
	private final Instant createdAt;

	private ChatRole(RoleId id, ChatRoomId chatRoomId, String name, Instant createdAt) {
		this.id = requireNonNull(id, ErrorMessages.ROLE_ID_CANNOT_BE_NULL);
		this.chatRoomId = chatRoomId; // This can be null ONLY IF IT'S A GLOBAL ROLE
		this.name = requireNonNull(name, "Name cannot be null");
		this.createdAt = requireNonNull(createdAt, "CreatedAt cannot be null");
	}

	public static ChatRole reconstitute(RoleId id, ChatRoomId chatRoomId, String name, Instant createdAt) {
		return new ChatRole(id, chatRoomId, name, createdAt);
	}

	public static ChatRole create(ChatRoomId chatRoomId, String name) {
		return new ChatRole(RoleId.newId(), chatRoomId, name, Instant.now());
	}

	public static ChatRole owner(ChatRoomId chatRoomId) {
		return new ChatRole(RoleId.newId(), chatRoomId, "OWNER", Instant.now());
	}

	public static ChatRole member(ChatRoomId chatRoomId) {
		return new ChatRole(RoleId.newId(), chatRoomId, "MEMBER", Instant.now());
	}

	public static ChatRole admin(ChatRoomId chatRoomId) {
		return new ChatRole(RoleId.newId(), chatRoomId, "ADMIN", Instant.now());
	}
}
