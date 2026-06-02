package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.identity.domain.factory.UserFactory;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.domain.valueobjects.PhoneNumber;
import com.javaee2026.citruschat.identity.domain.valueobjects.UserEmail;
import com.javaee2026.citruschat.identity.domain.valueobjects.Username;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.springframework.stereotype.Component;

@Component
public final class UserMapper {

	private final UserFactory userFactory;

	public UserMapper(UserFactory userFactory) {
		this.userFactory = userFactory;
	}

	public User toDomain(UserJpaEntity entity) {
		return userFactory.reconstitute(new UserId(entity.getId()), new UserEmail(entity.getEmail()),
				new Username(entity.getUsername()), new PhoneNumber(entity.getPhoneNumber()), entity.getPasswordHash(),
				entity.getCreatedAt(), entity.getUpdatedAt(), entity.getDeletedAt(), entity.getValidatedAt());
	}

	public UserJpaEntity toJpa(User user) {
		UserJpaEntity entity = new UserJpaEntity();

		entity.setId(user.getId().value());
		entity.setEmail(user.getEmail().getValue());
		entity.setUsername(user.getUsername().getValue());
		entity.setPhoneNumber(user.getPhoneNumber().getValue());
		entity.setPasswordHash(user.getPasswordHash());

		entity.setCreatedAt(user.getCreatedAt());
		entity.setUpdatedAt(user.getUpdatedAt());
		entity.setValidatedAt(user.getValidatedAt());
		entity.setDeletedAt(user.getDeletedAt());

		return entity;
	}
}
