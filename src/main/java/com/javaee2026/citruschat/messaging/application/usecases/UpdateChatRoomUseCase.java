package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.UpdateChatRoomCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.results.UpdateChatRoomResult;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

public class UpdateChatRoomUseCase {
	private final IChatRoomRepository chatRoomRepository;

	public UpdateChatRoomUseCase(IChatRoomRepository chatRoomRepository) {
		this.chatRoomRepository = chatRoomRepository;
	}

	private ChatRoom validateChatRoom(ChatRoomId chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new IllegalArgumentException("Chat room not found with id: " + chatRoomId.value()));
	}

	private ChatParticipant validateParticipant(ChatRoom chatRoom, UserId requesterId) {
		return chatRoom.getParticipants().stream().filter(participant -> participant.getUserId().equals(requesterId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("User is not a participant of this chat room"));
	}

	public void validatePermission(ChatRoom chatRoom, ChatParticipant participant) {
		if (!chatRoom.hasPermission(participant, ChatPermissionList.CAN_MODIFY_CHAT)) {
			throw new IllegalArgumentException("User does not have permission to update the chat room");
		}
	}

	public UpdateChatRoomResult execute(UpdateChatRoomCommand command) {
		ChatRoom chat = validateChatRoom(new ChatRoomId(command.chatRoomId()));
		ChatParticipant participant = validateParticipant(chat, new UserId(command.requesterId()));

		validatePermission(chat, participant);

		chat.rename(command.name());
		chatRoomRepository.save(chat);

		return new UpdateChatRoomResult(chat.getId(), chat.getName(), chat.getAvatarUrl(), chat.getUpdatedAt());
	}
}
