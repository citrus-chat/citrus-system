package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.GetChatRolesCommand;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatRoleException;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoleRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.results.GetChatRolesResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

import java.util.Comparator;

public class GetChatRolesUseCase {

	private final IChatRoomRepository chatRoomRepository;
	private final IChatRoleRepository chatRoleRepository;

	public GetChatRolesUseCase(IChatRoomRepository chatRoomRepository, IChatRoleRepository chatRoleRepository) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatRoleRepository = chatRoleRepository;
	}

	public GetChatRolesResult execute(GetChatRolesCommand command) {
		if (command == null || command.chatRoomId() == null) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROOM, "chatRoomId cannot be null");
		}

		ChatRoom chatRoom = chatRoomRepository.findById(command.chatRoomId())
				.orElseThrow(() -> new ChatRoleException(ErrorCode.CHATROOM_NOT_FOUND, "Chat room not found"));

		if (chatRoom.getType() != ChatRoomType.GROUP) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROOM,
					"Chat roles can only be listed in group chat rooms");
		}

		return new GetChatRolesResult(chatRoleRepository.findByChatRoomId(command.chatRoomId()).stream()
				.sorted(Comparator.comparing(ChatRole::getPriority).reversed()).toList());
	}
}
