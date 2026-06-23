package com.javaee2026.citruschat.messaging.domain.valueobjects;

import com.javaee2026.citruschat.messaging.domain.exceptions.InvalidMessageException;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class EncryptedContent {

	private final Integer keyVersion;
	private final String iv;
	private final String ciphertext;

	public EncryptedContent(Integer keyVersion, String iv, String ciphertext) {
		this.keyVersion = requireNonNull(keyVersion, ErrorMessages.KEY_VERSION_CANNOT_BE_NULL);
		this.iv = requireNonNull(iv, ErrorMessages.IV_CANNOT_BE_NULL);
		this.ciphertext = requireNonNull(ciphertext, ErrorMessages.CIPHERTEXT_CANNOT_BE_NULL);

		validate();
	}

	private void validate() {
		if (keyVersion < 1) {
			throw new InvalidMessageException(ErrorMessages.INVALID_KEY_VERSION);
		}

		if (iv.isBlank()) {
			throw new InvalidMessageException(ErrorMessages.IV_CANNOT_BE_EMPTY);
		}

		if (ciphertext.isBlank()) {
			throw new InvalidMessageException(ErrorMessages.CIPHERTEXT_CANNOT_BE_EMPTY);
		}
	}
}
