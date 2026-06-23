package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserProfileRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.UserProfileResult;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserProfileJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.UUID;

public class GetUserProfileUseCase {

    private final IUserProfileRepository userProfileRepository;
    private final IUserRepository userRepository;

    public GetUserProfileUseCase(
            IUserProfileRepository userProfileRepository,
            IUserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public UserProfileResult execute(UUID userId) {

        User user = userRepository
                .findById(new UserId(userId))
                .orElseThrow();

        UserProfileJpaEntity profile = userProfileRepository
                .findByUserId(userId)
                .orElseGet(() -> {
                    UserProfileJpaEntity p = new UserProfileJpaEntity();
                    p.setUserId(userId);
                    p.setDescription("");
                    p.setPrivacy("public");
                    p.setShowPhone(true);
                    p.setShowEmail(true);
                    p.setShowStatus(true);
                    p.setShowDescription(true);
                    p.setAllowGroupInvites(true);
                    return p;
                });

        return new UserProfileResult(
                userId,
                user.getUsername().getValue(),
                user.getAvatarUrl(),

                profile.getDescription(),
                profile.getPrivacy(),

                profile.isShowPhone(),
                profile.isShowEmail(),
                profile.isShowStatus(),
                profile.isShowDescription(),
                profile.isAllowGroupInvites());
    }
}