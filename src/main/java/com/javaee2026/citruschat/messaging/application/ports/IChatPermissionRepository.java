package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;

import java.util.Set;
import java.util.UUID;

public interface IChatPermissionRepository {
	Set<ChatPermission> findByCodes(Set<String> codes);
	Set<ChatPermission> findAllById(Set<UUID> ids);
	void save(ChatPermission chatPermission);
}
