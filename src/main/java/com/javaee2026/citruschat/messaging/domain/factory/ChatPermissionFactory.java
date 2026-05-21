package com.javaee2026.citruschat.messaging.domain.factory;

import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;
import org.springframework.stereotype.Component;

@Component
public class ChatPermissionFactory {

	public ChatPermission createNew(String code, String description) {
		return new ChatPermission(PermissionId.newId(), code, description);
	}

	public ChatPermission reconstitute(PermissionId id, String code, String description) {
		return new ChatPermission(id, code, description);
	}

}
