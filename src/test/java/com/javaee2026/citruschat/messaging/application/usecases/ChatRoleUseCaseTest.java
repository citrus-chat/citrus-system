package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.messaging.application.commands.CreateChatRoleCommand;
import com.javaee2026.citruschat.messaging.application.commands.CreateChatRoomCommand;
import com.javaee2026.citruschat.messaging.application.commands.DeleteChatRoleCommand;
import com.javaee2026.citruschat.messaging.application.commands.GetChatRolesCommand;
import com.javaee2026.citruschat.messaging.application.commands.UpdateChatRoleCommand;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatPermissionDeniedException;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatRoleException;
import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoleRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoleDefault;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.factory.ChatRoomFactory;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRoomResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.mapper.ChatRoomResponseMapper;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoleUseCaseTest {

	private final ChatRoomId chatRoomId = new ChatRoomId(UUID.fromString("cccccccc-0000-0000-0000-000000000001"));
	private final UserId creatorUserId = new UserId(UUID.fromString("cccccccc-0000-0000-0000-000000000002"));
	private final UserId requesterUserId = new UserId(UUID.fromString("cccccccc-0000-0000-0000-000000000003"));
	private final UserId targetUserId = new UserId(UUID.fromString("cccccccc-0000-0000-0000-000000000004"));
	private final ParticipantId creatorParticipantId = new ParticipantId(
			UUID.fromString("cccccccc-0000-0000-0000-000000000005"));
	private final ParticipantId requesterParticipantId = new ParticipantId(
			UUID.fromString("cccccccc-0000-0000-0000-000000000006"));
	private final ParticipantId targetParticipantId = new ParticipantId(
			UUID.fromString("cccccccc-0000-0000-0000-000000000007"));
	private final RoleId ownerRoleId = new RoleId(UUID.fromString("cccccccc-0000-0000-0000-000000000008"));
	private final RoleId adminRoleId = new RoleId(UUID.fromString("cccccccc-0000-0000-0000-000000000009"));
	private final RoleId memberRoleId = new RoleId(UUID.fromString("cccccccc-0000-0000-0000-000000000010"));
	private final RoleId customRoleId = new RoleId(UUID.fromString("cccccccc-0000-0000-0000-000000000011"));
	private final PermissionId sendPermissionId = new PermissionId(
			UUID.fromString("cccccccc-0000-0000-0000-000000000012"));
	private final PermissionId createRolePermissionId = new PermissionId(
			UUID.fromString("cccccccc-0000-0000-0000-000000000013"));
	private final PermissionId modifyRolePermissionId = new PermissionId(
			UUID.fromString("cccccccc-0000-0000-0000-000000000014"));
	private final PermissionId deleteRolePermissionId = new PermissionId(
			UUID.fromString("cccccccc-0000-0000-0000-000000000015"));

	@Mock
	private IChatRoomRepository chatRoomRepository;

	@Mock
	private IChatRoleRepository chatRoleRepository;

	@Mock
	private IChatParticipantRepository chatParticipantRepository;

	@Mock
	private IChatPermissionRepository chatPermissionRepository;

	@Mock
	private IUserRepository userRepository;

	@Test
	void createGroupAssignsOwnerToCreatorAndMemberToOthers() {
		UUID creatorId = creatorUserId.value();
		UUID participantId = targetUserId.value();
		CreateChatRoomUseCase useCase = new CreateChatRoomUseCase(chatRoomRepository, new ChatRoomFactory(),
				userRepository, chatPermissionRepository);

		when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(mock(User.class)));
		when(chatPermissionRepository.findByCodes(anySet()))
				.thenAnswer(invocation -> permissionsByCodes(invocation.getArgument(0)));

		useCase.execute(new CreateChatRoomCommand(ChatRoomType.GROUP, "Group", creatorId, List.of(participantId)));

		ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);
		verify(chatRoomRepository).save(captor.capture());
		ChatRoom room = captor.getValue();
		ChatRole owner = room.getRoles().get(ChatRoleDefault.OWNER.toString());
		ChatRole member = room.getRoles().get(ChatRoleDefault.MEMBER.toString());

		ChatParticipant creator = room.getParticipants().stream()
				.filter(participant -> participant.getUserId().value().equals(creatorId)).findFirst().orElseThrow();
		ChatParticipant other = room.getParticipants().stream()
				.filter(participant -> participant.getUserId().value().equals(participantId)).findFirst().orElseThrow();

		assertEquals(List.of(owner.getId()), creator.getRoles());
		assertEquals(List.of(member.getId()), other.getRoles());
		assertTrue(owner.hasPermission(ChatPermissionList.CAN_MODIFY_ROLE));
		assertTrue(owner.getPriority() > member.getPriority());
	}

	@Test
	void listRolesReturnsOnlyGroupRolesOrderedByPriority() {
		ChatRoom room = groupRoom(defaultParticipants(), defaultRoles());
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByChatRoomId(chatRoomId)).thenReturn(List.of(memberRole(), ownerRole()));

		var result = new GetChatRolesUseCase(chatRoomRepository, chatRoleRepository)
				.execute(new GetChatRolesCommand(chatRoomId));

		assertEquals(List.of(ownerRoleId, memberRoleId), result.roles().stream().map(ChatRole::getId).toList());
	}

	@Test
	void listRolesRejectsDirectChatRooms() {
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(directRoom()));

		ChatRoleException exception = assertThrows(ChatRoleException.class,
				() -> new GetChatRolesUseCase(chatRoomRepository, chatRoleRepository)
						.execute(new GetChatRolesCommand(chatRoomId)));

		assertEquals(ErrorCode.INVALID_CHATROOM, exception.getErrorCode());
	}

	@Test
	void creatorCanCreateRole() {
		ChatRoom room = groupRoom(defaultParticipants(), defaultRoles());
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.existsByNameAndChatRoomId("Moderator", chatRoomId)).thenReturn(false);
		when(chatPermissionRepository.findAllById(Set.of(sendPermissionId.value())))
				.thenReturn(Set.of(sendPermission()));
		when(chatRoleRepository.save(any(ChatRole.class))).thenAnswer(invocation -> invocation.getArgument(0));

		var result = createRoleUseCase().execute(
				new CreateChatRoleCommand(chatRoomId, creatorUserId, "Moderator", 50, List.of(sendPermissionId)));

		assertEquals("Moderator", result.role().getName());
		verify(chatRoleRepository).save(any(ChatRole.class));
	}

	@Test
	void userWithCreateRolePermissionCanCreateRole() {
		ChatRoom room = groupRoom(defaultParticipants(), defaultRoles());
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requesterUserId))
				.thenReturn(Optional.of(participant(requesterParticipantId, requesterUserId, List.of(adminRoleId))));
		effectiveRequesterPermissions(createRolePermission());
		when(chatRoleRepository.existsByNameAndChatRoomId("Moderator", chatRoomId)).thenReturn(false);
		when(chatPermissionRepository.findAllById(Set.of(sendPermissionId.value())))
				.thenReturn(Set.of(sendPermission()));
		when(chatRoleRepository.save(any(ChatRole.class))).thenAnswer(invocation -> invocation.getArgument(0));

		createRoleUseCase().execute(
				new CreateChatRoleCommand(chatRoomId, requesterUserId, "Moderator", 50, List.of(sendPermissionId)));

		verify(chatRoleRepository).save(any(ChatRole.class));
	}

	@Test
	void userWithoutCreateRolePermissionCannotCreateRole() {
		ChatRoom room = groupRoom(defaultParticipants(), defaultRoles());
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requesterUserId))
				.thenReturn(Optional.of(participant(requesterParticipantId, requesterUserId, List.of(memberRoleId))));
		effectiveRequesterPermissions(sendPermission());

		ChatPermissionDeniedException exception = assertThrows(ChatPermissionDeniedException.class,
				() -> createRoleUseCase().execute(new CreateChatRoleCommand(chatRoomId, requesterUserId, "Moderator",
						50, List.of(sendPermissionId))));

		assertEquals(ErrorCode.CHAT_PERMISSION_DENIED, exception.getErrorCode());
		verify(chatRoleRepository, never()).save(any());
	}

	@Test
	void createRoleRejectsDuplicateName() {
		ChatRoom room = groupRoom(defaultParticipants(), defaultRoles());
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.existsByNameAndChatRoomId("Member", chatRoomId)).thenReturn(true);

		ChatRoleException exception = assertThrows(ChatRoleException.class, () -> createRoleUseCase().execute(
				new CreateChatRoleCommand(chatRoomId, creatorUserId, "Member", 10, List.of(sendPermissionId))));

		assertEquals(ErrorCode.CHAT_RULE_CONFLICT, exception.getErrorCode());
	}

	@Test
	void createRoleRejectsMissingPermissions() {
		ChatRoom room = groupRoom(defaultParticipants(), defaultRoles());
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.existsByNameAndChatRoomId("Moderator", chatRoomId)).thenReturn(false);
		when(chatPermissionRepository.findAllById(Set.of(sendPermissionId.value()))).thenReturn(Set.of());

		ChatRoleException exception = assertThrows(ChatRoleException.class, () -> createRoleUseCase().execute(
				new CreateChatRoleCommand(chatRoomId, creatorUserId, "Moderator", 50, List.of(sendPermissionId))));

		assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
	}

	@Test
	void creatorCanUpdateRole() {
		ChatRoom room = roomWithCustomRole();
		ChatRole customRole = customRole(Set.of(sendPermission()), 20);
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId)).thenReturn(Optional.of(customRole));
		when(chatPermissionRepository.findAllById(Set.of(sendPermissionId.value())))
				.thenReturn(Set.of(sendPermission()));
		when(chatRoleRepository.update(any(ChatRole.class))).thenAnswer(invocation -> invocation.getArgument(0));

		var result = updateRoleUseCase().execute(new UpdateChatRoleCommand(chatRoomId, customRoleId, creatorUserId,
				"Advanced", 30, List.of(sendPermissionId)));

		assertEquals("Advanced", result.role().getName());
		verify(chatRoleRepository).update(any(ChatRole.class));
	}

	@Test
	void updateRejectsRemovingRoleAdministrationPermissionsFromLastAdministrativeRole() {
		ChatRoom room = groupRoom(List.of(participant(creatorParticipantId, creatorUserId, List.of(ownerRoleId))),
				Map.of("OWNER", ownerRole()));
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(ownerRoleId, chatRoomId)).thenReturn(Optional.of(ownerRole()));
		when(chatPermissionRepository.findAllById(Set.of(sendPermissionId.value(), modifyRolePermissionId.value())))
				.thenReturn(Set.of(sendPermission(), modifyRolePermission()));

		ChatRoleException exception = assertThrows(ChatRoleException.class,
				() -> updateRoleUseCase().execute(new UpdateChatRoleCommand(chatRoomId, ownerRoleId, creatorUserId,
						"OWNER", 100, List.of(sendPermissionId, modifyRolePermissionId))));

		assertEquals(ErrorCode.CHAT_RULE_CONFLICT, exception.getErrorCode());
		verify(chatRoleRepository, never()).update(any());
	}

	@Test
	void userWithModifyRolePermissionCanUpdateLowerRole() {
		ChatRoom room = roomWithCustomRole();
		ChatRole customRole = customRole(Set.of(sendPermission()), 20);
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId)).thenReturn(Optional.of(customRole));
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requesterUserId))
				.thenReturn(Optional.of(participant(requesterParticipantId, requesterUserId, List.of(adminRoleId))));
		effectiveRequesterPermissions(modifyRolePermission());
		when(chatPermissionRepository.findAllById(Set.of(sendPermissionId.value())))
				.thenReturn(Set.of(sendPermission()));
		when(chatRoleRepository.update(any(ChatRole.class))).thenAnswer(invocation -> invocation.getArgument(0));

		updateRoleUseCase().execute(new UpdateChatRoleCommand(chatRoomId, customRoleId, requesterUserId, "Advanced", 30,
				List.of(sendPermissionId)));

		verify(chatRoleRepository).update(any(ChatRole.class));
	}

	@Test
	void userWithoutModifyRolePermissionCannotUpdateRole() {
		ChatRoom room = roomWithCustomRole();
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId))
				.thenReturn(Optional.of(customRole(Set.of(sendPermission()), 20)));
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requesterUserId))
				.thenReturn(Optional.of(participant(requesterParticipantId, requesterUserId, List.of(memberRoleId))));
		effectiveRequesterPermissions(sendPermission());

		ChatPermissionDeniedException exception = assertThrows(ChatPermissionDeniedException.class,
				() -> updateRoleUseCase().execute(new UpdateChatRoleCommand(chatRoomId, customRoleId, requesterUserId,
						"Advanced", 30, List.of(sendPermissionId))));

		assertEquals(ErrorCode.CHAT_PERMISSION_DENIED, exception.getErrorCode());
	}

	@Test
	void updateRoleRejectsRoleFromAnotherChatRoom() {
		ChatRoom room = roomWithCustomRole();
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId)).thenReturn(Optional.empty());

		ChatRoleException exception = assertThrows(ChatRoleException.class,
				() -> updateRoleUseCase().execute(new UpdateChatRoleCommand(chatRoomId, customRoleId, creatorUserId,
						"Advanced", 30, List.of(sendPermissionId))));

		assertEquals(ErrorCode.CHATROLE_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void creatorCanDeleteUnassignedRole() {
		ChatRoom room = roomWithCustomRole();
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId))
				.thenReturn(Optional.of(customRole(Set.of(sendPermission()), 20)));
		when(chatParticipantRepository.isRoleAssignedToAnyParticipant(chatRoomId, customRoleId)).thenReturn(false);
		when(chatRoleRepository.delete(customRoleId)).thenReturn(true);

		deleteRoleUseCase().execute(new DeleteChatRoleCommand(chatRoomId, customRoleId, creatorUserId, null));

		verify(chatRoleRepository).delete(customRoleId);
	}

	@Test
	void deleteTwiceReturnsNotFoundSecondTime() {
		ChatRoom room = roomWithCustomRole();
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId))
				.thenReturn(Optional.of(customRole(Set.of(sendPermission()), 20)), Optional.empty());
		when(chatParticipantRepository.isRoleAssignedToAnyParticipant(chatRoomId, customRoleId)).thenReturn(false);
		when(chatRoleRepository.delete(customRoleId)).thenReturn(true);

		deleteRoleUseCase().execute(new DeleteChatRoleCommand(chatRoomId, customRoleId, creatorUserId, null));
		ChatRoleException exception = assertThrows(ChatRoleException.class, () -> deleteRoleUseCase()
				.execute(new DeleteChatRoleCommand(chatRoomId, customRoleId, creatorUserId, null)));

		assertEquals(ErrorCode.CHATROLE_NOT_FOUND, exception.getErrorCode());
		verify(chatRoleRepository, times(1)).delete(customRoleId);
	}

	@Test
	void deleteExistingRoleFailsIfRepositoryDidNotDeleteARow() {
		ChatRoom room = roomWithCustomRole();
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId))
				.thenReturn(Optional.of(customRole(Set.of(sendPermission()), 20)));
		when(chatParticipantRepository.isRoleAssignedToAnyParticipant(chatRoomId, customRoleId)).thenReturn(false);
		when(chatRoleRepository.delete(customRoleId)).thenReturn(false);

		ChatRoleException exception = assertThrows(ChatRoleException.class, () -> deleteRoleUseCase()
				.execute(new DeleteChatRoleCommand(chatRoomId, customRoleId, creatorUserId, null)));

		assertEquals(ErrorCode.CHATROLE_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void userWithDeleteRolePermissionCanDeleteLowerRole() {
		ChatRoom room = roomWithCustomRole();
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId))
				.thenReturn(Optional.of(customRole(Set.of(sendPermission()), 20)));
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requesterUserId))
				.thenReturn(Optional.of(participant(requesterParticipantId, requesterUserId, List.of(adminRoleId))));
		effectiveRequesterPermissions(deleteRolePermission());
		when(chatParticipantRepository.isRoleAssignedToAnyParticipant(chatRoomId, customRoleId)).thenReturn(false);
		when(chatRoleRepository.delete(customRoleId)).thenReturn(true);

		deleteRoleUseCase().execute(new DeleteChatRoleCommand(chatRoomId, customRoleId, requesterUserId, null));

		verify(chatRoleRepository).delete(customRoleId);
	}

	@Test
	void userWithoutDeleteRolePermissionCannotDeleteRole() {
		ChatRoom room = roomWithCustomRole();
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId))
				.thenReturn(Optional.of(customRole(Set.of(sendPermission()), 20)));
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requesterUserId))
				.thenReturn(Optional.of(participant(requesterParticipantId, requesterUserId, List.of(memberRoleId))));
		effectiveRequesterPermissions(sendPermission());

		ChatPermissionDeniedException exception = assertThrows(ChatPermissionDeniedException.class,
				() -> deleteRoleUseCase()
						.execute(new DeleteChatRoleCommand(chatRoomId, customRoleId, requesterUserId, null)));

		assertEquals(ErrorCode.CHAT_PERMISSION_DENIED, exception.getErrorCode());
	}

	@Test
	void deleteRejectsLastAdministrativeRole() {
		ChatRoom room = groupRoom(List.of(participant(creatorParticipantId, creatorUserId, List.of(ownerRoleId))),
				Map.of("OWNER", ownerRole()));
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(ownerRoleId, chatRoomId)).thenReturn(Optional.of(ownerRole()));

		ChatRoleException exception = assertThrows(ChatRoleException.class, () -> deleteRoleUseCase()
				.execute(new DeleteChatRoleCommand(chatRoomId, ownerRoleId, creatorUserId, null)));

		assertEquals(ErrorCode.CHAT_RULE_CONFLICT, exception.getErrorCode());
	}

	@Test
	void deleteRejectsAssignedRoleWithoutReplacement() {
		ChatRoom room = roomWithCustomRoleAssigned();
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId))
				.thenReturn(Optional.of(customRole(Set.of(sendPermission()), 20)));
		when(chatParticipantRepository.isRoleAssignedToAnyParticipant(chatRoomId, customRoleId)).thenReturn(true);

		ChatRoleException exception = assertThrows(ChatRoleException.class, () -> deleteRoleUseCase()
				.execute(new DeleteChatRoleCommand(chatRoomId, customRoleId, creatorUserId, null)));

		assertEquals(ErrorCode.CHAT_RULE_CONFLICT, exception.getErrorCode());
		verify(chatParticipantRepository, never()).replaceRoleForParticipants(any(), any(), any());
	}

	@Test
	void deleteWithReplacementReassignsParticipants() {
		ChatRoom room = roomWithCustomRoleAssigned();
		ChatRole customRole = customRole(Set.of(sendPermission()), 20);
		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
		when(chatRoleRepository.findByIdAndChatRoomId(customRoleId, chatRoomId)).thenReturn(Optional.of(customRole));
		when(chatParticipantRepository.isRoleAssignedToAnyParticipant(chatRoomId, customRoleId)).thenReturn(true);
		when(chatRoleRepository.findByIdAndChatRoomId(memberRoleId, chatRoomId)).thenReturn(Optional.of(memberRole()));
		when(chatRoleRepository.delete(customRoleId)).thenReturn(true);

		deleteRoleUseCase().execute(new DeleteChatRoleCommand(chatRoomId, customRoleId, creatorUserId, memberRoleId));

		verify(chatParticipantRepository).replaceRoleForParticipants(chatRoomId, customRoleId, memberRoleId);
		verify(chatRoleRepository).delete(customRoleId);
	}

	@Test
	void syncMapperIncludesGroupRolesAndReturnsEmptyRolesForDirectChats() {
		ChatRoomResponse groupResponse = ChatRoomResponseMapper
				.toResponse(groupRoom(defaultParticipants(), defaultRoles()));
		ChatRoomResponse directResponse = ChatRoomResponseMapper.toResponse(directRoom());

		assertFalse(groupResponse.roles().isEmpty());
		assertTrue(directResponse.roles().isEmpty());
	}

	private CreateChatRoleUseCase createRoleUseCase() {
		return new CreateChatRoleUseCase(chatRoomRepository, chatRoleRepository, chatPermissionRepository,
				authorizationService());
	}

	private UpdateChatRoleUseCase updateRoleUseCase() {
		return new UpdateChatRoleUseCase(chatRoomRepository, chatRoleRepository, chatPermissionRepository,
				authorizationService());
	}

	private DeleteChatRoleUseCase deleteRoleUseCase() {
		return new DeleteChatRoleUseCase(chatRoomRepository, chatRoleRepository, chatParticipantRepository,
				authorizationService());
	}

	private ChatPermissionAuthorizationService authorizationService() {
		return new ChatPermissionAuthorizationService(chatRoomRepository, chatParticipantRepository,
				chatPermissionRepository);
	}

	private void effectiveRequesterPermissions(ChatPermission... permissions) {
		when(chatPermissionRepository.findPermissionsByChatRoomAndParticipant(chatRoomId, requesterParticipantId))
				.thenReturn(Set.of(permissions));
	}

	private Set<ChatPermission> permissionsByCodes(Set<String> codes) {
		return codes.stream().map(this::permissionByCode).collect(Collectors.toSet());
	}

	private ChatPermission permissionByCode(String code) {
		PermissionId permissionId = switch (code) {
			case ChatPermissionList.CAN_CREATE_ROLE -> createRolePermissionId;
			case ChatPermissionList.CAN_MODIFY_ROLE -> modifyRolePermissionId;
			case ChatPermissionList.CAN_DELETE_ROLE -> deleteRolePermissionId;
			default -> sendPermissionId;
		};
		return new ChatPermission(permissionId, code, code);
	}

	private ChatPermission sendPermission() {
		return new ChatPermission(sendPermissionId, ChatPermissionList.CAN_SEND_MESSAGE, "Allows sending messages");
	}

	private ChatPermission createRolePermission() {
		return new ChatPermission(createRolePermissionId, ChatPermissionList.CAN_CREATE_ROLE, "Allows creating roles");
	}

	private ChatPermission modifyRolePermission() {
		return new ChatPermission(modifyRolePermissionId, ChatPermissionList.CAN_MODIFY_ROLE, "Allows modifying roles");
	}

	private ChatPermission deleteRolePermission() {
		return new ChatPermission(deleteRolePermissionId, ChatPermissionList.CAN_DELETE_ROLE, "Allows deleting roles");
	}

	private ChatRole ownerRole() {
		return role(ownerRoleId, "OWNER",
				Set.of(sendPermission(), createRolePermission(), modifyRolePermission(), deleteRolePermission()), 100);
	}

	private ChatRole adminRole() {
		return role(adminRoleId, "ADMIN",
				Set.of(sendPermission(), createRolePermission(), modifyRolePermission(), deleteRolePermission()), 80);
	}

	private ChatRole memberRole() {
		return role(memberRoleId, "MEMBER", Set.of(sendPermission()), 10);
	}

	private ChatRole customRole(Set<ChatPermission> permissions, int priority) {
		return role(customRoleId, "CUSTOM", permissions, priority);
	}

	private ChatRole role(RoleId roleId, String name, Set<ChatPermission> permissions, int priority) {
		return ChatRole.reconstitute(roleId, chatRoomId, permissions, name, priority, Instant.now());
	}

	private Map<String, ChatRole> defaultRoles() {
		return Map.of("OWNER", ownerRole(), "ADMIN", adminRole(), "MEMBER", memberRole());
	}

	private ChatRoom roomWithCustomRole() {
		return groupRoom(defaultParticipants(), Map.of("OWNER", ownerRole(), "ADMIN", adminRole(), "MEMBER",
				memberRole(), "CUSTOM", customRole(Set.of(sendPermission()), 20)));
	}

	private ChatRoom roomWithCustomRoleAssigned() {
		return groupRoom(
				List.of(participant(creatorParticipantId, creatorUserId, List.of(ownerRoleId)),
						participant(targetParticipantId, targetUserId, List.of(customRoleId))),
				Map.of("OWNER", ownerRole(), "ADMIN", adminRole(), "MEMBER", memberRole(), "CUSTOM",
						customRole(Set.of(sendPermission()), 20)));
	}

	private List<ChatParticipant> defaultParticipants() {
		return List.of(participant(creatorParticipantId, creatorUserId, List.of(ownerRoleId)),
				participant(requesterParticipantId, requesterUserId, List.of(adminRoleId)),
				participant(targetParticipantId, targetUserId, List.of(memberRoleId)));
	}

	private ChatParticipant participant(ParticipantId participantId, UserId userId, List<RoleId> roleIds) {
		return ChatParticipant.reconstitute(participantId, chatRoomId, userId, roleIds, Instant.now(), null, null);
	}

	private ChatRoom groupRoom(List<ChatParticipant> participants, Map<String, ChatRole> roles) {
		return new ChatRoom(chatRoomId, ChatRoomType.GROUP, "Group", creatorUserId, participants, roles, Instant.now(),
				null, null);
	}

	private ChatRoom directRoom() {
		return new ChatRoom(chatRoomId, ChatRoomType.DIRECT, "Direct", creatorUserId, defaultParticipants(),
				defaultRoles(), Instant.now(), null, null);
	}
}
