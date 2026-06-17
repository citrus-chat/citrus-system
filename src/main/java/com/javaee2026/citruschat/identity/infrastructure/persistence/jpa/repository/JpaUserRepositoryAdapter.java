package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.domain.valueobjects.PhoneNumber;
import com.javaee2026.citruschat.identity.domain.valueobjects.UserEmail;
import com.javaee2026.citruschat.identity.domain.valueobjects.Username;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper.UserMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaUserRepositoryAdapter implements IUserRepository {

	private final SpringDataUserRepository repository;
	private final UserMapper userMapper;

	public JpaUserRepositoryAdapter(SpringDataUserRepository repository, UserMapper userMapper) {
		this.repository = repository;
		this.userMapper = userMapper;
	}

	@Override
	public Optional<User> findById(UserId id) {
		return repository.findById(id.value()).map(userMapper::toDomain);
	}

	@Override
	public Optional<User> findByEmail(UserEmail email) {
		return repository.findByEmail(email.getValue()).map(userMapper::toDomain);
	}

	@Override
	public Optional<User> findByUsername(Username username) {
		return repository.findByUsername(username.getValue()).map(userMapper::toDomain);
	}

	@Override
	public Optional<User> findByPhoneNumber(PhoneNumber phoneNumber) {
		return repository.findByPhoneNumber(phoneNumber.getValue()).map(userMapper::toDomain);
	}

	@Override
	public boolean existsByEmail(UserEmail email) {
		return repository.existsByEmail(email.getValue());
	}

	@Override
	public boolean existsByUsername(Username username) {
		return repository.existsByUsername(username.getValue());
	}

	@Override
	public boolean existsByPhoneNumber(PhoneNumber phoneNumber) {
		return repository.existsByPhoneNumber(phoneNumber.getValue());
	}

	@Override
	public List<User> search(String text, int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		return repository.search(text, pageable).stream().map(userMapper::toDomain).toList();
	}

	@Override
	public List<User> findAll(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		return repository.findAll(pageable).stream().map(userMapper::toDomain).toList();
	}

	@Override
	public User save(User user) {
		return userMapper.toDomain(repository.save(userMapper.toJpa(user)));
	}
}
