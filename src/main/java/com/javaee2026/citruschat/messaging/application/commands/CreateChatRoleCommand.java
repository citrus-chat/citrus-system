package com.javaee2026.citruschat.messaging.application.commands;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.List;

public record CreateChatRoleCommand(ChatRoomId chatRoomId, UserId requesterUserId, String name, Integer priority,
		List<PermissionId> permissionIds) {
}
