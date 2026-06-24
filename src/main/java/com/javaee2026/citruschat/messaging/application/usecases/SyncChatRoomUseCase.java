package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.ports.IConversationKeyDistributionRepository;
import com.javaee2026.citruschat.messaging.application.results.SyncChatRoomResult;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.time.Instant;

public class SyncChatRoomUseCase {

	private final IChatRoomRepository chatRoomRepository;
	private final IUserDeviceRepository deviceRepository;
	private final IUserRepository userRepository;
	private final IConversationKeyDistributionRepository conversationKeyRepository;

	public SyncChatRoomUseCase(IChatRoomRepository chatRoomRepository, IUserDeviceRepository deviceRepository,
			IUserRepository userRepository, IConversationKeyDistributionRepository conversationKeyRepository) {
		this.chatRoomRepository = chatRoomRepository;
		this.deviceRepository = deviceRepository;
		this.userRepository = userRepository;
		this.conversationKeyRepository = conversationKeyRepository;
	}

	public SyncChatRoomResult execute(DeviceId deviceId) {

		UserDevice device = deviceRepository.findActiveById(deviceId.value())
				.orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + deviceId.value()));

		Instant since = device.getLastSync();
		UserId userId = device.getUserId();

		if (since == null) {
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId.value()));

			since = user.getCreatedAt();
		}

		if (since.isAfter(Instant.now())) {
			throw new IllegalArgumentException("Invalid since date: " + since);
		}

		var chatRooms = chatRoomRepository.findUpdatedChatRooms(userId, since);

		var conversationKeys = conversationKeyRepository.findByTargetDeviceAndCreatedAfter(deviceId, since);

		device.sync();
		deviceRepository.save(device);

		return new SyncChatRoomResult(chatRooms, conversationKeys);
	}
}
