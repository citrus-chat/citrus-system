package com.javaee2026.citruschat.identity.application.ports;

import com.javaee2026.citruschat.identity.application.results.StoredAvatarResult;

import java.util.UUID;

public interface IUserAvatarStorage {
	StoredAvatarResult store(UUID userId, String originalFilename, String contentType, byte[] content);

	void deleteByUrl(String avatarUrl);
}
