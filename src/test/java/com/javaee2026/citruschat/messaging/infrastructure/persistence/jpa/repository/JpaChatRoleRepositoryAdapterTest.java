package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaChatRoleRepositoryAdapterTest {

	@Mock
	private SpringDataChatRoleRepository chatRoleRepository;

	private JpaChatRoleRepositoryAdapter adapter;
	private RoleId roleId;

	@BeforeEach
	void setUp() {
		adapter = new JpaChatRoleRepositoryAdapter(chatRoleRepository, null, null, null);
		roleId = new RoleId(UUID.fromString("eeeeeeee-0000-0000-0000-000000000001"));
	}

	@Test
	void deleteClearsJoinRowsBeforeDeletingRole() {
		when(chatRoleRepository.deleteExistingById(roleId.value())).thenReturn(1);

		assertTrue(adapter.delete(roleId));

		InOrder ordered = inOrder(chatRoleRepository);
		ordered.verify(chatRoleRepository).deleteRolePermissionsByRoleId(roleId.value());
		ordered.verify(chatRoleRepository).deleteParticipantRolesByRoleId(roleId.value());
		ordered.verify(chatRoleRepository).deleteExistingById(roleId.value());
	}

	@Test
	void deleteReturnsFalseWhenRoleRowDoesNotExist() {
		when(chatRoleRepository.deleteExistingById(roleId.value())).thenReturn(0);

		assertFalse(adapter.delete(roleId));
	}
}
