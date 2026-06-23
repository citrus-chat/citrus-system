package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.UpdateUserProfileCommand;
import com.javaee2026.citruschat.identity.application.ports.IUserProfileRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.UserProfileResult;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserProfileJpaEntity;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.domain.valueobjects.Username;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.time.Instant;

public class UpdateUserProfileUseCase {

    private final IUserProfileRepository userProfileRepository;
    private final IUserRepository userRepository;

    public UpdateUserProfileUseCase(IUserProfileRepository userProfileRepository, IUserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public UserProfileResult execute(UpdateUserProfileCommand command) {

        User user = userRepository
                .findById(new UserId(command.userId()))
                .orElseThrow();

        if (command.username() != null &&
                !command.username().isBlank()) {

            user.changeUsername(
                    new Username(command.username()));
        }

        userRepository.save(user);

        // Upsert: load existing or create new
        UserProfileJpaEntity profile = userProfileRepository.findByUserId(command.userId())
                .orElseGet(UserProfileJpaEntity::new);
        profile.setUserId(command.userId());
        profile.setDescription(command.description() != null ? command.description() : "");
        profile.setPrivacy(command.privacy() != null ? command.privacy() : "public");
        profile.setShowPhone(command.showPhone());
        profile.setShowEmail(command.showEmail());
        profile.setShowStatus(command.showStatus());
        profile.setShowDescription(command.showDescription());
        profile.setAllowGroupInvites(command.allowGroupInvites());
        profile.setUpdatedAt(Instant.now());
        profile = userProfileRepository.save(profile);

        return new UserProfileResult(
                user.getId().value(),
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