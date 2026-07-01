package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.GetChatRoleCommand;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatRoleException;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoleRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.results.GetChatRoleResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;

public class GetChatRoleUseCase {

	private final IChatRoomRepository chatRoomRepository;
	private final IChatRoleRepository chatRoleRepository;

	public GetChatRoleUseCase(IChatRoomRepository chatRoomRepository, IChatRoleRepository chatRoleRepository) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatRoleRepository = chatRoleRepository;
	}

	public GetChatRoleResult execute(GetChatRoleCommand command) {
		if (command == null || command.chatRoomId() == null) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROOM, "chatRoomId cannot be null");
		}
		if (command.roleId() == null) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROLE, "roleId cannot be null");
		}

		ChatRoom chatRoom = chatRoomRepository.findById(command.chatRoomId())
				.orElseThrow(() -> new ChatRoleException(ErrorCode.CHATROOM_NOT_FOUND, "Chat room not found"));

		if (chatRoom.getType() != ChatRoomType.GROUP) {
			throw new ChatRoleException(ErrorCode.INVALID_CHATROOM,
					"Chat roles can only be retrieved in group chat rooms");
		}

		return new GetChatRoleResult(chatRoleRepository.findByIdAndChatRoomId(command.roleId(), command.chatRoomId())
				.orElseThrow(() -> new ChatRoleException(ErrorCode.CHATROLE_NOT_FOUND, "Chat role not found")));
	}
}
