package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchUsersUseCase {

	private final IUserRepository userRepository;

	public SearchUsersUseCase(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserResult> execute(String search, int page, int size) {

		List<User> users;

		if (search == null || search.isBlank()) {
			users = userRepository.findAll(page, size);
		} else {
			users = userRepository.search(search, page, size);
		}

		return users.stream().map(UserResult::from).toList();
	}
}
