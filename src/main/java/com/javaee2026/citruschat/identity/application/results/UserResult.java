package com.javaee2026.citruschat.identity.application.results;

import com.javaee2026.citruschat.identity.domain.model.User;
import lombok.Getter;

import java.util.UUID;
@Getter
public class UserResult {

	private final UUID id;
	private final String username;
	private final String email;
	private final boolean active;

	public UserResult(UUID id, String username, String email, boolean active) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.active = active;
	}

	public static UserResult from(User user) {
		return new UserResult(user.getId().value(), user.getUsername().getValue(), user.getEmail().getValue(),
				user.isActive());
	}
}
