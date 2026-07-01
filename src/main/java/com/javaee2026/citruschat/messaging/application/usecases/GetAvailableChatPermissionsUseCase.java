package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.GetAvailableChatPermissionsCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.results.GetAvailableChatPermissionsResult;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;

import java.util.Comparator;

public class GetAvailableChatPermissionsUseCase {

	private final IChatPermissionRepository chatPermissionRepository;

	public GetAvailableChatPermissionsUseCase(IChatPermissionRepository chatPermissionRepository) {
		this.chatPermissionRepository = chatPermissionRepository;
	}

	public GetAvailableChatPermissionsResult execute(GetAvailableChatPermissionsCommand command) {
		return new GetAvailableChatPermissionsResult(chatPermissionRepository.findAll().stream()
				.sorted(Comparator.comparing(ChatPermission::getCode)).toList());
	}
}
