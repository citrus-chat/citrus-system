package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;

import java.util.List;
import java.util.Optional;

public interface IChatRoleRepository {
	Optional<ChatRole> findById(RoleId id);
	List<ChatRole> findGlobalRoles();

	Optional<ChatRole> ownerRole();
	Optional<ChatRole> memberRole();

	void save(ChatRole chatRole);
}
