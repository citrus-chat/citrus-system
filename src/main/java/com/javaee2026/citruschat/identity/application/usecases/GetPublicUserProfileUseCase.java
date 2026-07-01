package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.exceptions.UserNotFoundException;
import com.javaee2026.citruschat.identity.application.ports.IUserProfileRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.PublicUserProfileResult;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserOrganizationJpaEntity;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserProfileJpaEntity;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository.SpringDataUserOrganizationRepository;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository.SpringDataUserRepository;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.UUID;

/**
 * Devuelve el perfil de un usuario (targetId) visto por otro (requesterId).
 *
 * Reglas de privacidad: - Si el requester es el jefe del target (managerId ==
 * requesterId), ve todo. - Si no, los campos con show* = false aparecen como
 * null. - Los datos corporativos (posición, departamento, nivel, jefe) siempre
 * son visibles.
 */
public class GetPublicUserProfileUseCase {

	private final IUserRepository userRepository;
	private final IUserProfileRepository profileRepository;
	private final SpringDataUserOrganizationRepository orgRepository;
	private final SpringDataUserRepository userJpaRepository;

	public GetPublicUserProfileUseCase(IUserRepository userRepository, IUserProfileRepository profileRepository,
			SpringDataUserOrganizationRepository orgRepository, SpringDataUserRepository userJpaRepository) {
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
		this.orgRepository = orgRepository;
		this.userJpaRepository = userJpaRepository;
	}

	public PublicUserProfileResult execute(UUID targetId, UUID requesterId) {
		System.out.println("=== ENTRE AL PERFIL PUBLICO ===");
		// Obtener datos del usuario objetivo
		User target = userRepository.findById(new UserId(targetId)).orElseThrow(UserNotFoundException::new);

		// Perfil de privacidad (si no existe, se usan defaults abiertos)
		UserProfileJpaEntity profile = profileRepository.findByUserId(targetId).orElseGet(() -> {
			UserProfileJpaEntity p = new UserProfileJpaEntity();
			p.setUserId(targetId);
			p.setDescription("");
			p.setPrivacy("public");
			p.setShowPhone(true);
			p.setShowEmail(true);
			p.setShowStatus(true);
			p.setShowDescription(true);
			p.setAllowGroupInvites(true);
			return p;
		});

		// Organización del target
		UserOrganizationJpaEntity org = orgRepository.findByUserIdWithPosition(targetId).orElse(null);

		// El jefe siempre ve todo
		boolean isManager = org != null && org.getManagerId() != null && org.getManagerId().equals(requesterId);

		// Aplicar filtros de privacidad
		String phoneNumber = (isManager || profile.isShowPhone()) ? target.getPhoneNumber().getValue() : null;

		String email = (isManager || profile.isShowEmail()) ? target.getEmail().getValue() : null;

		String description = (isManager || profile.isShowDescription()) ? profile.getDescription() : null;

		// Estado: siempre "online" por ahora (cuando haya WebSocket real, se puede
		// actualizar)
		String status = (isManager || profile.isShowStatus()) ? "online" : null;

		// Datos de organización (siempre visibles)
		String positionName = null;
		String department = null;
		Integer hierarchyLevel = null;
		UUID managerId = null;
		String managerUsername = null;

		if (org != null) {
			System.out.println("ORG OK");

			System.out.println(org.getPosition());

			System.out.println("POSITION OK");

			positionName = org.getPosition().getName();

			System.out.println("NAME OK");
		}

		if (org != null) {
			if (org.getPosition() != null) {
				positionName = org.getPosition().getName();
				hierarchyLevel = org.getPosition().getHierarchyLevel();
			}
			managerId = org.getManagerId();

			if (managerId != null) {
				managerUsername = userJpaRepository.findById(managerId).map(u -> u.getUsername()).orElse(null);
			}
		}

		return new PublicUserProfileResult(targetId, target.getUsername().getValue(), target.getAvatarUrl(),
				phoneNumber, email, description, status, positionName, department, hierarchyLevel, managerId,
				managerUsername, profile.isShowPhone(), profile.isShowEmail(), profile.isShowStatus(),
				profile.isShowDescription());
	}
}
