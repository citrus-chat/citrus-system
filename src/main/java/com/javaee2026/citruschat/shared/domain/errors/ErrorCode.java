package com.javaee2026.citruschat.shared.domain.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

	UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR), EMAIL_ALREADY_IN_USE(
			HttpStatus.CONFLICT), USERNAME_ALREADY_IN_USE(HttpStatus.CONFLICT), PHONE_NUMBER_ALREADY_IN_USE(
					HttpStatus.CONFLICT), INVALID_USERNAME(HttpStatus.BAD_REQUEST), INVALID_EMAIL(
							HttpStatus.BAD_REQUEST), INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST), INVALID_PERSON_NAME(
									HttpStatus.BAD_REQUEST), INVALID_USER(HttpStatus.BAD_REQUEST), INVALID_MESSAGE(
											HttpStatus.BAD_REQUEST), INVALID_TEMPORARY_PASSWORD(
													HttpStatus.BAD_REQUEST), INVALID_MESSAGE_CONTENT(
															HttpStatus.BAD_REQUEST), INVALID_CREDENTIALS(
																	HttpStatus.BAD_REQUEST), INVALID_PARTICIPANT(
																			HttpStatus.BAD_REQUEST), INVALID_CHATROOM(
																					HttpStatus.BAD_REQUEST), INVALID_DEVICE(
																							HttpStatus.BAD_REQUEST), INVALID_CHATROLE(
																									HttpStatus.BAD_REQUEST), USER_ALREADY_ACTIVE(
																											HttpStatus.CONFLICT), USER_ALREADY_INACTIVE(
																													HttpStatus.CONFLICT), USER_ALREADY_VALIDATED(
																															HttpStatus.CONFLICT), USER_NOT_FOUND(
																																	HttpStatus.NOT_FOUND);

	private final HttpStatus httpStatus;

	ErrorCode(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
