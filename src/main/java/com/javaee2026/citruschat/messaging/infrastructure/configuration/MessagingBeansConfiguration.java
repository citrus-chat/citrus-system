package com.javaee2026.citruschat.messaging.infrastructure.configuration;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.messaging.application.ports.*;
import com.javaee2026.citruschat.messaging.application.usecases.*;
import com.javaee2026.citruschat.messaging.domain.factory.*;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatPermissionMapper;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatRoleMapper;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatRoomMapper;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.MessageMapper;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository.*;

import com.javaee2026.citruschat.messaging.infrastructure.websocket.ports.IChatListRealtimeNotifier;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.ports.IMessageRealtimeNotifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingBeansConfiguration {

	@Bean
	public MessageFactory messageFactory() {
		return new MessageFactory();
	}

	@Bean
	public MessageMapper messageMapper(MessageFactory messageFactory) {
		return new MessageMapper(messageFactory);
	}

	@Bean
	public IMessageRepository messageRepository(SpringDataMessageRepository messageRepository,
			MessageMapper messageMapper) {
		return new JpaMessageRepositoryAdapter(messageRepository, messageMapper);
	}

	@Bean
	public IConversationKeyRequestRepository conversationKeyRequestRepository(
			SpringDataConversationKeyRequestRepository repository) {
		return new JpaConversationKeyRequestRepositoryAdapter(repository);
	};

	@Bean
	public RequestConversationKeyUseCase requestConversationKeyUseCase(IConversationKeyRequestRepository keyRepository,
			IChatParticipantRepository participantRepository, IChatRoomRepository chatRoomRepository) {
		return new RequestConversationKeyUseCase(keyRepository, participantRepository, chatRoomRepository);
	}

	@Bean
	public SendMessageUseCase sendMessageUseCase(IUserDeviceRepository deviceRepository, IUserRepository userRepository,
			IChatRoomRepository chatRoomRepository, IMessageRepository messageRepository, MessageFactory messageFactory,
			IMessageRealtimeNotifier messageRealtimeNotifier,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		return new SendMessageUseCase(deviceRepository, userRepository, chatRoomRepository, messageRepository,
				messageFactory, messageRealtimeNotifier, permissionAuthorizationService);
	}

	@Bean
	public SyncMessagesUseCase SyncMessagesUseCase(IMessageRepository messageRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		return new SyncMessagesUseCase(messageRepository, permissionAuthorizationService);
	}

	@Bean
	public ChatPermissionFactory chatPermissionFactory() {
		return new ChatPermissionFactory();
	}

	@Bean
	public ChatPermissionMapper chatPermissionMapper(ChatPermissionFactory chatPermissionFactory) {
		return new ChatPermissionMapper(chatPermissionFactory);
	}

	@Bean
	public ChatRoleMapper chatRoleMapper(ChatPermissionMapper chatPermissionMapper) {
		return new ChatRoleMapper(chatPermissionMapper);
	}

	@Bean
	public ChatRoomFactory chatRoomFactory() {
		return new ChatRoomFactory();
	}

	@Bean
	public ChatRoomMapper chatRoomMapper(ChatRoomFactory chatRoomFactory, ChatRoleMapper chatRoleMapper) {
		return new ChatRoomMapper(chatRoomFactory, chatRoleMapper);
	}

	@Bean
	public IChatRoomRepository chatRoomRepository(SpringDataChatRoomRepository springDataChatRoomRepository,
			ChatRoomMapper chatRoomMapper, SpingDataChatPermissionRepository chatPermissionRepository) {
		return new JpaChatRoomRepositoryAdapter(springDataChatRoomRepository, chatRoomMapper, chatPermissionRepository);
	}

	@Bean
	public IChatRoleRepository chatRoleRepository(SpringDataChatRoleRepository springDataChatRoleRepository,
			SpringDataChatRoomRepository springDataChatRoomRepository,
			SpingDataChatPermissionRepository chatPermissionRepository, ChatRoleMapper chatRoleMapper) {
		return new JpaChatRoleRepositoryAdapter(springDataChatRoleRepository, springDataChatRoomRepository,
				chatPermissionRepository, chatRoleMapper);
	}

	@Bean
	public IChatPermissionRepository chatPermissionRepository(
			SpingDataChatPermissionRepository chatPermissionRepository, ChatPermissionMapper chatPermissionMapper) {
		return new JpaChatPermissionRepositoryAdapter(chatPermissionRepository, chatPermissionMapper);
	}

	@Bean
	public CreateChatRoomUseCase createChatRoomUseCase(IChatRoomRepository chatRoomRepository,
			ChatRoomFactory chatRoomFactory, IUserRepository userRepository,
			IChatPermissionRepository permissionRepository, IChatListRealtimeNotifier realtimeNotifier) {
		return new CreateChatRoomUseCase(chatRoomRepository, chatRoomFactory, userRepository, permissionRepository,
				realtimeNotifier);
	}

	@Bean
	public UpdateChatRoomUseCase updateChatRoomUseCase(IChatRoomRepository chatRoomRepository) {
		return new UpdateChatRoomUseCase(chatRoomRepository);
	}

	@Bean
	public ValidateChatParticipantUseCase validateChatParticipantUseCase(
			IChatParticipantRepository chatParticipantRepository) {
		return new ValidateChatParticipantUseCase(chatParticipantRepository);
	}

	@Bean
	public GetCurrentUserChatRoomsUseCase getCurrentUserChatRoomsUseCase(IChatRoomRepository chatRoomRepository) {
		return new GetCurrentUserChatRoomsUseCase(chatRoomRepository);
	}

	@Bean
	public GetChatRoomMessagesUseCase getChatRoomMessagesUseCase(IMessageRepository messageRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		return new GetChatRoomMessagesUseCase(messageRepository, permissionAuthorizationService);
	}

	@Bean
	public IChatParticipantRepository chatParticipantRepository(
			SpringDataChatParticipantRepository springDataChatParticipantRepository,
			SpringDataChatRoleRepository springDataChatRoleRepository) {
		return new JpaChatParticipantRepositoryAdapter(springDataChatParticipantRepository,
				springDataChatRoleRepository);
	}

	@Bean
	public ChatPermissionAuthorizationService chatPermissionAuthorizationService(IChatRoomRepository chatRoomRepository,
			IChatParticipantRepository chatParticipantRepository, IChatPermissionRepository chatPermissionRepository) {
		return new ChatPermissionAuthorizationService(chatRoomRepository, chatParticipantRepository,
				chatPermissionRepository);
	}

	@Bean
	public SyncChatRoomUseCase syncChatRoomUseCase(IUserRepository userRepository,
			IChatRoomRepository chatRoomRepository, IUserDeviceRepository deviceRepository,
			IConversationKeyDistributionRepository conversationKeyRepository) {
		return new SyncChatRoomUseCase(chatRoomRepository, deviceRepository, userRepository, conversationKeyRepository);
	}

	@Bean
	public GetParticipantPermissionsUseCase getParticipantPermissionsUseCase(
			IChatPermissionRepository chatPermissionRepository,
			ValidateChatParticipantUseCase validateChatParticipantUseCase) {
		return new GetParticipantPermissionsUseCase(chatPermissionRepository, validateChatParticipantUseCase);
	}

	@Bean
	public UpdateParticipantRolesUseCase updateParticipantRolesUseCase(IChatRoomRepository chatRoomRepository,
			IChatParticipantRepository chatParticipantRepository, IChatPermissionRepository chatPermissionRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		return new UpdateParticipantRolesUseCase(chatRoomRepository, chatParticipantRepository,
				chatPermissionRepository, permissionAuthorizationService);
	}

	@Bean
	public GetChatRolesUseCase getChatRolesUseCase(IChatRoomRepository chatRoomRepository,
			IChatRoleRepository chatRoleRepository) {
		return new GetChatRolesUseCase(chatRoomRepository, chatRoleRepository);
	}

	@Bean
	public GetChatRoleUseCase getChatRoleUseCase(IChatRoomRepository chatRoomRepository,
			IChatRoleRepository chatRoleRepository) {
		return new GetChatRoleUseCase(chatRoomRepository, chatRoleRepository);
	}

	@Bean
	public CreateChatRoleUseCase createChatRoleUseCase(IChatRoomRepository chatRoomRepository,
			IChatRoleRepository chatRoleRepository, IChatPermissionRepository chatPermissionRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		return new CreateChatRoleUseCase(chatRoomRepository, chatRoleRepository, chatPermissionRepository,
				permissionAuthorizationService);
	}

	@Bean
	public UpdateChatRoleUseCase updateChatRoleUseCase(IChatRoomRepository chatRoomRepository,
			IChatRoleRepository chatRoleRepository, IChatPermissionRepository chatPermissionRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		return new UpdateChatRoleUseCase(chatRoomRepository, chatRoleRepository, chatPermissionRepository,
				permissionAuthorizationService);
	}

	@Bean
	public DeleteChatRoleUseCase deleteChatRoleUseCase(IChatRoomRepository chatRoomRepository,
			IChatRoleRepository chatRoleRepository, IChatParticipantRepository chatParticipantRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		return new DeleteChatRoleUseCase(chatRoomRepository, chatRoleRepository, chatParticipantRepository,
				permissionAuthorizationService);
	}

	@Bean
	public GetAvailableChatPermissionsUseCase getAvailableChatPermissionsUseCase(
			IChatPermissionRepository chatPermissionRepository) {
		return new GetAvailableChatPermissionsUseCase(chatPermissionRepository);
	}
}
