# Chat Permission Audit

Backend permission authority lives in `ChatPermissionAuthorizationService`.
Use cases must call it with constants from `ChatPermissionList`; group permissions
must not use `/api/v1/admin/access`.

## Enforced Endpoints

| Area | Endpoint / use case | Permission |
| --- | --- | --- |
| Messages | `GET /api/v1/chatroom/{chatroomId}/sync/messages` | `CAN_VIEW_MESSAGE` |
| Messages | `GetChatRoomMessagesUseCase` (controller route currently disabled) | `CAN_VIEW_MESSAGE` |
| Messages | `POST /api/v1/messages/send` | `CAN_SEND_MESSAGE` |
| Roles | `POST /api/v1/chatroom/{chatroomId}/roles` | `CAN_CREATE_ROLE` or chat creator |
| Roles | `PATCH /api/v1/chatroom/{chatroomId}/roles/{roleId}` | `CAN_MODIFY_ROLE` or chat creator |
| Roles | `DELETE /api/v1/chatroom/{chatroomId}/roles/{roleId}` | `CAN_DELETE_ROLE` or chat creator |
| Roles | `PATCH /api/v1/chatroom/{chatroomId}/participant/{participantId}/roles` | `CAN_MODIFY_ROLE` or chat creator |

## Not Implemented Yet

These permissions exist, but no backend endpoint/use case currently performs the
corresponding action:

| Permission | Technical TODO |
| --- | --- |
| `CAN_EDIT_MESSAGE` | Add this guard when a message edit endpoint/use case is implemented. |
| `CAN_DELETE_MESSAGE` | Add this guard when a message delete endpoint/use case is implemented. |
| `CAN_PING_MESSAGE` | Add this guard when ping/mention/notify functionality is implemented. |
| `CAN_ATTACH_FILE` | Add this guard when message attachments or media upload are implemented. |
| `CAN_START_CALL` | Add this guard when call start signaling is implemented. |
| `CAN_ADD_CHAT_PARTICIPANT` | Add this guard when post-creation participant invite/add is implemented. |
| `CAN_REMOVE_CHAT_PARTICIPANT` | Add this guard when kick/remove participant is implemented. |
| `CAN_MODIFY_CHAT_PARTICIPANT` | Add this guard when participant metadata updates are implemented. |
| `CAN_MODIFY_CHAT` | Add this guard when group metadata updates are implemented. |
| `CAN_DELETE_CHAT` | Add this guard when full chat deletion/deactivation is implemented. |
