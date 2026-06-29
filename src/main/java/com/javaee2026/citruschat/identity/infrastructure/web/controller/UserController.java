package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.results.UserDeviceKeysResult;
import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.application.usecases.GetUserDeviceKeysUseCase;
import com.javaee2026.citruschat.identity.application.usecases.SearchUsersUseCase;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserOrganizationJpaEntity;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository.SpringDataUserOrganizationRepository;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository.SpringDataUserRepository;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.OrgUserResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserDeviceKeysResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.mapper.UserWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class UserController {

	private final SearchUsersUseCase searchUsersUseCase;
	private final GetUserDeviceKeysUseCase getUserDeviceKeysUseCase;
	private final SpringDataUserRepository userRepository;
	private final SpringDataUserOrganizationRepository orgRepository;

	public UserController(SearchUsersUseCase searchUsersUseCase, GetUserDeviceKeysUseCase getUserDeviceKeysUseCase,
			SpringDataUserRepository userRepository, SpringDataUserOrganizationRepository orgRepository) {
		this.searchUsersUseCase = searchUsersUseCase;
		this.getUserDeviceKeysUseCase = getUserDeviceKeysUseCase;
		this.userRepository = userRepository;
		this.orgRepository = orgRepository;
	}

	@GetMapping(ApiRoutes.API_USERS_SEARCH)
	public ResponseEntity<ApiResponse<List<UserResponse>>> list(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		List<UserResult> results = searchUsersUseCase.execute(search, page, size);

		return ApiResponses.ok(ApiResponseMessages.USER_LIST_RETRIEVED_SUCCESS, UserWebMapper.toResponseList(results));
	}

	@GetMapping(ApiRoutes.API_USER_KEYS)
	public ResponseEntity<ApiResponse<UserDeviceKeysResponse>> getKeys(@PathVariable UUID userId) {

		UserDeviceKeysResult result = getUserDeviceKeysUseCase.execute(userId);

		return ApiResponses.ok(ApiResponseMessages.USER_KEYS_RETRIEVED_SUCCESS,
				UserWebMapper.toUserDeviceKeysResponse(result));
	}

	/**
	 * Devuelve todos los usuarios activos con sus datos de organización para
	 * construir el organigrama en el frontend. GET /api/v1/users/org
	 */
	@GetMapping(ApiRoutes.API_USERS_ORG)
	public ResponseEntity<ApiResponse<List<OrgUserResponse>>> getOrgUsers() {
		// Cargar todos los registros de organización indexados por userId
		Map<UUID, UserOrganizationJpaEntity> orgByUserId = orgRepository.findAll().stream()
				.collect(Collectors.toMap(UserOrganizationJpaEntity::getUserId, o -> o, (a, b) -> a));

		// Mapear cada usuario activo con sus datos de organización
		List<OrgUserResponse> result = userRepository.findAll().stream().filter(u -> u.getDeletedAt() == null)
				.map(u -> {
					UserOrganizationJpaEntity org = orgByUserId.get(u.getId());
					return new OrgUserResponse(u.getId(), u.getUsername(), u.getAvatarUrl(),
							org != null && org.getPosition() != null ? org.getPosition().getName() : null,
							org != null && org.getPosition() != null ? org.getPosition().getHierarchyLevel() : null,
							org != null ? org.getManagerId() : null);
				}).collect(Collectors.toList());

		return ApiResponses.ok("Organigrama obtenido correctamente", result);
	}

}
