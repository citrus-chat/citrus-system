package com.javaee2026.citruschat.shared.domain.constants;

public final class ErrorMessages {

	public static final String MESSAGE_CONTENT_CANNOT_BE_EMPTY = "Message content cannot be empty";

	private ErrorMessages() {
	}

	public static final String VALIDATION_ERROR = "Validation error";
	public static final String UNEXPECTED_ERROR = "Unexpected error";

	public static final String EMAIL_ALREADY_IN_USE = "Email already in use";
	public static final String USERNAME_ALREADY_IN_USE = "Username already in use";
	public static final String PHONE_NUMBER_ALREADY_IN_USE = "Phone number already in use";

	public static final String USER_NOT_FOUND = "User not found";
	public static final String USER_ID_CANNOT_BE_NULL = "UserId cannot be null";
	public static final String USER_ALREADY_ACTIVE = "User already active";
	public static final String USER_ALREADY_INACTIVE = "User already inactive";
	public static final String USER_ALREADY_VALIDATED = "User already validated";

	public static final String PASSWORD_HASH_CANNOT_BE_NULL = "Password hash cannot be null";
	public static final String CREATED_AT_CANNOT_BE_NULL = "createdAt cannot be null";
	public static final String UPDATED_AT_CANNOT_BE_NULL = "updatedAt cannot be null";

	public static final String EMAIL_CANNOT_BE_NULL = "Email cannot be null";
	public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
	public static final String INVALID_TEMPORARY_PASSWORD = "Invalid temporary password";

	public static final String NAME_CANNOT_BE_EMPTY = "Name cannot be empty";
	public static final String USERNAME_CANNOT_BE_NULL = "Username cannot be null";
	public static final String USERNAME_CANNOT_BE_EMPTY = "Username cannot be empty";
	public static final String USERNAME_INVALID_CHARACTERS = "Username can only contain lowercase letters, numbers and underscore";
	public static final String USERNAME_INVALID_BOUNDARY_UNDERSCORE = "Username cannot start or end with underscore";
	public static final String USERNAME_CONSECUTIVE_UNDERSCORES = "Username cannot contain consecutive underscores";

	public static final String PHONE_NUMBER_CANNOT_BE_NULL = "Phone number cannot be null";
	public static final String INVALID_PHONE_NUMBER_FORMAT = "Invalid phone number format";
	public static final String INVALID_CREDENTIALS = "Invalid credentials";

	public static final String MESSAGE_ID_NOT_FOUND = "Message id not found";
	public static final String MESSAGE_ID_CANNOT_BE_NULL = "Message cannot be null";
	public static final String MESSAGE_CANNOT_BE_EDITED = "Message cannot be edited";
	public static final String MESSAGE_ALREADY_DELETED = "Message already deleted";

	public static final String DEVICE_ID_NOT_FOUND = "Device id not found";
	public static final String DEVICE_ID_CANNOT_BE_NULL = "Device id cannot be null";

	public static final String CHATROOM_ID_NOT_FOUND = "Chatroom id not found";
	public static final String CHATROOM_ID_CANNOT_BE_NULL = "Chatroom id cannot be null";
	public static final String CHATROOM_CANNOT_BE_EDITED = "Chatroom cannot be edited";
	public static final String CHATROOM_ALREADY_DELETED = "Chatroom already deleted";
	public static final String CHATROOM_DOES_NOT_HAVE_ROLES = "Chatroom does not have roles";

	public static final String PARTICIPANT_ID_CANNOT_BE_NULL = "Participant id cannot be null";

	public static final String ROLE_ID_CANNOT_BE_NULL = "Role id cannot be null";
	public static final String ROLES_CANNOT_BE_NULL = "Roles cannot be null";

	public static String usernameLengthBetween(int min, int max) {
		return "Username must be between " + min + " and " + max + " characters";
	}

	public static String MESSAGE_CONTENT_TOO_LONG(int maxLength) {
		return "Message content cannot exceed " + maxLength + " characters";
	}
}
