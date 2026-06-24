package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.dto.UserPageQuery;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.shared.application.results.PagedResult;

public class GetAdminUsersUseCase {

	private final IUserRepository userRepository;

	public GetAdminUsersUseCase(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public PagedResult<UserResult> execute(UserPageQuery query) {
		PagedResult<User> users = userRepository.findPage(query);

		return new PagedResult<>(users.items().stream().map(UserResult::from).toList(), users.total(),
				users.currentPage(), users.perPage(), users.lastPage(), users.hasNextPage(), users.hasPreviousPage());
	}
}
