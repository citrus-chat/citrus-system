package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.messaging.application.commands.CreateChatRoomCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoleDefault;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.factory.ChatRoomFactory;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class CreateChatRoomUseCaseTest {

	private final IChatRoomRepository chatRoomRepository = mock(IChatRoomRepository.class);

	private final IUserRepository userRepository = mock(IUserRepository.class);

	private final IChatPermissionRepository permissionRepository = mock(IChatPermissionRepository.class);

	private final CreateChatRoomUseCase useCase = new CreateChatRoomUseCase(chatRoomRepository, new ChatRoomFactory(),
			userRepository, permissionRepository);

	@Test
	void shouldThrowExceptionWhenUserDoesNotExist() {

		UUID creatorId = UUID.randomUUID();
		UUID participantId = UUID.randomUUID();

		CreateChatRoomCommand command = new CreateChatRoomCommand(ChatRoomType.GROUP, "Test Group", creatorId,
				List.of(participantId));

		when(userRepository.findById(any(UserId.class))).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> useCase.execute(command));

		assertTrue(exception.getMessage().contains("does not exist"));

		verify(chatRoomRepository, never()).save(any());
	}

	@Test
	void shouldThrowExceptionWhenDirectChatHasMoreThanOneParticipant() {

		UUID creatorId = UUID.randomUUID();

		CreateChatRoomCommand command = new CreateChatRoomCommand(ChatRoomType.DIRECT, "Direct Chat", creatorId,
				List.of(UUID.randomUUID(), UUID.randomUUID()));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> useCase.execute(command));

		assertEquals("Direct chat rooms must have exactly one participant", exception.getMessage());

		verify(chatRoomRepository, never()).save(any());
	}

	@Test
	void shouldThrowExceptionWhenChatRoomTypeIsNull() {

		UUID creatorId = UUID.randomUUID();

		CreateChatRoomCommand command = new CreateChatRoomCommand(null, "Test Group", creatorId,
				List.of(UUID.randomUUID()));

		when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(mock(User.class)));

		NullPointerException exception = assertThrows(NullPointerException.class, () -> useCase.execute(command));

		assertEquals("ChatRoom type cannot be null", exception.getMessage());

		verify(chatRoomRepository, never()).save(any());
	}

	@Test
	void shouldThrowExceptionWhenCreatorIsNull() {

		CreateChatRoomCommand command = new CreateChatRoomCommand(ChatRoomType.GROUP, "Test Group", null,
				List.of(UUID.randomUUID()));

		assertThrows(Exception.class, () -> useCase.execute(command));

		verify(chatRoomRepository, never()).save(any());
	}

	@Test
	void shouldCreateGroupChatRoomSuccessfully() {

		UUID creatorId = UUID.randomUUID();
		UUID participantId1 = UUID.randomUUID();
		UUID participantId2 = UUID.randomUUID();

		CreateChatRoomCommand command = new CreateChatRoomCommand(ChatRoomType.GROUP, "Gaming Group", creatorId,
				List.of(participantId1, participantId2));

		when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(mock(User.class)));

		when(permissionRepository.findByCodes(anySet())).thenReturn(Set.of(mock(ChatPermission.class)));

		assertDoesNotThrow(() -> useCase.execute(command));

		ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);

		verify(chatRoomRepository).save(captor.capture());

		ChatRoom chatRoom = captor.getValue();

		assertNotNull(chatRoom.getId());

		assertEquals(ChatRoomType.GROUP, chatRoom.getType());
		assertEquals("Gaming Group", chatRoom.getName());
		assertEquals(creatorId, chatRoom.getCreatedBy().value());

		assertNotNull(chatRoom.getCreatedAt());

		assertEquals(3, chatRoom.getRoles().size());

		assertTrue(chatRoom.getRoles().containsKey(ChatRoleDefault.OWNER.toString()));

		assertTrue(chatRoom.getRoles().containsKey(ChatRoleDefault.ADMIN.toString()));

		assertTrue(chatRoom.getRoles().containsKey(ChatRoleDefault.MEMBER.toString()));

		List<ChatParticipant> participants = chatRoom.getParticipants();

		assertEquals(3, participants.size());

		assertTrue(participants.stream().anyMatch(p -> p.getUserId().value().equals(creatorId)));

		assertTrue(participants.stream().anyMatch(p -> p.getUserId().value().equals(participantId1)));

		assertTrue(participants.stream().anyMatch(p -> p.getUserId().value().equals(participantId2)));
	}

	@Test
	void shouldCreateDirectChatRoomSuccessfully() {

		UUID creatorId = UUID.randomUUID();
		UUID participantId = UUID.randomUUID();

		CreateChatRoomCommand command = new CreateChatRoomCommand(ChatRoomType.DIRECT, "Private Chat", creatorId,
				List.of(participantId));

		when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(mock(User.class)));

		when(permissionRepository.findByCodes(anySet())).thenReturn(Set.of(mock(ChatPermission.class)));

		assertDoesNotThrow(() -> useCase.execute(command));

		ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);

		verify(chatRoomRepository).save(captor.capture());

		ChatRoom chatRoom = captor.getValue();

		assertEquals(ChatRoomType.DIRECT, chatRoom.getType());

		assertEquals(2, chatRoom.getParticipants().size());

		List<ChatParticipant> participants = chatRoom.getParticipants();

		assertTrue(participants.stream().anyMatch(p -> p.getUserId().value().equals(creatorId)));

		assertTrue(participants.stream().anyMatch(p -> p.getUserId().value().equals(participantId)));
	}

	@Test
	void shouldInitializeDefaultRolesSuccessfully() {

		UUID creatorId = UUID.randomUUID();

		CreateChatRoomCommand command = new CreateChatRoomCommand(ChatRoomType.GROUP, "Roles Test", creatorId,
				List.of(UUID.randomUUID()));

		when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(mock(User.class)));

		when(permissionRepository.findByCodes(anySet())).thenReturn(Set.of(mock(ChatPermission.class)));

		useCase.execute(command);

		ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);

		verify(chatRoomRepository).save(captor.capture());

		ChatRoom room = captor.getValue();

		assertEquals(3, room.getRoles().size());

		assertNotNull(room.getRoles().get(ChatRoleDefault.OWNER.toString()));
		assertNotNull(room.getRoles().get(ChatRoleDefault.ADMIN.toString()));
		assertNotNull(room.getRoles().get(ChatRoleDefault.MEMBER.toString()));
	}

	@Test
	void shouldAddCreatorAsOwnerParticipant() {

		UUID creatorId = UUID.randomUUID();

		CreateChatRoomCommand command = new CreateChatRoomCommand(ChatRoomType.GROUP, "Owner Test", creatorId,
				List.of(UUID.randomUUID()));

		when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(mock(User.class)));

		when(permissionRepository.findByCodes(anySet())).thenReturn(Set.of(mock(ChatPermission.class)));

		useCase.execute(command);

		ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);

		verify(chatRoomRepository).save(captor.capture());

		ChatRoom room = captor.getValue();

		ChatParticipant creatorParticipant = room.getParticipants().stream()
				.filter(p -> p.getUserId().value().equals(creatorId)).findFirst().orElse(null);

		assertNotNull(creatorParticipant);
	}
}
