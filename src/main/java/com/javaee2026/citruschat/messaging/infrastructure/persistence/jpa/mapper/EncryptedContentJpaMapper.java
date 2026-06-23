package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.messaging.domain.valueobjects.EncryptedContent;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.EncryptedContentJpaEmbeddable;

public final class EncryptedContentJpaMapper {

	private EncryptedContentJpaMapper() {
	}

	public static EncryptedContentJpaEmbeddable toEmbeddable(EncryptedContent content) {
		return new EncryptedContentJpaEmbeddable(content.getKeyVersion(), content.getIv(), content.getCiphertext());
	}

	public static EncryptedContent toDomain(EncryptedContentJpaEmbeddable embeddable) {
		return new EncryptedContent(embeddable.getKeyVersion(), embeddable.getIv(), embeddable.getCiphertext());
	}
}
