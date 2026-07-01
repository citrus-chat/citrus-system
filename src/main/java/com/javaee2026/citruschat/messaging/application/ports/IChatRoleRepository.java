package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;

import java.util.List;
import java.util.Optional;

public interface IChatRoleRepository {

	List<ChatRole> findByChatRoomId(ChatRoomId chatRoomId);

	Optional<ChatRole> findByIdAndChatRoomId(RoleId roleId, ChatRoomId chatRoomId);

	ChatRole save(ChatRole role);

	ChatRole update(ChatRole role);

	boolean delete(RoleId roleId);

	boolean existsByNameAndChatRoomId(String name, ChatRoomId chatRoomId);

	boolean existsByNameAndChatRoomIdExcludingRole(String name, ChatRoomId chatRoomId, RoleId excludedRoleId);

	boolean existsByIdAndChatRoomId(RoleId roleId, ChatRoomId chatRoomId);
}
