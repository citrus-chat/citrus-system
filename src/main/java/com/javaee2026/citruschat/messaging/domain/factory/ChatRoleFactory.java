// package com.javaee2026.citruschat.messaging.domain.factory;
//
// import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
// import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
// import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
// import org.springframework.stereotype.Component;
//
// import java.time.Instant;
//
// @Component
// public class ChatRoleFactory {
//
// public static ChatRole reconstitute(RoleId id, ChatRoomId chatRoomId, String
// name, Instant createdAt) {
// return new ChatRole(id, chatRoomId, name, createdAt);
// }
//
// public static ChatRole create(ChatRoomId chatRoomId, String name) {
// return new ChatRole(RoleId.newId(), chatRoomId, name, Instant.now());
// }
//
// public static ChatRole owner(ChatRoomId chatRoomId){
// return new ChatRole(RoleId.newId(), chatRoomId, "OWNER", Instant.now());
// }
//
// public static ChatRole member(ChatRoomId chatRoomId){
// return new ChatRole(RoleId.newId(), chatRoomId, "MEMBER", Instant.now());
// }
//
// public static ChatRole admin(ChatRoomId chatRoomId){
// return new ChatRole(RoleId.newId(), chatRoomId, "ADMIN", Instant.now());
// }
// }
