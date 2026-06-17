package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {

	Optional<UserJpaEntity> findByEmail(String email);

	Optional<UserJpaEntity> findByUsername(String username);

	Optional<UserJpaEntity> findByPhoneNumber(String phoneNumber);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByPhoneNumber(String phoneNumber);

	@Query("""
			    SELECT u FROM UserJpaEntity u
			    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :text, '%'))
			       OR LOWER(u.email) LIKE LOWER(CONCAT('%', :text, '%'))
			""")
	Page<UserJpaEntity> search(@Param("text") String text, Pageable pageable);

	Page<UserJpaEntity> findAll(Pageable pageable);
}
