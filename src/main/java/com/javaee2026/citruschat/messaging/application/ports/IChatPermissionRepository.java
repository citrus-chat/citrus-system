package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;

import java.util.Set;

public interface IChatPermissionRepository {
	Set<ChatPermission> findByCodes(Set<String> codes);
	void save(ChatPermission chatPermission);
}
