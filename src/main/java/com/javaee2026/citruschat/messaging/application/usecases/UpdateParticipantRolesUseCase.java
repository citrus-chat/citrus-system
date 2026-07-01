package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.UpdateParticipantRolesCommand;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatParticipantRolesException;
import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.results.UpdateParticipantRolesResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UpdateParticipantRolesUseCase {

	private final IChatRoomRepository chatRoomRepository;
	private final IChatParticipantRepository chatParticipantRepository;
	private final IChatPermissionRepository chatPermissionRepository;
	private final ChatPermissionAuthorizationService permissionAuthorizationService;

	public UpdateParticipantRolesUseCase(IChatRoomRepository chatRoomRepository,
			IChatParticipantRepository chatParticipantRepository, IChatPermissionRepository chatPermissionRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatParticipantRepository = chatParticipantRepository;
		this.chatPermissionRepository = chatPermissionRepository;
		this.permissionAuthorizationService = permissionAuthorizationService;
	}

	@Transactional
	public UpdateParticipantRolesResult execute(UpdateParticipantRolesCommand command) {
		if (command == null) {
			throw new IllegalArgumentException("command cannot be null");
		}

		validateRequired(command);
		List<RoleId> roleIds = validateRoleIds(command.roleIds());

		ChatRoom chatRoom = chatRoomRepository.findById(command.chatRoomId()).orElseThrow(
				() -> new ChatParticipantRolesException(ErrorCode.CHATROOM_NOT_FOUND, "Chat room not found"));

		if (chatRoom.getType() != ChatRoomType.GROUP) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_CHATROOM,
					"Participant roles can only be updated in group chat rooms");
		}

		ChatParticipant targetParticipant = chatParticipantRepository
				.findActiveByChatRoomIdAndParticipantId(command.chatRoomId(), command.participantId())
				.orElseThrow(() -> new ChatParticipantRolesException(ErrorCode.CHAT_PARTICIPANT_NOT_FOUND,
						"Active participant not found"));

		ChatParticipant requesterParticipant = chatParticipantRepository
				.findActiveByChatRoomIdAndUserId(command.chatRoomId(), command.requesterUserId())
				.orElseThrow(() -> new ChatParticipantRolesException(ErrorCode.FORBIDDEN,
						"Requester is not an active participant of this chat room"));

		permissionAuthorizationService.requirePermissionOrCreator(chatRoom, command.requesterUserId(),
				ChatPermissionList.CAN_MODIFY_ROLE);

		List<ChatRole> requestedRoles = findRolesInChatRoom(chatRoom, roleIds);
		validateHierarchy(chatRoom, requesterParticipant, requestedRoles, command);
		if (!willHaveAdministrativeParticipant(chatRoom, targetParticipant.getId(), requestedRoles)) {
			throw new ChatParticipantRolesException(ErrorCode.CHAT_RULE_CONFLICT,
					"Cannot leave a group chat room without an administrative participant");
		}

		chatParticipantRepository.replaceParticipantRoles(command.chatRoomId(), command.participantId(), roleIds);

		Set<ChatPermission> permissions = chatPermissionRepository
				.findPermissionsByChatRoomAndParticipant(command.chatRoomId(), command.participantId());

		return new UpdateParticipantRolesResult(targetParticipant.getId(), targetParticipant.getChatRoomId(),
				targetParticipant.getUserId(), roleIds, permissions);
	}

	private void validateRequired(UpdateParticipantRolesCommand command) {
		if (command.chatRoomId() == null) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_CHATROOM, "chatRoomId cannot be null");
		}
		if (command.participantId() == null) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_PARTICIPANT, "participantId cannot be null");
		}
		if (command.requesterUserId() == null) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_USER, "requesterUserId cannot be null");
		}
	}

	private List<RoleId> validateRoleIds(List<RoleId> roleIds) {
		if (roleIds == null) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_CHATROLE, "roleIds cannot be null");
		}
		if (roleIds.isEmpty()) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_CHATROLE, "roleIds cannot be empty");
		}
		if (roleIds.stream().anyMatch(roleId -> roleId == null)) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_CHATROLE, "roleIds cannot contain null values");
		}

		List<RoleId> uniqueRoleIds = new LinkedHashSet<>(roleIds).stream().toList();
		if (uniqueRoleIds.size() != roleIds.size()) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_CHATROLE, "roleIds cannot contain duplicates");
		}

		return uniqueRoleIds;
	}

	private List<ChatRole> findRolesInChatRoom(ChatRoom chatRoom, List<RoleId> roleIds) {
		Map<RoleId, ChatRole> rolesById = chatRoom.getRoles().values().stream()
				.collect(Collectors.toMap(ChatRole::getId, Function.identity()));

		return roleIds.stream().map(roleId -> {
			ChatRole role = rolesById.get(roleId);
			if (role == null) {
				throw new ChatParticipantRolesException(ErrorCode.INVALID_CHATROLE,
						"Role does not exist in this chat room: " + roleId.value());
			}
			return role;
		}).toList();
	}

	private void validateHierarchy(ChatRoom chatRoom, ChatParticipant requesterParticipant,
			List<ChatRole> requestedRoles, UpdateParticipantRolesCommand command) {
		if (chatRoom.getCreatedBy().equals(command.requesterUserId())) {
			return;
		}

		int requesterMaxPriority = requesterParticipant.getRoles().stream().map(chatRoom::getRole)
				.mapToInt(ChatRole::getPriority).max().orElse(0);
		boolean assignsRoleAboveRequester = requestedRoles.stream()
				.anyMatch(role -> role.getPriority() > requesterMaxPriority);
		if (assignsRoleAboveRequester) {
			throw new ChatParticipantRolesException(ErrorCode.FORBIDDEN,
					"Requester cannot assign roles above their highest priority");
		}
	}

	private boolean willHaveAdministrativeParticipant(ChatRoom chatRoom, ParticipantId targetParticipantId,
			List<ChatRole> requestedRoles) {
		return hasAdministrativeRole(requestedRoles)
				|| chatRoom.getParticipants().stream().filter(participant -> participant.getLeftAt() == null)
						.filter(participant -> !participant.getId().equals(targetParticipantId))
						.anyMatch(participant -> participant.getRoles().stream().map(chatRoom::getRole)
								.anyMatch(this::hasAdministrativePermission));
	}

	private boolean hasAdministrativeRole(List<ChatRole> roles) {
		return roles.stream().anyMatch(this::hasAdministrativePermission);
	}

	private boolean hasAdministrativePermission(ChatRole role) {
		return role.getRolePermissions().stream().map(ChatPermission::getCode)
				.anyMatch(ChatPermissionList.ADMINISTRATIVE::contains);
	}
}
