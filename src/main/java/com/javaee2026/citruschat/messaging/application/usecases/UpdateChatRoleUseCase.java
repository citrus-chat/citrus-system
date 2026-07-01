package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.UpdateChatRoleCommand;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatRoleException;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoleRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.results.UpdateChatRoleResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import jakarta.transaction.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UpdateChatRoleUseCase {

	private static final int MIN_PRIORITY = 0;
	private static final int MAX_PRIORITY = 100;
	private static final Set<String> ROLE_ADMINISTRATION_PERMISSIONS = Set.of(ChatPermissionList.CAN_CREATE_ROLE,
			ChatPermissionList.CAN_MODIFY_ROLE, ChatPermissionList.CAN_DELETE_ROLE);

	private final IChatRoomRepository chatRoomRepository;
	private final IChatRoleRepository chatRoleRepository;
	private final IChatPermissionRepository chatPermissionRepository;
	private final ChatPermissionAuthorizationService permissionAuthorizationService;

	public UpdateChatRoleUseCase(IChatRoomRepository chatRoomRepository, IChatRoleRepository chatRoleRepository,
			IChatPermissionRepository chatPermissionRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatRoleRepository = chatRoleRepository;
		this.chatPermissionRepository = chatPermissionRepository;
		this.permissionAuthorizationService = permissionAuthorizationService;
	}

	@Transactional
	public UpdateChatRoleResult execute(UpdateChatRoleCommand command) {
		validateCommand(command);

		ChatRoom chatRoom = loadGroupRoom(command);
		ChatRole currentRole = chatRoleRepository.findByIdAndChatRoomId(command.roleId(), command.chatRoomId())
				.orElseThrow(() -> new ChatRoleException(ErrorCode.CHATROLE_NOT_FOUND, "Chat role not found"));

		permissionAuthorizationService.requirePermissionOrCreator(chatRoom, command.requesterUserId(),
				ChatPermissionList.CAN_MODIFY_ROLE);

		String name = normalizeName(command.name());
		validatePriority(command.priority());
		validateHierarchy(chatRoom, currentRole, command);
		if (chatRoleRepository.existsByNameAndChatRoomIdExcludingRole(name, command.chatRoomId(), command.roleId())) {
			throw new ChatRoleException(ErrorCode.CHAT_RULE_CONFLICT, "Chat role name already exists in this group");
		}

		Set<ChatPermission> permissions = resolvePermissions(command.permissionIds());
		boolean lastAdministrativeRole = isAdministrative(currentRole) && countAdministrativeRoles(chatRoom) <= 1;
		if (lastAdministrativeRole && !hasAdministrativePermission(permissions)) {
			throw new ChatRoleException(ErrorCode.CHAT_RULE_CONFLICT,
					"Cannot remove administrative permissions from the last administrative role");
		}
		if (lastAdministrativeRole && removesRoleAdministrationPermission(currentRole, permissions)) {
			throw new ChatRoleException(ErrorCode.CHAT_RULE_CONFLICT,
					"Cannot remove role administration permissions from the last administrative role");
		}
		if (!willHaveAdministrativeParticipantAfterUpdate(chatRoom, command.roleId(), permissions)) {
			throw new ChatRoleException(ErrorCode.CHAT_RULE_CONFLICT,
					"Cannot leave a group chat room without an administrative participant");
		}

		ChatRole updatedRole = ChatRole.reconstitute(currentRole.getId(), currentRole.getChatRoomId(), permissions,
				name, command.priority(), currentRole.getCreatedAt());

		return new UpdateChatRoleResult(chatRoleRepository.update(updatedRole));
	}

	private void validateCommand(UpdateChatRoleCommand command) {
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

	private ChatRoom loadGroupRoom(UpdateChatRoleCommand command) {
		ChatRoom chatRoom = chatRoomRepository.findById(command.chatRoomId())
				.orElseThrow(() -> new ChatRoleException(ErrorCode.CHATROOM_NOT_FOUND, "Chat room not found"));
		if (chatRoom.getType() != ChatRoomType.GROUP) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROOM,
					"Chat roles can only be updated in group chat rooms");
		}
		return chatRoom;
	}

	private void validateHierarchy(ChatRoom chatRoom, ChatRole currentRole, UpdateChatRoleCommand command) {
		if (chatRoom.getCreatedBy().equals(command.requesterUserId())) {
			return;
		}

		int requesterMaxPriority = permissionAuthorizationService.highestRolePriority(chatRoom,
				command.requesterUserId());
		if (currentRole.getPriority() >= requesterMaxPriority || command.priority() > requesterMaxPriority) {
			throw new ChatRoleException(ErrorCode.FORBIDDEN,
					"Requester cannot update roles at or above their highest priority");
		}
	}

	private String normalizeName(String name) {
		if (name == null || name.isBlank()) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROLE, "name cannot be blank");
		}

		String normalized = name.trim();
		if (normalized.length() < 2) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROLE, "name must have at least 2 characters");
		}
		if (normalized.length() > 50) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROLE, "name cannot exceed 50 characters");
		}
		return normalized;
	}

	private void validatePriority(Integer priority) {
		if (priority == null) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROLE, "priority cannot be null");
		}
		if (priority < MIN_PRIORITY || priority > MAX_PRIORITY) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROLE, "priority must be between 0 and 100");
		}
	}

	private Set<ChatPermission> resolvePermissions(List<PermissionId> permissionIds) {
		Set<PermissionId> uniquePermissionIds = validatePermissionIds(permissionIds);
		Set<UUID> ids = uniquePermissionIds.stream().map(PermissionId::value).collect(Collectors.toSet());

		try {
			Set<ChatPermission> permissions = chatPermissionRepository.findAllById(ids);
			if (permissions.size() != ids.size()) {
				throw new ChatRoleException(ErrorCode.INVALID_PERMISSION, "One or more permissions do not exist");
			}
			return permissions;
		} catch (IllegalStateException ex) {
			throw new ChatRoleException(ErrorCode.INVALID_PERMISSION, ex.getMessage());
		}
	}

	private Set<PermissionId> validatePermissionIds(List<PermissionId> permissionIds) {
		if (permissionIds == null || permissionIds.isEmpty()) {
			throw new ChatRoleException(ErrorCode.INVALID_PERMISSION, "permissionIds cannot be empty");
		}
		if (permissionIds.stream().anyMatch(permissionId -> permissionId == null)) {
			throw new ChatRoleException(ErrorCode.INVALID_PERMISSION, "permissionIds cannot contain null values");
		}

		Set<PermissionId> uniquePermissionIds = new LinkedHashSet<>(permissionIds);
		if (uniquePermissionIds.size() != permissionIds.size()) {
			throw new ChatRoleException(ErrorCode.INVALID_PERMISSION, "permissionIds cannot contain duplicates");
		}
		return uniquePermissionIds;
	}

	private boolean willHaveAdministrativeParticipantAfterUpdate(ChatRoom chatRoom, RoleId updatedRoleId,
			Set<ChatPermission> updatedPermissions) {
		Set<RoleId> adminRoleIds = chatRoom.getRoles().values().stream()
				.filter(role -> !role.getId().equals(updatedRoleId)).filter(this::isAdministrative).map(ChatRole::getId)
				.collect(Collectors.toCollection(LinkedHashSet::new));

		if (hasAdministrativePermission(updatedPermissions)) {
			adminRoleIds.add(updatedRoleId);
		}

		return chatRoom.getParticipants().stream().filter(participant -> participant.getLeftAt() == null)
				.anyMatch(participant -> participant.getRoles().stream().anyMatch(adminRoleIds::contains));
	}

	private int countAdministrativeRoles(ChatRoom chatRoom) {
		return (int) chatRoom.getRoles().values().stream().filter(this::isAdministrative).count();
	}

	private boolean isAdministrative(ChatRole role) {
		return hasAdministrativePermission(role.getRolePermissions());
	}

	private boolean hasAdministrativePermission(Set<ChatPermission> permissions) {
		return permissions.stream()
				.anyMatch(permission -> ChatPermissionList.ADMINISTRATIVE.contains(permission.getCode()));
	}

	private boolean removesRoleAdministrationPermission(ChatRole currentRole, Set<ChatPermission> updatedPermissions) {
		Set<String> currentCodes = currentRole.getRolePermissions().stream().map(ChatPermission::getCode)
				.collect(Collectors.toSet());
		Set<String> updatedCodes = updatedPermissions.stream().map(ChatPermission::getCode).collect(Collectors.toSet());

		return ROLE_ADMINISTRATION_PERMISSIONS.stream()
				.anyMatch(permission -> currentCodes.contains(permission) && !updatedCodes.contains(permission));
	}
}
