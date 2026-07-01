package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.exceptions.ChatParticipantRolesException;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatPermissionDeniedException;
import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.Set;
import java.util.stream.Collectors;

public class ChatPermissionAuthorizationService {

	private final IChatRoomRepository chatRoomRepository;
	private final IChatParticipantRepository chatParticipantRepository;
	private final IChatPermissionRepository chatPermissionRepository;

	public ChatPermissionAuthorizationService(IChatRoomRepository chatRoomRepository,
			IChatParticipantRepository chatParticipantRepository, IChatPermissionRepository chatPermissionRepository) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatParticipantRepository = chatParticipantRepository;
		this.chatPermissionRepository = chatPermissionRepository;
	}

	public void requirePermission(ChatRoomId chatRoomId, UserId requesterUserId, String permissionCode) {
		requirePermission(loadChatRoom(chatRoomId), requesterUserId, permissionCode);
	}

	public void requirePermission(ChatRoom chatRoom, UserId requesterUserId, String permissionCode) {
		if (!hasPermission(chatRoom, requesterUserId, permissionCode)) {
			throw new ChatPermissionDeniedException();
		}
	}

	public void requirePermissionOrCreator(ChatRoom chatRoom, UserId requesterUserId, String permissionCode) {
		validateRequesterUser(requesterUserId);
		if (chatRoom.getCreatedBy().equals(requesterUserId)) {
			return;
		}
		requirePermission(chatRoom, requesterUserId, permissionCode);
	}

	public boolean hasPermission(ChatRoomId chatRoomId, UserId requesterUserId, String permissionCode) {
		return hasPermission(loadChatRoom(chatRoomId), requesterUserId, permissionCode);
	}

	public boolean hasPermission(ChatRoom chatRoom, UserId requesterUserId, String permissionCode) {
		validateChatRoom(chatRoom);
		validateRequesterUser(requesterUserId);
		validatePermissionCode(permissionCode);

		ChatParticipant participant = chatParticipantRepository
				.findActiveByChatRoomIdAndUserId(chatRoom.getId(), requesterUserId)
				.orElseThrow(() -> new ChatParticipantRolesException(ErrorCode.FORBIDDEN,
						"Requester is not an active participant of this chat room"));

		Set<String> permissionCodes = chatPermissionRepository
				.findPermissionsByChatRoomAndParticipant(chatRoom.getId(), participant.getId()).stream()
				.map(ChatPermission::getCode).collect(Collectors.toSet());

		return permissionCodes.contains(permissionCode);
	}

	public int highestRolePriority(ChatRoom chatRoom, UserId userId) {
		validateChatRoom(chatRoom);
		validateRequesterUser(userId);
		return chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoom.getId(), userId)
				.map(participant -> participant.getRoles().stream().map(chatRoom::getRole)
						.mapToInt(ChatRole::getPriority).max().orElse(0))
				.orElse(0);
	}

	private ChatRoom loadChatRoom(ChatRoomId chatRoomId) {
		if (chatRoomId == null) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_CHATROOM, "chatRoomId cannot be null");
		}
		return chatRoomRepository.findById(chatRoomId).orElseThrow(
				() -> new ChatParticipantRolesException(ErrorCode.CHATROOM_NOT_FOUND, "Chat room not found"));
	}

	private void validateChatRoom(ChatRoom chatRoom) {
		if (chatRoom == null) {
			throw new ChatParticipantRolesException(ErrorCode.CHATROOM_NOT_FOUND, "Chat room not found");
		}
	}

	private void validateRequesterUser(UserId requesterUserId) {
		if (requesterUserId == null) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_USER, "requesterUserId cannot be null");
		}
	}

	private void validatePermissionCode(String permissionCode) {
		if (permissionCode == null || permissionCode.isBlank()) {
			throw new ChatParticipantRolesException(ErrorCode.INVALID_PERMISSION, "permissionCode cannot be blank");
		}
	}
}
