package com.javaee2026.citruschat.messaging.domain.model;

import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatParticipant {

	@EqualsAndHashCode.Include
	private final ParticipantId id;

	private final ChatRoomId chatRoomId;
	private final UserId userId;
	private final List<RoleId> roles;

	private final Instant joinedAt;
	private final Instant leftAt;
	private final MessageId lastReadMessageId;

	private ChatParticipant(ParticipantId id, ChatRoomId chatRoomId, UserId userId, List<RoleId> roles,
			Instant joinedAt, Instant leftAt, MessageId lastReadMessageId) {
		this.id = requireNonNull(id, ErrorMessages.PARTICIPANT_ID_CANNOT_BE_NULL);
		this.chatRoomId = requireNonNull(chatRoomId, ErrorMessages.CHATROOM_ID_CANNOT_BE_NULL);
		this.userId = requireNonNull(userId, ErrorMessages.USER_ID_CANNOT_BE_NULL);
		this.roles = requireNonNull(roles, ErrorMessages.ROLES_CANNOT_BE_NULL);
		this.joinedAt = requireNonNull(joinedAt, "joinedAt cannot be null");
		this.leftAt = leftAt;
		this.lastReadMessageId = lastReadMessageId;
	}

	public static ChatParticipant createNew(ChatRoomId chatRoomId, UserId userId, List<RoleId> roles,
			Instant joinedAt) {
		return new ChatParticipant(ParticipantId.newId(), chatRoomId, userId, roles, joinedAt, null, null);
	}

	public static ChatParticipant reconstitute(ParticipantId id, ChatRoomId chatRoomId, UserId userId,
			List<RoleId> roles, Instant joinedAt, Instant leftAt, MessageId lastReadMessageId) {
		return new ChatParticipant(id, chatRoomId, userId, roles, joinedAt, leftAt, lastReadMessageId);
	}

}
