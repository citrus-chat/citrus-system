package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.application.ports.IWebLoginTokenRepository;
import com.javaee2026.citruschat.identity.domain.model.WebLoginToken;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper.WebLoginTokenMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaWebLoginTokenRepositoryAdapter implements IWebLoginTokenRepository {

	private final SpringDataWebLoginTokenRepository repository;

	public JpaWebLoginTokenRepositoryAdapter(SpringDataWebLoginTokenRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean existsByTokenHash(String tokenHash) {
		return repository.existsByTokenHash(tokenHash);
	}

	@Override
	public Optional<WebLoginToken> findByTokenHashForUpdate(String tokenHash) {
		return repository.findByTokenHash(tokenHash).map(WebLoginTokenMapper::toDomain);
	}

	@Override
	public WebLoginToken save(WebLoginToken webLoginToken) {
		var saved = repository.save(WebLoginTokenMapper.toEntity(webLoginToken));
		return WebLoginTokenMapper.toDomain(saved);
	}
}
