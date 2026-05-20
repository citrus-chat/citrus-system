package com.javaee2026.citruschat.messaging.domain.model;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.exceptions.InvalidMessageException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatRoom {

	@EqualsAndHashCode.Include
	private final ChatRoomId id;

	private final ChatRoomType type;
	private final String name;
	private final UserId createdBy;
	private final List<ChatParticipant> participants;
	private final Map<String, ChatRole> roles;

	private final Instant createdAt;
	private Instant updatedAt;
	private Instant deletedAt;

	public ChatRoom(ChatRoomId id, ChatRoomType type, String name, UserId createdBy, Instant createdAt) {
		this(id, type, name, createdBy, null, null, createdAt, null, null);
	}

	public ChatRoom(ChatRoomId id, ChatRoomType type, String name, UserId createdBy, List<ChatParticipant> participants,
			Map<String, ChatRole> roles, Instant createdAt, Instant updatedAt, Instant deletedAt) {
		this.id = requireNonNull(id, ErrorMessages.CHATROOM_ID_CANNOT_BE_NULL);
		this.type = requireNonNull(type, "ChatRoom type cannot be null");
		this.name = requireNonNull(name, "ChatRoom name cannot be null");
		this.createdBy = requireNonNull(createdBy, ErrorMessages.USER_ID_CANNOT_BE_NULL);
		this.participants = participants;
		this.roles = roles;
		this.createdAt = requireNonNull(createdAt, "Created at cannot be null");
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
	}

	public void initRoles() {
		if (!this.roles.isEmpty()) {
			return;
		}

		roles.put("OWNER", ChatRole.owner(id));
		roles.put("MEMBER", ChatRole.member(id));
		roles.put("ADMIN", ChatRole.admin(id));
	}

	public void touch() {
		if (isDeleted()) {
			throw new InvalidMessageException(ErrorMessages.CHATROOM_CANNOT_BE_EDITED);
		}

		this.updatedAt = Instant.now();
	}

	public void delete() {
		if (isDeleted()) {
			throw new InvalidMessageException(ErrorMessages.CHATROOM_ALREADY_DELETED);
		}

		this.deletedAt = Instant.now();
	}

	public void initParticipants(UserId creatorId, List<UserId> participantIds) {
		ChatRole ownerRole = this.roles.get("OWNER");
		ChatRole memberRole = this.roles.get("MEMBER");

		if (ownerRole == null || memberRole == null) {
			throw new InvalidMessageException(ErrorMessages.CHATROOM_DOES_NOT_HAVE_ROLES);
		}

		addOwner(creatorId, ownerRole);

		for (UserId userId : participantIds) {
			addMember(userId, memberRole);
		}
	}

	public void addOwner(UserId userId, ChatRole ownerRole) {
		List<RoleId> ownerRoles = new ArrayList<>();
		ownerRoles.add(ownerRole.getId());

		ChatParticipant owner = ChatParticipant.createNew(id, userId, ownerRoles, Instant.now());
		participants.add(owner);
	}

	public void addMember(UserId userId, ChatRole memberRole) {
		List<RoleId> memberRoles = new ArrayList<>();
		memberRoles.add(memberRole.getId());

		ChatParticipant member = ChatParticipant.createNew(id, userId, memberRoles, Instant.now());
		participants.add(member);
	}

	public boolean isDeleted() {
		return deletedAt != null;
	}
}
