package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.CreateChatRoleCommand;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatRoleException;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoleRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.results.CreateChatRoleResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;
import jakarta.transaction.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreateChatRoleUseCase {

	private static final int MIN_PRIORITY = 0;
	private static final int MAX_PRIORITY = 100;

	private final IChatRoomRepository chatRoomRepository;
	private final IChatRoleRepository chatRoleRepository;
	private final IChatPermissionRepository chatPermissionRepository;
	private final ChatPermissionAuthorizationService permissionAuthorizationService;

	public CreateChatRoleUseCase(IChatRoomRepository chatRoomRepository, IChatRoleRepository chatRoleRepository,
			IChatPermissionRepository chatPermissionRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatRoleRepository = chatRoleRepository;
		this.chatPermissionRepository = chatPermissionRepository;
		this.permissionAuthorizationService = permissionAuthorizationService;
	}

	@Transactional
	public CreateChatRoleResult execute(CreateChatRoleCommand command) {
		validateCommand(command);

		ChatRoom chatRoom = loadGroupRoom(command);
		permissionAuthorizationService.requirePermissionOrCreator(chatRoom, command.requesterUserId(),
				ChatPermissionList.CAN_CREATE_ROLE);

		String name = normalizeName(command.name());
		validatePriority(command.priority());
		if (chatRoleRepository.existsByNameAndChatRoomId(name, command.chatRoomId())) {
			throw new ChatRoleException(ErrorCode.CHAT_RULE_CONFLICT, "Chat role name already exists in this group");
		}
		if (!chatRoom.getCreatedBy().equals(command.requesterUserId()) && command
				.priority() > permissionAuthorizationService.highestRolePriority(chatRoom, command.requesterUserId())) {
			throw new ChatRoleException(ErrorCode.FORBIDDEN,
					"Requester cannot create a role above their highest priority");
		}

		Set<ChatPermission> permissions = resolvePermissions(command.permissionIds());
		ChatRole role = ChatRole.create(command.chatRoomId(), permissions, name, command.priority());

		return new CreateChatRoleResult(chatRoleRepository.save(role));
	}

	private void validateCommand(CreateChatRoleCommand command) {
		if (command == null) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROLE, "command cannot be null");
		}
		if (command.chatRoomId() == null) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROOM, "chatRoomId cannot be null");
		}
		if (command.requesterUserId() == null) {
			throw new ChatRoleException(ErrorCode.INVALID_USER, "requesterUserId cannot be null");
		}
	}

	private ChatRoom loadGroupRoom(CreateChatRoleCommand command) {
		ChatRoom chatRoom = chatRoomRepository.findById(command.chatRoomId())
				.orElseThrow(() -> new ChatRoleException(ErrorCode.CHATROOM_NOT_FOUND, "Chat room not found"));
		if (chatRoom.getType() != ChatRoomType.GROUP) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROOM,
					"Chat roles can only be created in group chat rooms");
		}
		return chatRoom;
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
}
