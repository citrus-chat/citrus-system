package com.javaee2026.citruschat.identity.application.ports;

import com.javaee2026.citruschat.identity.domain.model.WebLoginToken;

import java.util.Optional;

public interface IWebLoginTokenRepository {

	boolean existsByTokenHash(String tokenHash);

	Optional<WebLoginToken> findByTokenHashForUpdate(String tokenHash);

	WebLoginToken save(WebLoginToken webLoginToken);
}
