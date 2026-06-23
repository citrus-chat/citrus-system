package com.javaee2026.citruschat.identity.infrastructure.web.mapper;

import com.javaee2026.citruschat.identity.application.commands.UpdateUserProfileCommand;
import com.javaee2026.citruschat.identity.application.results.UserProfileResult;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.UpdateUserProfileRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserProfileResponse;

import java.util.UUID;

public final class UserProfileWebMapper {

    private UserProfileWebMapper() {
    }

    public static UpdateUserProfileCommand toCommand(UUID userId, UpdateUserProfileRequest req) {
        return new UpdateUserProfileCommand(
                userId,
                req.username(),
                req.description(),
                req.privacy(),
                req.showPhone(),
                req.showEmail(),
                req.showStatus(),
                req.showDescription(),
                req.allowGroupInvites());
    }

    public static UserProfileResponse toResponse(UserProfileResult result) {
        return new UserProfileResponse(
                result.userId(),
                result.username(),
                result.avatarUrl(),
                result.description(),
                result.privacy(),
                result.showPhone(),
                result.showEmail(),
                result.showStatus(),
                result.showDescription(),
                result.allowGroupInvites());
    }
}