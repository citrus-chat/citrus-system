package com.javaee2026.citruschat.identity.application.ports;

import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserProfileJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface IUserProfileRepository {
	UserProfileJpaEntity save(UserProfileJpaEntity profile);

	Optional<UserProfileJpaEntity> findByUserId(UUID userId);
}
