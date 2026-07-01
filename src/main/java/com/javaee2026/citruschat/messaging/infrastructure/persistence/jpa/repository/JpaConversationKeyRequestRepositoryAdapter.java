package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IConversationKeyRequestRepository;
import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyRequest;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ConversationKeyRequestJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ConversationKeyRequestJpaMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

import java.util.List;

public class JpaConversationKeyRequestRepositoryAdapter implements IConversationKeyRequestRepository {

	private final SpringDataConversationKeyRequestRepository repository;

	public JpaConversationKeyRequestRepositoryAdapter(SpringDataConversationKeyRequestRepository repository) {

		this.repository = repository;
	}

	@Override
	public ConversationKeyRequest save(ConversationKeyRequest request) {

		ConversationKeyRequestJpaEntity saved = repository.save(ConversationKeyRequestJpaMapper.toJpa(request));

		return ConversationKeyRequestJpaMapper.toDomain(saved);
	}

	@Override
	public boolean existsByConversationIdAndTargetDeviceId(ChatRoomId conversationId, DeviceId targetDeviceId) {

		return repository.existsByConversationIdAndTargetDeviceId(conversationId.value(), targetDeviceId.value());
	}

	@Override
	public List<ConversationKeyRequest> findAllByTargetDeviceId(DeviceId targetDeviceId) {

		return repository.findAllByTargetDeviceId(targetDeviceId.value()).stream()
				.map(ConversationKeyRequestJpaMapper::toDomain).toList();
	}

	@Override
	public void deleteByConversationIdAndTargetDeviceId(ChatRoomId conversationId, DeviceId targetDeviceId) {

		repository.deleteByConversationIdAndTargetDeviceId(conversationId.value(), targetDeviceId.value());
	}

}
