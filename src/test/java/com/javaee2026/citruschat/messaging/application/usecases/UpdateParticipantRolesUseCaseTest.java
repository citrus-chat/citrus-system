package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.UpdateParticipantRolesCommand;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatParticipantRolesException;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatPermissionDeniedException;
import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.results.UpdateParticipantRolesResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateParticipantRolesUseCaseTest {

	@Mock
	private IChatRoomRepository chatRoomRepository;

	@Mock
	private IChatParticipantRepository chatParticipantRepository;

	@Mock
	private IChatPermissionRepository chatPermissionRepository;

	private UpdateParticipantRolesUseCase useCase;
	private ChatRoomId chatRoomId;
	private ParticipantId targetParticipantId;
	private ParticipantId requesterParticipantId;
	private ParticipantId otherParticipantId;
	private UserId creatorUserId;
	private UserId requesterUserId;
	private UserId targetUserId;
	private RoleId ownerRoleId;
	private RoleId adminRoleId;
	private RoleId memberRoleId;
	private ChatPermission manageRolesPermission;
	private ChatPermission sendMessagePermission;
	private ChatRole ownerRole;
	private ChatRole adminRole;
	private ChatRole memberRole;

	@BeforeEach
	void setUp() {
		useCase = new UpdateParticipantRolesUseCase(chatRoomRepository, chatParticipantRepository,
				chatPermissionRepository, authorizationService());
		chatRoomId = new ChatRoomId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001"));
		targetParticipantId = new ParticipantId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000002"));
		requesterParticipantId = new ParticipantId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000003"));
		otherParticipantId = new ParticipantId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000004"));
		creatorUserId = new UserId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000005"));
		requesterUserId = new UserId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000006"));
		targetUserId = new UserId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000007"));
		ownerRoleId = new RoleId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000008"));
		adminRoleId = new RoleId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000009"));
		memberRoleId = new RoleId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000010"));
		manageRolesPermission = permission("aaaaaaaa-0000-0000-0000-000000000011", ChatPermissionList.CAN_MODIFY_ROLE);
		sendMessagePermission = permission("aaaaaaaa-0000-0000-0000-000000000012", ChatPermissionList.CAN_SEND_MESSAGE);
		ownerRole = role(ownerRoleId, "OWNER", Set.of(manageRolesPermission, sendMessagePermission), 100);
		adminRole = role(adminRoleId, "ADMIN", Set.of(manageRolesPermission, sendMessagePermission), 80);
		memberRole = role(memberRoleId, "MEMBER", Set.of(sendMessagePermission), 10);
	}

	@Test
	void shouldFailWithCommandNull() {
		assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
	}

	@Test
	void shouldFailWithDuplicateRoleIds() {
		UpdateParticipantRolesCommand command = command(requesterUserId, List.of(memberRoleId, memberRoleId));

		ChatParticipantRolesException exception = assertThrows(ChatParticipantRolesException.class,
				() -> useCase.execute(command));

		assertEquals(ErrorCode.INVALID_CHATROLE, exception.getErrorCode());
		verifyNoInteractions(chatRoomRepository);
	}

	@Test
	void shouldFailIfRequesterIsNotParticipant() {
		ChatParticipant target = participant(targetParticipantId, targetUserId, List.of(memberRoleId));
		ChatRoom room = groupRoom(creatorUserId, List.of(target));

		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatParticipantRepository.findActiveByChatRoomIdAndParticipantId(chatRoomId, targetParticipantId))
				.thenReturn(Optional.of(target));
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requesterUserId))
				.thenReturn(Optional.empty());

		ChatParticipantRolesException exception = assertThrows(ChatParticipantRolesException.class,
				() -> useCase.execute(command(requesterUserId, List.of(memberRoleId))));

		assertEquals(ErrorCode.FORBIDDEN, exception.getErrorCode());
		verify(chatParticipantRepository, never()).replaceParticipantRoles(any(), any(), any());
	}

	@Test
	void shouldFailIfTargetParticipantDoesNotExist() {
		ChatRoom room = groupRoom(creatorUserId, List.of());

		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatParticipantRepository.findActiveByChatRoomIdAndParticipantId(chatRoomId, targetParticipantId))
				.thenReturn(Optional.empty());

		ChatParticipantRolesException exception = assertThrows(ChatParticipantRolesException.class,
				() -> useCase.execute(command(requesterUserId, List.of(memberRoleId))));

		assertEquals(ErrorCode.CHAT_PARTICIPANT_NOT_FOUND, exception.getErrorCode());
		verify(chatParticipantRepository, never()).replaceParticipantRoles(any(), any(), any());
	}

	@Test
	void shouldFailIfChatRoomIsNotGroup() {
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(directRoom()));

		ChatParticipantRolesException exception = assertThrows(ChatParticipantRolesException.class,
				() -> useCase.execute(command(requesterUserId, List.of(memberRoleId))));

		assertEquals(ErrorCode.INVALID_CHATROOM, exception.getErrorCode());
		verifyNoMoreInteractions(chatParticipantRepository);
	}

	@Test
	void shouldFailIfRequesterHasNoPermission() {
		ChatParticipant requester = participant(requesterParticipantId, requesterUserId, List.of(memberRoleId));
		ChatParticipant target = participant(targetParticipantId, targetUserId, List.of(memberRoleId));
		ChatRoom room = groupRoom(creatorUserId, List.of(requester, target));

		stubParticipants(room, target, requester);
		effectiveRequesterPermissions(sendMessagePermission);

		ChatPermissionDeniedException exception = assertThrows(ChatPermissionDeniedException.class,
				() -> useCase.execute(command(requesterUserId, List.of(adminRoleId))));

		assertEquals(ErrorCode.CHAT_PERMISSION_DENIED, exception.getErrorCode());
		verify(chatParticipantRepository, never()).replaceParticipantRoles(any(), any(), any());
	}

	@Test
	void shouldAllowCreatorToManageRoles() {
		ChatParticipant requester = participant(requesterParticipantId, creatorUserId, List.of(memberRoleId));
		ChatParticipant target = participant(targetParticipantId, targetUserId, List.of(memberRoleId));
		ChatRoom room = groupRoom(creatorUserId, List.of(requester, target));

		stubParticipants(room, target, requester);
		when(chatPermissionRepository.findPermissionsByChatRoomAndParticipant(chatRoomId, targetParticipantId))
				.thenReturn(Set.of(manageRolesPermission));

		useCase.execute(command(creatorUserId, List.of(adminRoleId)));

		verify(chatParticipantRepository).replaceParticipantRoles(chatRoomId, targetParticipantId,
				List.of(adminRoleId));
	}

	@Test
	void shouldAllowRequesterWithManageRolesPermission() {
		ChatParticipant requester = participant(requesterParticipantId, requesterUserId, List.of(adminRoleId));
		ChatParticipant target = participant(targetParticipantId, targetUserId, List.of(memberRoleId));
		ChatRoom room = groupRoom(creatorUserId, List.of(requester, target));

		stubParticipants(room, target, requester);
		effectiveRequesterPermissions(manageRolesPermission);
		when(chatPermissionRepository.findPermissionsByChatRoomAndParticipant(chatRoomId, targetParticipantId))
				.thenReturn(Set.of(sendMessagePermission));

		useCase.execute(command(requesterUserId, List.of(memberRoleId)));

		verify(chatParticipantRepository).replaceParticipantRoles(chatRoomId, targetParticipantId,
				List.of(memberRoleId));
	}

	@Test
	void shouldFailIfRequesterAssignsRoleAboveOwnPriority() {
		ChatParticipant requester = participant(requesterParticipantId, requesterUserId, List.of(adminRoleId));
		ChatParticipant target = participant(targetParticipantId, targetUserId, List.of(memberRoleId));
		ChatRoom room = groupRoom(creatorUserId, List.of(requester, target));

		stubParticipants(room, target, requester);
		effectiveRequesterPermissions(manageRolesPermission);

		ChatParticipantRolesException exception = assertThrows(ChatParticipantRolesException.class,
				() -> useCase.execute(command(requesterUserId, List.of(ownerRoleId))));

		assertEquals(ErrorCode.FORBIDDEN, exception.getErrorCode());
		verify(chatParticipantRepository, never()).replaceParticipantRoles(any(), any(), any());
	}

	@Test
	void shouldFailIfUpdateWouldLeaveGroupWithoutAdmin() {
		ChatParticipant targetAndRequester = participant(targetParticipantId, requesterUserId, List.of(adminRoleId));
		ChatRoom room = groupRoom(creatorUserId, List.of(targetAndRequester));

		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatParticipantRepository.findActiveByChatRoomIdAndParticipantId(chatRoomId, targetParticipantId))
				.thenReturn(Optional.of(targetAndRequester));
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requesterUserId))
				.thenReturn(Optional.of(targetAndRequester));
		when(chatPermissionRepository.findPermissionsByChatRoomAndParticipant(chatRoomId, targetParticipantId))
				.thenReturn(Set.of(manageRolesPermission));

		ChatParticipantRolesException exception = assertThrows(ChatParticipantRolesException.class,
				() -> useCase.execute(command(requesterUserId, List.of(memberRoleId))));

		assertEquals(ErrorCode.CHAT_RULE_CONFLICT, exception.getErrorCode());
		verify(chatParticipantRepository, never()).replaceParticipantRoles(any(), any(), any());
	}

	@Test
	void shouldFailWithUnknownRole() {
		RoleId unknownRoleId = new RoleId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000013"));
		ChatParticipant requester = participant(requesterParticipantId, requesterUserId, List.of(adminRoleId));
		ChatParticipant target = participant(targetParticipantId, targetUserId, List.of(memberRoleId));
		ChatRoom room = groupRoom(creatorUserId, List.of(requester, target));

		stubParticipants(room, target, requester);
		effectiveRequesterPermissions(manageRolesPermission);

		ChatParticipantRolesException exception = assertThrows(ChatParticipantRolesException.class,
				() -> useCase.execute(command(requesterUserId, List.of(unknownRoleId))));

		assertEquals(ErrorCode.INVALID_CHATROLE, exception.getErrorCode());
		verify(chatParticipantRepository, never()).replaceParticipantRoles(any(), any(), any());
	}

	@Test
	void shouldUpdateRolesAndReturnRecalculatedPermissions() {
		ChatParticipant requester = participant(requesterParticipantId, requesterUserId, List.of(adminRoleId));
		ChatParticipant target = participant(targetParticipantId, targetUserId, List.of(memberRoleId));
		ChatRoom room = groupRoom(creatorUserId, List.of(requester, target));

		stubParticipants(room, target, requester);
		effectiveRequesterPermissions(manageRolesPermission);
		when(chatPermissionRepository.findPermissionsByChatRoomAndParticipant(chatRoomId, targetParticipantId))
				.thenReturn(Set.of(manageRolesPermission, sendMessagePermission));

		UpdateParticipantRolesResult result = useCase.execute(command(requesterUserId, List.of(adminRoleId)));

		assertEquals(targetParticipantId, result.participantId());
		assertEquals(chatRoomId, result.chatRoomId());
		assertEquals(targetUserId, result.userId());
		assertEquals(List.of(adminRoleId), result.roleIds());
		assertEquals(Set.of(manageRolesPermission, sendMessagePermission), result.permissions());
		verify(chatParticipantRepository).replaceParticipantRoles(chatRoomId, targetParticipantId,
				List.of(adminRoleId));
		verify(chatPermissionRepository).findPermissionsByChatRoomAndParticipant(chatRoomId, targetParticipantId);
	}

	private void stubParticipants(ChatRoom room, ChatParticipant target, ChatParticipant requester) {
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatParticipantRepository.findActiveByChatRoomIdAndParticipantId(chatRoomId, targetParticipantId))
				.thenReturn(Optional.of(target));
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requester.getUserId()))
				.thenReturn(Optional.of(requester));
	}

	private ChatPermissionAuthorizationService authorizationService() {
		return new ChatPermissionAuthorizationService(chatRoomRepository, chatParticipantRepository,
				chatPermissionRepository);
	}

	private void effectiveRequesterPermissions(ChatPermission... permissions) {
		when(chatPermissionRepository.findPermissionsByChatRoomAndParticipant(chatRoomId, requesterParticipantId))
				.thenReturn(Set.of(permissions));
	}

	private UpdateParticipantRolesCommand command(UserId requester, List<RoleId> roleIds) {
		return new UpdateParticipantRolesCommand(chatRoomId, targetParticipantId, requester, roleIds);
	}

	private ChatRoom groupRoom(UserId createdBy, List<ChatParticipant> participants) {
		return new ChatRoom(chatRoomId, ChatRoomType.GROUP, "Group", createdBy, participants, roles(), Instant.now(),
				null, null);
	}

	private ChatRoom directRoom() {
		return new ChatRoom(chatRoomId, ChatRoomType.DIRECT, "Direct", creatorUserId, List.of(), roles(), Instant.now(),
				null, null);
	}

	private Map<String, ChatRole> roles() {
		return Map.of("OWNER", ownerRole, "ADMIN", adminRole, "MEMBER", memberRole);
	}

	private ChatParticipant participant(ParticipantId participantId, UserId userId, List<RoleId> roleIds) {
		return ChatParticipant.reconstitute(participantId, chatRoomId, userId, roleIds, Instant.now(), null, null);
	}

	private ChatRole role(RoleId roleId, String name, Set<ChatPermission> permissions, int priority) {
		return ChatRole.reconstitute(roleId, chatRoomId, permissions, name, priority, Instant.now());
	}

	private ChatPermission permission(String id, String code) {
		return new ChatPermission(new PermissionId(UUID.fromString(id)), code, code);
	}
}
