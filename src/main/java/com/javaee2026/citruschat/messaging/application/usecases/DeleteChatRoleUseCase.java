package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.DeleteChatRoleCommand;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatRoleException;
import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoleRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.results.DeleteChatRoleResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import jakarta.transaction.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DeleteChatRoleUseCase {

	private final IChatRoomRepository chatRoomRepository;
	private final IChatRoleRepository chatRoleRepository;
	private final IChatParticipantRepository chatParticipantRepository;
	private final ChatPermissionAuthorizationService permissionAuthorizationService;

	public DeleteChatRoleUseCase(IChatRoomRepository chatRoomRepository, IChatRoleRepository chatRoleRepository,
			IChatParticipantRepository chatParticipantRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatRoleRepository = chatRoleRepository;
		this.chatParticipantRepository = chatParticipantRepository;
		this.permissionAuthorizationService = permissionAuthorizationService;
	}

	@Transactional
	public DeleteChatRoleResult execute(DeleteChatRoleCommand command) {
		validateCommand(command);

		ChatRoom chatRoom = loadGroupRoom(command);
		ChatRole role = chatRoleRepository.findByIdAndChatRoomId(command.roleId(), command.chatRoomId())
				.orElseThrow(() -> new ChatRoleException(ErrorCode.CHATROLE_NOT_FOUND, "Chat role not found"));

		permissionAuthorizationService.requirePermissionOrCreator(chatRoom, command.requesterUserId(),
				ChatPermissionList.CAN_DELETE_ROLE);
		validateHierarchy(chatRoom, role, command);
		if (isAdministrative(role) && countAdministrativeRoles(chatRoom) <= 1) {
			throw new ChatRoleException(ErrorCode.CHAT_RULE_CONFLICT, "Cannot delete the last administrative role");
		}

		boolean assigned = chatParticipantRepository.isRoleAssignedToAnyParticipant(command.chatRoomId(),
				command.roleId());
		ChatRole replacementRole = validateReplacementRole(command, assigned);
		RoleId replacementRoleId = replacementRole != null ? replacementRole.getId() : null;

		if (!willHaveAdministrativeParticipantAfterDelete(chatRoom, command.roleId(), replacementRoleId)) {
			throw new ChatRoleException(ErrorCode.CHAT_RULE_CONFLICT,
					"Cannot leave a group chat room without an administrative participant");
		}

		if (assigned) {
			chatParticipantRepository.replaceRoleForParticipants(command.chatRoomId(), command.roleId(),
					replacementRoleId);
		}
		if (!chatRoleRepository.delete(command.roleId())) {
			throw new ChatRoleException(ErrorCode.CHATROLE_NOT_FOUND, "Chat role not found");
		}

		return new DeleteChatRoleResult(command.roleId());
	}

	private void validateCommand(DeleteChatRoleCommand command) {
		if (command == null) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROLE, "command cannot be null");
		}
		if (command.chatRoomId() == null) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROOM, "chatRoomId cannot be null");
		}
		if (command.roleId() == null) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROLE, "roleId cannot be null");
		}
		if (command.requesterUserId() == null) {
			throw new ChatRoleException(ErrorCode.INVALID_USER, "requesterUserId cannot be null");
		}
	}

	private ChatRoom loadGroupRoom(DeleteChatRoleCommand command) {
		ChatRoom chatRoom = chatRoomRepository.findById(command.chatRoomId())
				.orElseThrow(() -> new ChatRoleException(ErrorCode.CHATROOM_NOT_FOUND, "Chat room not found"));
		if (chatRoom.getType() != ChatRoomType.GROUP) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROOM,
					"Chat roles can only be deleted in group chat rooms");
		}
		return chatRoom;
	}

	private void validateHierarchy(ChatRoom chatRoom, ChatRole role, DeleteChatRoleCommand command) {
		if (chatRoom.getCreatedBy().equals(command.requesterUserId())) {
			return;
		}

		int requesterMaxPriority = permissionAuthorizationService.highestRolePriority(chatRoom,
				command.requesterUserId());
		if (role.getPriority() >= requesterMaxPriority) {
			throw new ChatRoleException(ErrorCode.FORBIDDEN,
					"Requester cannot delete roles at or above their highest priority");
		}
	}

	private ChatRole validateReplacementRole(DeleteChatRoleCommand command, boolean assigned) {
		if (command.replacementRoleId() != null && command.replacementRoleId().equals(command.roleId())) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROLE, "replacementRoleId cannot be the deleted role");
		}
		if (!assigned) {
			return command.replacementRoleId() == null
					? null
					: chatRoleRepository.findByIdAndChatRoomId(command.replacementRoleId(), command.chatRoomId())
							.orElseThrow(() -> new ChatRoleException(ErrorCode.CHATROLE_NOT_FOUND,
									"Replacement chat role not found"));
		}
		if (command.replacementRoleId() == null) {
			throw new ChatRoleException(ErrorCode.CHAT_RULE_CONFLICT,
					"replacementRoleId is required when the role is assigned to participants");
		}

		return chatRoleRepository.findByIdAndChatRoomId(command.replacementRoleId(), command.chatRoomId()).orElseThrow(
				() -> new ChatRoleException(ErrorCode.CHATROLE_NOT_FOUND, "Replacement chat role not found"));
	}

	private boolean willHaveAdministrativeParticipantAfterDelete(ChatRoom chatRoom, RoleId deletedRoleId,
			RoleId replacementRoleId) {
		Set<RoleId> adminRoleIds = chatRoom.getRoles().values().stream()
				.filter(role -> !role.getId().equals(deletedRoleId)).filter(this::isAdministrative).map(ChatRole::getId)
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return chatRoom.getParticipants().stream().filter(participant -> participant.getLeftAt() == null)
				.anyMatch(participant -> hasAdministrativeRoleAfterDelete(participant, deletedRoleId, replacementRoleId,
						adminRoleIds));
	}

	private boolean hasAdministrativeRoleAfterDelete(ChatParticipant participant, RoleId deletedRoleId,
			RoleId replacementRoleId, Set<RoleId> adminRoleIds) {
		Set<RoleId> roleIds = new LinkedHashSet<>(participant.getRoles());
		if (roleIds.remove(deletedRoleId) && replacementRoleId != null) {
			roleIds.add(replacementRoleId);
		}
		return roleIds.stream().anyMatch(adminRoleIds::contains);
	}

	private int countAdministrativeRoles(ChatRoom chatRoom) {
		return (int) chatRoom.getRoles().values().stream().filter(this::isAdministrative).count();
	}

	private boolean isAdministrative(ChatRole role) {
		return role.getRolePermissions().stream().map(ChatPermission::getCode)
				.anyMatch(ChatPermissionList.ADMINISTRATIVE::contains);
	}
}
