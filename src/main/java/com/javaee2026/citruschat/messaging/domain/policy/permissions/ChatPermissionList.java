package com.javaee2026.citruschat.messaging.domain.policy.permissions;

import java.util.Set;

public final class ChatPermissionList {
	private ChatPermissionList() {
	}

	// Message based permissions
	public static final String CAN_SEND_MESSAGE = "CAN_SEND_MESSAGE";
	public static final String CAN_DELETE_MESSAGE = "CAN_DELETE_MESSAGE";
	public static final String CAN_EDIT_MESSAGE = "CAN_EDIT_MESSAGE";
	public static final String CAN_VIEW_MESSAGE = "CAN_VIEW_MESSAGE";
	public static final String CAN_ATTACH_FILE = "CAN_ATTACH_FILE"; // this includes multimedia files and audiomessages
	public static final String CAN_START_CALL = "CAN_START_CALL"; // this includes videocalls, and audio calls
	public static final String CAN_PING_MESSAGE = "CAN_PING_MESSAGE";

	// Role based permissions
	public static final String CAN_CREATE_ROLE = "CAN_CREATE_ROLE";
	public static final String CAN_CREATE_ROL = CAN_CREATE_ROLE;
	public static final String CAN_MODIFY_ROLE = "CAN_MODIFY_ROLE";
	public static final String CAN_DELETE_ROLE = "CAN_DELETE_ROLE";

	// ChatParticipant based permissions
	public static final String CAN_MODIFY_CHAT_PARTICIPANT = "CAN_MODIFY_CHAT_PARTICIPANT";
	public static final String CAN_REMOVE_CHAT_PARTICIPANT = "CAN_REMOVE_CHAT_PARTICIPANT";
	public static final String CAN_ADD_CHAT_PARTICIPANT = "CAN_ADD_CHAT_PARTICIPANT";

	// Chat based permissions
	public static final String CAN_DELETE_CHAT = "CAN_DELETE_CHAT";
	public static final String CAN_MODIFY_CHAT = "CAN_MODIFY_CHAT";

	public static final Set<String> ADMINISTRATIVE = Set.of(CAN_CREATE_ROLE, CAN_MODIFY_ROLE, CAN_DELETE_ROLE,
			CAN_MODIFY_CHAT_PARTICIPANT, CAN_REMOVE_CHAT_PARTICIPANT, CAN_ADD_CHAT_PARTICIPANT, CAN_DELETE_CHAT,
			CAN_MODIFY_CHAT);

	public static final Set<String> ALL = Set.of(CAN_SEND_MESSAGE, CAN_DELETE_MESSAGE, CAN_EDIT_MESSAGE,
			CAN_VIEW_MESSAGE, CAN_ATTACH_FILE, CAN_START_CALL, CAN_PING_MESSAGE, CAN_CREATE_ROLE, CAN_MODIFY_ROLE,
			CAN_DELETE_ROLE, CAN_MODIFY_CHAT_PARTICIPANT, CAN_REMOVE_CHAT_PARTICIPANT, CAN_ADD_CHAT_PARTICIPANT,
			CAN_DELETE_CHAT, CAN_MODIFY_CHAT);

}
