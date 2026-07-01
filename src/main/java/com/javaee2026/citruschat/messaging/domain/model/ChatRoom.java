package com.javaee2026.citruschat.messaging.domain.model;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoleDefault;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.exceptions.InvalidMessageException;
import com.javaee2026.citruschat.messaging.domain.exceptions.InvalidChatRoomException;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatRoleDefaultPriority;
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
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatRoom {

	@EqualsAndHashCode.Include
	private final ChatRoomId id;

	private final ChatRoomType type;
	private String name;
	private String avatarUrl;
	private final UserId createdBy;
	private final List<ChatParticipant> participants;
	private final Map<String, ChatRole> roles;

	private final Instant createdAt;
	private Instant updatedAt;
	private Instant deletedAt;

	public ChatRoom(ChatRoomId id, ChatRoomType type, String name, UserId createdBy, Instant createdAt) {
		this(id, type, name, null, createdBy, null, null, createdAt, null, null);
	}

	public ChatRoom(ChatRoomId id, ChatRoomType type, String name, String avatarUrl, UserId createdBy,
			List<ChatParticipant> participants, Map<String, ChatRole> roles, Instant createdAt, Instant updatedAt,
			Instant deletedAt) {
		this.id = requireNonNull(id, ErrorMessages.CHATROOM_ID_CANNOT_BE_NULL);
		this.type = requireNonNull(type, "ChatRoom type cannot be null");
		this.name = requireNonNull(name, "ChatRoom name cannot be null");
		this.avatarUrl = avatarUrl;
		this.createdBy = requireNonNull(createdBy, ErrorMessages.USER_ID_CANNOT_BE_NULL);
		this.participants = participants;
		this.roles = roles;
		this.createdAt = requireNonNull(createdAt, "Created at cannot be null");
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
	}

	public void initRoles(Map<ChatRoleDefault, Set<ChatPermission>> permissions) {
		if (!this.roles.isEmpty()) {
			throw new InvalidMessageException(ErrorMessages.CHATROOM_ROLES_ALREADY_INITIALIZED);
		}

		for (ChatRoleDefault roleDefault : ChatRoleDefault.values()) { // Por cada roleDefault que venga de la BD
			Set<ChatPermission> rolePermissions = permissions.get(roleDefault);

			String roleName = roleDefault.toString();
			Integer rolePriority = ChatRoleDefaultPriority.priority(roleDefault);

			roles.put(roleName, ChatRole.createDefault(id, roleDefault, rolePermissions, rolePriority));
		}
		touch();
	}

	public void touch() {
		if (isDeleted()) {
			throw new InvalidMessageException(ErrorMessages.CHATROOM_CANNOT_BE_EDITED);
		}

		this.updatedAt = Instant.now();
	}

	public void rename(String newName) {
		if (newName == null || newName.isBlank()) {
			throw new InvalidChatRoomException(ErrorMessages.CHATROOM_NAME_CANNOT_BE_EMPTY);
		}
		touch();
		this.name = newName;
	}

	public void changeAvatar(String newAvatar) {
		if (newAvatar == null || newAvatar.isBlank()) {
			throw new InvalidChatRoomException(ErrorMessages.CHATROOM_AVATAR_CANNOT_BE_EMPTY);
		}
		touch();
		this.avatarUrl = newAvatar;
	}

	public void delete() {
		if (isDeleted()) {
			throw new InvalidMessageException(ErrorMessages.CHATROOM_ALREADY_DELETED);
		}

		touch();
		this.deletedAt = Instant.now();
	}

	public void initParticipants(UserId creatorId, List<UserId> participantIds) {
		ChatRole ownerRole = this.roles.get(ChatRoleDefault.OWNER.toString());
		ChatRole memberRole = this.roles.get(ChatRoleDefault.MEMBER.toString());

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
		touch();
	}

	public void addMember(UserId userId, ChatRole memberRole) {
		List<RoleId> memberRoles = new ArrayList<>();
		memberRoles.add(memberRole.getId());

		ChatParticipant member = ChatParticipant.createNew(id, userId, memberRoles, Instant.now());
		participants.add(member);
		touch();
	}

	public ChatRole getRole(RoleId roleId) {
		return roles.values().stream().filter(role -> role.getId().equals(roleId)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleId.value()));
	}

	public boolean hasPermission(ChatParticipant participant, String permission) {
		return participant.getRoles().stream().map(this::getRole).anyMatch(role -> role.hasPermission(permission));
	}

	public boolean isDeleted() {
		return deletedAt != null;
	}
}
