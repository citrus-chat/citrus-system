package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.exceptions.UserNotFoundException;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.UUID;

public class GetUserUseCase {

	private IUserRepository userRepository;

	public GetUserUseCase(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserResult execute(UUID userId) {

		System.out.println("Searching for user with id: " + userId.toString());

		User user = userRepository.findById(new UserId(userId)).orElseThrow(UserNotFoundException::new);

		System.out.println("User found: " + user.getUsername().getValue());

		return new UserResult(user.getId().value(), user.getUsername().getValue(), user.getEmail().getValue(),
				user.isActive(), user.getAvatarUrl());
	}
}
