package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.identity.domain.model.WebLoginToken;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.WebLoginTokenJpaEntity;

public final class WebLoginTokenMapper {

	private WebLoginTokenMapper() {
	}

	public static WebLoginToken toDomain(WebLoginTokenJpaEntity entity) {
		return WebLoginToken.reconstitute(entity.getId(), entity.getTokenHash(), entity.getWebDeviceId(),
				entity.getWebDeviceName(), entity.getWebPublicKey(), entity.getStatus(), entity.getExpiresAt(),
				entity.getUsedAt(), entity.getCreatedAt(), entity.getUpdatedAt());
	}

	public static WebLoginTokenJpaEntity toEntity(WebLoginToken webLoginToken) {
		return new WebLoginTokenJpaEntity(webLoginToken.getId(), webLoginToken.getTokenHash(),
				webLoginToken.getWebDeviceId(), webLoginToken.getWebDeviceName(), webLoginToken.getWebPublicKey(),
				webLoginToken.getStatus(), webLoginToken.getExpiresAt(), webLoginToken.getUsedAt(),
				webLoginToken.getCreatedAt(), webLoginToken.getUpdatedAt());
	}
}
