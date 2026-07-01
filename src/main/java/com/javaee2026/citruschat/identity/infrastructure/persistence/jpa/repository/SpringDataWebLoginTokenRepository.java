package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.WebLoginTokenJpaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataWebLoginTokenRepository extends JpaRepository<WebLoginTokenJpaEntity, UUID> {

	boolean existsByTokenHash(String tokenHash);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<WebLoginTokenJpaEntity> findByTokenHash(String tokenHash);
}
