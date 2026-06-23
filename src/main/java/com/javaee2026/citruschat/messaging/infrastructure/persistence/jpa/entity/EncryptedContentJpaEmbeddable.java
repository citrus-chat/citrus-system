package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EncryptedContentJpaEmbeddable {

	@Column(name = "key_version", nullable = false)
	private Integer keyVersion;

	@Column(name = "iv", nullable = false, length = 1024)
	private String iv;

	@Lob
	@Column(name = "ciphertext", nullable = false)
	private String ciphertext;

	public EncryptedContentJpaEmbeddable(Integer keyVersion, String iv, String ciphertext) {
		this.keyVersion = keyVersion;
		this.iv = iv;
		this.ciphertext = ciphertext;
	}
}
