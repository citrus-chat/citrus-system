package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.exceptions.UserNotFoundException;
import com.javaee2026.citruschat.identity.application.ports.IUserAvatarStorage;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.UUID;

public class DeleteUserAvatarUseCase {

	private final IUserRepository userRepository;
	private final IUserAvatarStorage avatarStorage;

	public DeleteUserAvatarUseCase(IUserRepository userRepository, IUserAvatarStorage avatarStorage) {
		this.userRepository = userRepository;
		this.avatarStorage = avatarStorage;
	}

	public UserResult execute(UUID userId) {
		User user = userRepository.findById(new UserId(userId)).orElseThrow(UserNotFoundException::new);
		String previousAvatarUrl = user.getAvatarUrl();

		user.deleteAvatar();
		User savedUser = userRepository.save(user);
		avatarStorage.deleteByUrl(previousAvatarUrl);

		return UserResult.from(savedUser);
	}
}
