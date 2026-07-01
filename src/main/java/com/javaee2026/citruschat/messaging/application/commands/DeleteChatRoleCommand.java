package com.javaee2026.citruschat.messaging.application.commands;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

public record DeleteChatRoleCommand(ChatRoomId chatRoomId, RoleId roleId, UserId requesterUserId,
		RoleId replacementRoleId) {
}
