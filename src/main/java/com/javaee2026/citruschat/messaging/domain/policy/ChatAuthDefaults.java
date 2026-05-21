package com.javaee2026.citruschat.messaging.domain.policy;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoleDefault;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;

import java.util.Map;
import java.util.Set;

import static com.javaee2026.citruschat.messaging.domain.enums.ChatRoleDefault.*;

public class ChatAuthDefaults {

	public static final Map<ChatRoleDefault, Set<String>> DEFAULTS = Map.of(OWNER, ChatPermissionList.ALL,

			ADMIN,
			Set.of(ChatPermissionList.CAN_SEND_MESSAGE, ChatPermissionList.CAN_DELETE_MESSAGE,
					ChatPermissionList.CAN_EDIT_MESSAGE, ChatPermissionList.CAN_VIEW_MESSAGE,
					ChatPermissionList.CAN_ATTACH_FILE, ChatPermissionList.CAN_START_CALL,
					ChatPermissionList.CAN_PING_MESSAGE, ChatPermissionList.CAN_CREATE_ROL,
					ChatPermissionList.CAN_MODIFY_ROLE, ChatPermissionList.CAN_DELETE_ROLE,
					ChatPermissionList.CAN_MODIFY_CHAT_PARTICIPANT, ChatPermissionList.CAN_REMOVE_CHAT_PARTICIPANT,
					ChatPermissionList.CAN_ADD_CHAT_PARTICIPANT, ChatPermissionList.CAN_MODIFY_CHAT),

			MEMBER,
			Set.of(ChatPermissionList.CAN_SEND_MESSAGE, ChatPermissionList.CAN_DELETE_MESSAGE,
					ChatPermissionList.CAN_EDIT_MESSAGE, ChatPermissionList.CAN_VIEW_MESSAGE,
					ChatPermissionList.CAN_ATTACH_FILE, ChatPermissionList.CAN_START_CALL,
					ChatPermissionList.CAN_PING_MESSAGE)

	);
}
