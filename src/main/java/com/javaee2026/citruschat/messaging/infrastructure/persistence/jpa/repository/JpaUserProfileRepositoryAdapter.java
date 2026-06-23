package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.application.ports.IUserProfileRepository;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserProfileJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUserProfileRepositoryAdapter implements IUserProfileRepository {

    private final SpringDataUserProfileRepository repository;

    public JpaUserProfileRepositoryAdapter(SpringDataUserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserProfileJpaEntity save(UserProfileJpaEntity profile) {
        return repository.save(profile);
    }

    @Override
    public Optional<UserProfileJpaEntity> findByUserId(UUID userId) {
        return repository.findById(userId);
    }
}