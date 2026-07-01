package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.exceptions.ChatParticipantRolesException;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatPermissionDeniedException;
import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatPermissionAuthorizationServiceTest {

	private final ChatRoomId chatRoomId = new ChatRoomId(UUID.fromString("ffffffff-0000-0000-0000-000000000001"));
	private final UserId creatorUserId = new UserId(UUID.fromString("ffffffff-0000-0000-0000-000000000002"));
	private final UserId requesterUserId = new UserId(UUID.fromString("ffffffff-0000-0000-0000-000000000003"));
	private final ParticipantId participantId = new ParticipantId(
			UUID.fromString("ffffffff-0000-0000-0000-000000000004"));
	private final RoleId roleId = new RoleId(UUID.fromString("ffffffff-0000-0000-0000-000000000005"));

	@Mock
	private IChatRoomRepository chatRoomRepository;

	@Mock
	private IChatParticipantRepository chatParticipantRepository;

	@Mock
	private IChatPermissionRepository chatPermissionRepository;

	private ChatPermissionAuthorizationService service;
	private ChatRoom chatRoom;
	private ChatParticipant participant;

	@BeforeEach
	void setUp() {
		service = new ChatPermissionAuthorizationService(chatRoomRepository, chatParticipantRepository,
				chatPermissionRepository);
		participant = ChatParticipant.reconstitute(participantId, chatRoomId, requesterUserId, List.of(roleId),
				Instant.now(), null, null);
		chatRoom = new ChatRoom(chatRoomId, ChatRoomType.GROUP, "Group", creatorUserId, List.of(participant),
				Map.of("ROLE", role(ChatPermissionList.CAN_VIEW_MESSAGE)), Instant.now(), null, null);
	}

	@ParameterizedTest
	@MethodSource("allPermissionCodes")
	void requirePermissionAllowsEachEffectivePermission(String permissionCode) {
		stubEffectivePermissions(permissionCode);

		assertDoesNotThrow(() -> service.requirePermission(chatRoom, requesterUserId, permissionCode));
	}

	@Test
	void requirePermissionRejectsMissingPermission() {
		stubEffectivePermissions(ChatPermissionList.CAN_VIEW_MESSAGE);

		ChatPermissionDeniedException exception = assertThrows(ChatPermissionDeniedException.class,
				() -> service.requirePermission(chatRoom, requesterUserId, ChatPermissionList.CAN_SEND_MESSAGE));

		assertEquals(ErrorCode.CHAT_PERMISSION_DENIED, exception.getErrorCode());
	}

	@Test
	void requirePermissionRejectsInactiveParticipantEvenIfTheyHadRoles() {
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requesterUserId))
				.thenReturn(Optional.empty());

		ChatParticipantRolesException exception = assertThrows(ChatParticipantRolesException.class,
				() -> service.requirePermission(chatRoom, requesterUserId, ChatPermissionList.CAN_VIEW_MESSAGE));

		assertEquals(ErrorCode.FORBIDDEN, exception.getErrorCode());
		verify(chatPermissionRepository, never()).findPermissionsByChatRoomAndParticipant(chatRoomId, participantId);
	}

	@Test
	void creatorBypassDoesNotReadEffectivePermissions() {
		assertDoesNotThrow(
				() -> service.requirePermissionOrCreator(chatRoom, creatorUserId, ChatPermissionList.CAN_DELETE_ROLE));

		verifyNoInteractions(chatParticipantRepository, chatPermissionRepository);
	}

	private void stubEffectivePermissions(String permissionCode) {
		when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId, requesterUserId))
				.thenReturn(Optional.of(participant));
		when(chatPermissionRepository.findPermissionsByChatRoomAndParticipant(chatRoomId, participantId))
				.thenReturn(Set.of(permission(permissionCode)));
	}

	private ChatRole role(String permissionCode) {
		return ChatRole.reconstitute(roleId, chatRoomId, Set.of(permission(permissionCode)), "ROLE", 10, Instant.now());
	}

	private ChatPermission permission(String code) {
		return new ChatPermission(new PermissionId(UUID.nameUUIDFromBytes(code.getBytes(StandardCharsets.UTF_8))), code,
				code);
	}

	private static Stream<String> allPermissionCodes() {
		return ChatPermissionList.ALL.stream().sorted(Comparator.naturalOrder());
	}
}
