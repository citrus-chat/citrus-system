package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.commands.CreateChatRoleCommand;
import com.javaee2026.citruschat.messaging.application.commands.DeleteChatRoleCommand;
import com.javaee2026.citruschat.messaging.application.commands.UpdateChatRoleCommand;
import com.javaee2026.citruschat.messaging.application.results.GetAvailableChatPermissionsResult;
import com.javaee2026.citruschat.messaging.application.results.GetChatRolesResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.CreateChatRoleRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.UpdateChatRoleRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatPermissionsResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRolesResponse;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.UUID;

public final class ChatRoleWebMapper {

	private ChatRoleWebMapper() {
	}

	public static CreateChatRoleCommand toCommand(CreateChatRoleRequest request, UUID chatRoomId,
			UUID requesterUserId) {
		return new CreateChatRoleCommand(new ChatRoomId(chatRoomId), new UserId(requesterUserId), request.name(),
				request.priority(), request.permissionIds().stream().map(PermissionId::new).toList());
	}

	public static UpdateChatRoleCommand toCommand(UpdateChatRoleRequest request, UUID chatRoomId, UUID roleId,
			UUID requesterUserId) {
		return new UpdateChatRoleCommand(new ChatRoomId(chatRoomId), new RoleId(roleId), new UserId(requesterUserId),
				request.name(), request.priority(), request.permissionIds().stream().map(PermissionId::new).toList());
	}

	public static DeleteChatRoleCommand toCommand(UUID chatRoomId, UUID roleId, UUID requesterUserId,
			UUID replacementRoleId) {
		return new DeleteChatRoleCommand(new ChatRoomId(chatRoomId), new RoleId(roleId), new UserId(requesterUserId),
				replacementRoleId != null ? new RoleId(replacementRoleId) : null);
	}

	public static ChatRolesResponse toResponse(GetChatRolesResult result) {
		return new ChatRolesResponse(result.roles().stream().map(ChatRoleResponseMapper::toResponse).toList());
	}

	public static ChatPermissionsResponse toResponse(GetAvailableChatPermissionsResult result) {
		return new ChatPermissionsResponse(
				result.permissions().stream().map(ChatPermissionResponseMapper::toResponse).toList());
	}
}
