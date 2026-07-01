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

		User user = userRepository.findById(new UserId(userId)).orElseThrow(UserNotFoundException::new);

		return new UserResult(user.getId().value(), user.getUsername().getValue(), user.getEmail().getValue(),
				user.isActive(), user.getAvatarUrl());
	}
}
