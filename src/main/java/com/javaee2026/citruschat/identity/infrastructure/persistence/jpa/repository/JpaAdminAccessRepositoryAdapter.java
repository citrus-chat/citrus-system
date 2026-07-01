package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.application.ports.IAdminAccessRepository;
import com.javaee2026.citruschat.identity.domain.constants.AdminAccessPolicy;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JpaAdminAccessRepositoryAdapter implements IAdminAccessRepository {

	private final SpringDataUserOrganizationRepository repository;

	public JpaAdminAccessRepositoryAdapter(SpringDataUserOrganizationRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean userHasAdminAccess(UUID userId) {
		boolean bool = repository.hasAdminAccess(userId,
				AdminAccessPolicy.ADMIN_POSITION_NAME.toUpperCase(),
				AdminAccessPolicy.ADMIN_HIERARCHY_LEVEL);
		System.out.println("Admin check for " + userId + ": " + bool);
		return bool;
	}
}
