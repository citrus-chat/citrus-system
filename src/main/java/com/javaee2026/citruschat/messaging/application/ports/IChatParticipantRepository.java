package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IChatParticipantRepository {
	boolean existsActiveByChatRoomIdAndParticipantId(UUID chatRoomId, UUID participantId);

	boolean existsChatParticipantByChatRoomIdAndUserId(UUID chatRoomId, UUID userId);

	Optional<ChatParticipant> findActiveByChatRoomIdAndParticipantId(ChatRoomId chatRoomId,
			ParticipantId participantId);

	Optional<ChatParticipant> findActiveByChatRoomIdAndUserId(ChatRoomId chatRoomId, UserId userId);

	boolean isRoleAssignedToAnyParticipant(ChatRoomId chatRoomId, RoleId roleId);

	void replaceRoleForParticipants(ChatRoomId chatRoomId, RoleId oldRoleId, RoleId replacementRoleId);

	boolean existsActiveParticipantWithAnyRole(ChatRoomId chatRoomId, List<RoleId> roleIds);

	long countActiveParticipantsUsingRole(ChatRoomId chatRoomId, RoleId roleId);

	void replaceParticipantRoles(ChatRoomId chatRoomId, ParticipantId participantId, List<RoleId> roleIds);
}
