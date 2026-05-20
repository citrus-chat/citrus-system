// package com.javaee2026.citruschat.messaging.domain.factory;
//
// import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
// import com.javaee2026.citruschat.shared.domain.valueobjects.*;
// import org.springframework.stereotype.Component;
//
// import java.time.Instant;
//
// @Component
// public class ChatParticipantFactory {
//
// public ChatParticipant createNew(ChatRoomId chatRoomId, UserId userId, RoleId
// roleId, Instant joinedAt) {
// return new ChatParticipant(ParticipantId.newId(), chatRoomId, userId, roleId,
// joinedAt, null, null);
// }
//
// public ChatParticipant reconstitute(ParticipantId id, ChatRoomId chatRoomId,
// UserId userId, RoleId roleId,
// Instant joinedAt, Instant leftAt, MessageId lastReadMessageId) {
// return new ChatParticipant(id, chatRoomId, userId, roleId, joinedAt, leftAt,
// lastReadMessageId);
// }
// }
