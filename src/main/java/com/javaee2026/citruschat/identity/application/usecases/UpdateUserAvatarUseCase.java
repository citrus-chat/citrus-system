package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.UpdateUserAvatarCommand;
import com.javaee2026.citruschat.identity.application.exceptions.InvalidAvatarException;
import com.javaee2026.citruschat.identity.application.exceptions.UserNotFoundException;
import com.javaee2026.citruschat.identity.application.ports.IUserAvatarStorage;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.StoredAvatarResult;
import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

public class UpdateUserAvatarUseCase {

	private final IUserRepository userRepository;
	private final IUserAvatarStorage avatarStorage;

	public UpdateUserAvatarUseCase(IUserRepository userRepository, IUserAvatarStorage avatarStorage) {
		this.userRepository = userRepository;
		this.avatarStorage = avatarStorage;
	}

	public UserResult execute(UpdateUserAvatarCommand command) {
		if (command.content() == null || command.content().length == 0) {
			throw new InvalidAvatarException("Avatar file is required");
		}

		User user = userRepository.findById(new UserId(command.userId())).orElseThrow(UserNotFoundException::new);
		String previousAvatarUrl = user.getAvatarUrl();
		StoredAvatarResult storedAvatar = avatarStorage.store(user.getId().value(), command.originalFilename(),
				command.contentType(), command.content());

		try {
			user.changeAvatarUrl(storedAvatar.avatarUrl());
			User savedUser = userRepository.save(user);
			avatarStorage.deleteByUrl(previousAvatarUrl);

			return UserResult.from(savedUser);
		} catch (RuntimeException ex) {
			avatarStorage.deleteByUrl(storedAvatar.avatarUrl());
			throw ex;
		}
	}
}
