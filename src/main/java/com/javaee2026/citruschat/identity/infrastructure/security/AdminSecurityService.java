package com.javaee2026.citruschat.identity.infrastructure.security;

import com.javaee2026.citruschat.identity.application.usecases.CheckAdminAccessUseCase;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("adminSecurityService")
public class AdminSecurityService {

	private final CheckAdminAccessUseCase useCase;

	public AdminSecurityService(CheckAdminAccessUseCase useCase) {
		this.useCase = useCase;
	}

	public boolean isAdmin(Authentication authentication) {
		if (authentication == null)
			return false;
		if (!authentication.isAuthenticated())
			return false;

		try {
			UUID userId = UUID.fromString(authentication.getName());
			return useCase.execute(userId);
		} catch (IllegalArgumentException ex) {
			System.out.println("Failed to parse userId: " + ex.getMessage());
			return false;
		}
	}
}
