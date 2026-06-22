package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.exceptions.UserNotFoundException;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.UUID;

public class GetCurrentUserUseCase {

	private final IUserRepository userRepository;

	public GetCurrentUserUseCase(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserResult execute(UUID userId) {
		return userRepository.findById(new UserId(userId)).map(UserResult::from)
				.orElseThrow(UserNotFoundException::new);
	}
}
