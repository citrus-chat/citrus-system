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
	public SendMessageUseCase sendMessageUseCase(IUserDeviceRepository deviceRepository, IUserRepository userRepository,
			IChatRoomRepository chatRoomRepository, IMessageRepository messageRepository,
			MessageFactory messageFactory) {
		return new SendMessageUseCase(deviceRepository, userRepository, chatRoomRepository, messageRepository,
				messageFactory);
	}

	@Bean
	public SyncMessagesUseCase SyncMessagesUseCase(IMessageRepository messageRepository,
			IChatRoomRepository chatRoomRepository) {
		return new SyncMessagesUseCase(messageRepository, chatRoomRepository);
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
	public IChatPermissionRepository chatPermissionRepository(
			SpingDataChatPermissionRepository chatPermissionRepository, ChatPermissionMapper chatPermissionMapper) {
		return new JpaChatPermissionRepositoryAdapter(chatPermissionRepository, chatPermissionMapper);
	}

	@Bean
	public CreateChatRoomUseCase createChatRoomUseCase(IChatRoomRepository chatRoomRepository,
			ChatRoomFactory chatRoomFactory, IUserRepository userRepository,
			IChatPermissionRepository permissionRepository) {
		return new CreateChatRoomUseCase(chatRoomRepository, chatRoomFactory, userRepository, permissionRepository);
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
			ValidateChatParticipantUseCase validateChatParticipantUseCase) {
		return new GetChatRoomMessagesUseCase(messageRepository, validateChatParticipantUseCase);
	}

	@Bean
	public IChatParticipantRepository chatParticipantRepository(
			SpringDataChatParticipantRepository springDataChatParticipantRepository) {
		return new JpaChatParticipantRepositoryAdapter(springDataChatParticipantRepository);
	}

	@Bean
	public SyncChatRoomUseCase syncChatRoomUseCase(IUserRepository userRepository,
			IChatRoomRepository chatRoomRepository, IUserDeviceRepository deviceRepository,
			IConversationKeyDistributionRepository conversationKeyRepository) {
		return new SyncChatRoomUseCase(chatRoomRepository, deviceRepository, userRepository, conversationKeyRepository);
	}
}
