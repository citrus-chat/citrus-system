package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.messaging.application.commands.CreateChatRoomCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.factory.ChatRoomFactory;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.List;

public class CreateChatRoomUseCase {

	private final IChatRoomRepository chatRoomRepository;
	private final ChatRoomFactory chatRoomFactory;
	private final IUserRepository userRepository;

	public CreateChatRoomUseCase(IChatRoomRepository chatRoomRepository, ChatRoomFactory chatRoomFactory,
			IUserRepository userRepository) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatRoomFactory = chatRoomFactory;
		this.userRepository = userRepository;
	}

	public void execute(CreateChatRoomCommand command) {

		UserId creatorId = new UserId(command.chatRoomCreatorId());

		List<UserId> userIds = command.participantIds().stream().map(UserId::new).toList();

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

		ChatRoom chatRoom = chatRoomFactory.createNew(command.chatRoomType(), command.name(), creatorId);

		chatRoom.initRoles(); // Inicializa los roles por defecto
		chatRoom.initParticipants(creatorId, userIds); // Inicializa los participantes

		chatRoomRepository.save(chatRoom);
	}
}
