package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.messaging.application.commands.CreateChatRoomCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.results.CreateChatRoomResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoleDefault;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.factory.ChatRoomFactory;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.policy.ChatAuthDefaults;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateChatRoomUseCase {

	private final IChatRoomRepository chatRoomRepository;
	private final ChatRoomFactory chatRoomFactory;
	private final IUserRepository userRepository;
	private final IChatPermissionRepository permissionRepository;

	public CreateChatRoomUseCase(IChatRoomRepository chatRoomRepository, ChatRoomFactory chatRoomFactory,
			IUserRepository userRepository, IChatPermissionRepository permissionRepository) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatRoomFactory = chatRoomFactory;
		this.userRepository = userRepository;
		this.permissionRepository = permissionRepository;
	}

	public void validate(CreateChatRoomCommand command, UserId creatorId, List<UserId> userIds) {
		if (command.chatRoomType() == ChatRoomType.DIRECT) {
			if (command.participantIds().size() != 1) {
				throw new IllegalArgumentException("Direct chat rooms must have exactly one participant");
			}
		}

		if (userRepository.findById(creatorId).isEmpty()) {
			throw new IllegalArgumentException("User with id " + creatorId + " does not exist");
		}

		if (userIds.contains(creatorId)) {
			throw new IllegalArgumentException("Creator cannot be participant twice");
		}

		for (UserId userId : userIds) {
			if (userRepository.findById(userId).isEmpty()) {
				throw new IllegalArgumentException("User with id " + userId + " does not exist");
			}
		}

		if (command.chatRoomType() == ChatRoomType.DIRECT) {
			if (chatRoomRepository.existsDirectChatBetweenParticipants(creatorId, userIds.getFirst())) {
				throw new IllegalArgumentException("Direct chat room already exists between these participants");
			}
		}
	}

	public Map<ChatRoleDefault, Set<ChatPermission>> resolvePermision() {
		Map<ChatRoleDefault, Set<ChatPermission>> resolved = new HashMap<>();

		for (var entry : ChatAuthDefaults.DEFAULTS.entrySet()) {
			Set<ChatPermission> permissions = permissionRepository.findByCodes(entry.getValue());

			if (permissions.isEmpty()) {
				continue;
			}

			resolved.put(entry.getKey(), permissions);
		}

		System.out.println(resolved);

		return resolved;
	}

	public CreateChatRoomResult execute(CreateChatRoomCommand command) {

		UserId creatorId = new UserId(command.chatRoomCreatorId());

		List<UserId> userIds = command.participantIds().stream().map(UserId::new).toList();

		validate(command, creatorId, userIds);

		ChatRoom chatRoom = chatRoomFactory.createNew(command.chatRoomType(), command.name(), creatorId);

		Map<ChatRoleDefault, Set<ChatPermission>> rolesDefaultPermissions = resolvePermision();

		chatRoom.initRoles(rolesDefaultPermissions); // Inicializa los roles por defecto
		chatRoom.initParticipants(creatorId, userIds); // Inicializa los participantes

		chatRoomRepository.save(chatRoom);

		return new CreateChatRoomResult(chatRoom.getId(), chatRoom.getType(), chatRoom.getName(),
				chatRoom.getCreatedBy(), chatRoom.getParticipants(), chatRoom.getRoles(), chatRoom.getCreatedAt(),
				chatRoom.getUpdatedAt(), chatRoom.getDeletedAt());
	}
}
