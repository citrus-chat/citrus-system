package com.javaee2026.citruschat.identity.domain.model;

import com.javaee2026.citruschat.identity.domain.exceptions.InvalidUserException;
import com.javaee2026.citruschat.identity.domain.exceptions.UserAlreadyActiveException;
import com.javaee2026.citruschat.identity.domain.exceptions.UserAlreadyInactiveException;
import com.javaee2026.citruschat.identity.domain.exceptions.UserAlreadyValidatedException;
import com.javaee2026.citruschat.identity.domain.valueobjects.PhoneNumber;
import com.javaee2026.citruschat.identity.domain.valueobjects.UserEmail;
import com.javaee2026.citruschat.identity.domain.valueobjects.Username;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString(exclude = "passwordHash")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

	@EqualsAndHashCode.Include
	private final UserId id;

	private UserEmail email;
	private PhoneNumber phoneNumber;
	private Username username;
	private String passwordHash;
	private String avatarUrl;

	private final Instant createdAt;
	private Instant updatedAt;
	private Instant validatedAt;
	private Instant deletedAt;

	public User(UserId id, UserEmail email, Username username, PhoneNumber phoneNumber, String passwordHash,
			String avatarUrl, Instant createdAt, Instant updatedAt, Instant validatedAt, Instant deletedAt) {
		this.id = requireNonNull(id, ErrorMessages.USER_ID_CANNOT_BE_NULL);
		this.email = requireNonNull(email, ErrorMessages.EMAIL_CANNOT_BE_NULL);
		this.username = requireNonNull(username, ErrorMessages.USERNAME_CANNOT_BE_NULL);
		this.phoneNumber = requireNonNull(phoneNumber, ErrorMessages.PHONE_NUMBER_CANNOT_BE_NULL);
		this.passwordHash = requireNonNull(passwordHash, ErrorMessages.PASSWORD_HASH_CANNOT_BE_NULL);
		this.avatarUrl = avatarUrl;
		this.createdAt = requireNonNull(createdAt, ErrorMessages.CREATED_AT_CANNOT_BE_NULL);
		this.updatedAt = requireNonNull(updatedAt, ErrorMessages.UPDATED_AT_CANNOT_BE_NULL);
		this.validatedAt = validatedAt;
		this.deletedAt = deletedAt;
	}

	public boolean isActive() {
		return deletedAt == null && isValidated();
	}

	public boolean isValidated() {
		return validatedAt != null;
	}

	public void deactivate() {
		if (!isActive()) {
			throw new UserAlreadyInactiveException();
		}

		this.deletedAt = Instant.now();
		touch();
	}

	public void activate() {
		if (isActive()) {
			throw new UserAlreadyActiveException();
		}

		this.deletedAt = null;
		touch();
	}

	public void changeEmail(UserEmail newEmail) {
		this.email = requireNonNull(newEmail, ErrorMessages.EMAIL_CANNOT_BE_NULL);
		touch();
	}

	public void changePassword(String newPasswordHash) {
		this.passwordHash = requireNonNull(newPasswordHash, ErrorMessages.PASSWORD_HASH_CANNOT_BE_NULL);
		touch();
	}

	public void changePhoneNumber(PhoneNumber newPhoneNumber) {
		this.phoneNumber = requireNonNull(newPhoneNumber, ErrorMessages.PHONE_NUMBER_CANNOT_BE_NULL);
		touch();
	}

	public void changeUsername(Username newUsername) {
		this.username = requireNonNull(newUsername, ErrorMessages.USERNAME_CANNOT_BE_NULL);
		touch();
	}

	public void changeAvatarUrl(String newAvatarUrl) {
		if (newAvatarUrl == null || newAvatarUrl.isBlank()) {
			throw new InvalidUserException("Avatar URL cannot be blank");
		}

		this.avatarUrl = newAvatarUrl;
		touch();
	}

	public void deleteAvatar() {
		this.avatarUrl = null;
		touch();
	}

	public void validateAccount(final String newPasswordHash) {
		if (isValidated()) {
			throw new UserAlreadyValidatedException();
		}

		this.passwordHash = requireNonNull(newPasswordHash, ErrorMessages.PASSWORD_HASH_CANNOT_BE_NULL);
		this.validatedAt = Instant.now();
		touch();
	}

	private void touch() {
		this.updatedAt = Instant.now();
	}

	private <T> T requireNonNull(T value, String message) {
		if (value == null) {
			throw new InvalidUserException(message);
		}

		return value;
	}
}
