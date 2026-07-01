package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatParticipantJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoleJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoomJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaChatParticipantRepositoryAdapterTest {

	@Mock
	private SpringDataChatParticipantRepository chatParticipantRepository;

	@Mock
	private SpringDataChatRoleRepository chatRoleRepository;

	private JpaChatParticipantRepositoryAdapter adapter;
	private UUID chatRoomUuid;
	private UUID participantUuid;
	private UUID oldRoleUuid;
	private UUID newRoleUuid;
	private ChatRoomJpaEntity chatRoom;
	private ChatParticipantJpaEntity participant;
	private ChatRoleJpaEntity oldRole;
	private ChatRoleJpaEntity newRole;

	@BeforeEach
	void setUp() {
		adapter = new JpaChatParticipantRepositoryAdapter(chatParticipantRepository, chatRoleRepository);
		chatRoomUuid = UUID.fromString("cccccccc-0000-0000-0000-000000000001");
		participantUuid = UUID.fromString("cccccccc-0000-0000-0000-000000000002");
		oldRoleUuid = UUID.fromString("cccccccc-0000-0000-0000-000000000003");
		newRoleUuid = UUID.fromString("cccccccc-0000-0000-0000-000000000004");
		chatRoom = chatRoom(chatRoomUuid);
		oldRole = role(oldRoleUuid, chatRoom);
		newRole = role(newRoleUuid, chatRoom);
		participant = participant(participantUuid, chatRoom, List.of(oldRole));
	}

	@Test
	void shouldReplacePreviousRoles() {
		stubParticipantAndRoles(List.of(newRole));

		adapter.replaceParticipantRoles(new ChatRoomId(chatRoomUuid), new ParticipantId(participantUuid),
				List.of(new RoleId(newRoleUuid)));

		assertEquals(List.of(newRole), participant.getRoles());
		verify(chatParticipantRepository).save(participant);
	}

	@Test
	void shouldNotDuplicateRoles() {
		stubParticipantAndRoles(List.of(newRole));

		RoleId roleId = new RoleId(newRoleUuid);
		adapter.replaceParticipantRoles(new ChatRoomId(chatRoomUuid), new ParticipantId(participantUuid),
				List.of(roleId, roleId));

		assertEquals(List.of(newRole), participant.getRoles());
		verify(chatRoleRepository).findAllById(List.of(newRoleUuid));
	}

	@Test
	void shouldRejectRolesFromAnotherChatRoom() {
		ChatRoleJpaEntity foreignRole = role(newRoleUuid,
				chatRoom(UUID.fromString("cccccccc-0000-0000-0000-000000000005")));

		stubParticipantAndRoles(List.of(foreignRole));

		assertThrows(IllegalArgumentException.class, () -> adapter.replaceParticipantRoles(new ChatRoomId(chatRoomUuid),
				new ParticipantId(participantUuid), List.of(new RoleId(newRoleUuid))));

		verify(chatParticipantRepository, never()).save(any());
	}

	private void stubParticipantAndRoles(List<ChatRoleJpaEntity> roles) {
		when(chatParticipantRepository.findByChatRoomIdAndIdAndLeftAtIsNull(chatRoomUuid, participantUuid))
				.thenReturn(Optional.of(participant));
		when(chatRoleRepository.findAllById(List.of(newRoleUuid))).thenReturn(roles);
	}

	private ChatRoomJpaEntity chatRoom(UUID id) {
		ChatRoomJpaEntity entity = new ChatRoomJpaEntity();
		entity.setId(id);
		entity.setName("Group");
		entity.setCreatedBy(UUID.randomUUID());
		entity.setCreatedAt(Instant.now());
		return entity;
	}

	private ChatParticipantJpaEntity participant(UUID id, ChatRoomJpaEntity chatRoom, List<ChatRoleJpaEntity> roles) {
		ChatParticipantJpaEntity entity = new ChatParticipantJpaEntity();
		entity.setId(id);
		entity.setChatRoom(chatRoom);
		entity.setUserId(UUID.randomUUID());
		entity.setRoles(new java.util.ArrayList<>(roles));
		entity.setJoinedAt(Instant.now());
		return entity;
	}

	private ChatRoleJpaEntity role(UUID id, ChatRoomJpaEntity chatRoom) {
		ChatRoleJpaEntity entity = new ChatRoleJpaEntity();
		entity.setId(id);
		entity.setChatRoom(chatRoom);
		entity.setName("role-" + id);
		entity.setPriority(1);
		entity.setCreatedAt(Instant.now());
		return entity;
	}
}
