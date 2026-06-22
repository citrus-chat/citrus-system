package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.commands.UpdateUserAvatarCommand;
import com.javaee2026.citruschat.identity.application.exceptions.InvalidAvatarException;
import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.application.usecases.DeleteUserAvatarUseCase;
import com.javaee2026.citruschat.identity.application.usecases.UpdateUserAvatarUseCase;
import com.javaee2026.citruschat.identity.infrastructure.storage.LocalUserAvatarStorage;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.AvatarResponse;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class UserAvatarController {

	private final UpdateUserAvatarUseCase updateUserAvatarUseCase;
	private final DeleteUserAvatarUseCase deleteUserAvatarUseCase;
	private final LocalUserAvatarStorage avatarStorage;

	public UserAvatarController(UpdateUserAvatarUseCase updateUserAvatarUseCase,
			DeleteUserAvatarUseCase deleteUserAvatarUseCase, LocalUserAvatarStorage avatarStorage) {
		this.updateUserAvatarUseCase = updateUserAvatarUseCase;
		this.deleteUserAvatarUseCase = deleteUserAvatarUseCase;
		this.avatarStorage = avatarStorage;
	}

	@PutMapping(value = ApiRoutes.API_USER_ME_AVATAR, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<AvatarResponse>> updateAvatar(@AuthenticationPrincipal Jwt jwt,
			@RequestParam("file") MultipartFile file) {
		try {
			UserResult result = updateUserAvatarUseCase
					.execute(new UpdateUserAvatarCommand(UUID.fromString(jwt.getSubject()), file.getOriginalFilename(),
							file.getContentType(), file.getBytes()));

			return ApiResponses.ok(ApiResponseMessages.USER_AVATAR_UPDATED_SUCCESS,
					new AvatarResponse(result.getAvatarUrl()));
		} catch (IOException ex) {
			throw new InvalidAvatarException("Avatar file could not be read");
		}
	}

	@DeleteMapping(ApiRoutes.API_USER_ME_AVATAR)
	public ResponseEntity<ApiResponse<AvatarResponse>> deleteAvatar(@AuthenticationPrincipal Jwt jwt) {
		UserResult result = deleteUserAvatarUseCase.execute(UUID.fromString(jwt.getSubject()));

		return ApiResponses.ok(ApiResponseMessages.USER_AVATAR_DELETED_SUCCESS,
				new AvatarResponse(result.getAvatarUrl()));
	}

	@GetMapping(ApiRoutes.API_USER_AVATAR_IMAGE)
	public ResponseEntity<Resource> getAvatar(@PathVariable String filename) {
		Resource resource = avatarStorage.loadAsResource(filename);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(avatarStorage.contentType(filename)))
				.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic()).body(resource);
	}
}
