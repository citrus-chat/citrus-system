package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.GetParticipantPermissionsCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.results.GetParticipantPermissionsResult;

public class GetParticipantPermissionsUseCase {

	private final IChatPermissionRepository chatPermissionRepository;
	private final ValidateChatParticipantUseCase validateChatParticipantUseCase;

	public GetParticipantPermissionsUseCase(IChatPermissionRepository chatPermissionRepository,
			ValidateChatParticipantUseCase validateChatParticipantUseCase) {
		this.chatPermissionRepository = chatPermissionRepository;
		this.validateChatParticipantUseCase = validateChatParticipantUseCase;
	}

	public GetParticipantPermissionsResult execute(GetParticipantPermissionsCommand command) {

		if (command == null) {
			throw new IllegalArgumentException("command cannot be null");
		}

		if (!validateChatParticipantUseCase.execute(command.chatRoomId().value(), command.participantId().value())) {
			throw new IllegalArgumentException("User is not participant of this chat room");
		}

		return new GetParticipantPermissionsResult(chatPermissionRepository
				.findPermissionsByChatRoomAndParticipant(command.chatRoomId(), command.participantId()));
	}
}
