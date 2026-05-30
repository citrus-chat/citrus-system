# 📡 CitrusChat - Messaging Flow (REST + WebSocket)

## Objetivo

Implementar el flujo completo de mensajería en tiempo real respetando:

* Clean Architecture
* Hexagonal Architecture
* Spring Boot
* JWT Authentication
* Multi Device Support
* WebSocket + STOMP
* Persistencia de mensajes
* Validaciones de seguridad

---

# Arquitectura General

```text
Frontend
    │
    ▼
REST Login
    │
    ▼
JWT + Device Registration
    │
    ▼
Frontend almacena:
    - accessToken
    - deviceId
    │
    ▼
WebSocket CONNECT
    │
    ▼
JWT Validation
    │
    ▼
ChatWebSocketController
    │
    ▼
SendMessageUseCase
    │
    ▼
MessageRepository
    │
    ▼
Database
    │
    ▼
Broadcast STOMP
```

---

# Device Management

## Problema

Cada login generaba un nuevo UserDevice.

Esto provocaba:

```text
Chrome Login #1
→ Device A

Chrome Login #2
→ Device B

Chrome Login #3
→ Device C
```

Generando dispositivos inútiles.

---

## Solución Implementada

Se decidió:

```text
1 usuario
+
1 deviceType
=
1 UserDevice activo
```

Ejemplo:

```text
Jose
 ├─ web
 ├─ mobile
 └─ desktop
```

Máximo:

```text
1 device web
1 device mobile
1 device desktop
```

---

## Flujo Login

### Primer Login

```text
deviceId = null
```

↓

```text
buscar device web
```

↓

No existe

↓

```text
crear UserDevice
```

↓

Retornar:

```json
{
  "deviceId": "..."
}
```

---

### Login Posterior

Frontend envía:

```json
{
  "deviceId": "..."
}
```

↓

Backend:

```text
findByIdAndUserId()
```

↓

Existe

↓

```text
update lastSeen
```

↓

Retornar mismo deviceId

---

# Endpoint: Login

## Request

```http
POST /api/v1/auth/login
```

```json
{
  "email": "admin@admin.com",
  "password": "admin",
  "deviceName": "Postman Web",
  "deviceType": "web",
  "publicIdentityKey": "temp-public-key",
  "signedPrekey": "temp-signed-prekey"
}
```

---

## Response

```json
{
  "success": true,
  "data": {
    "userId": "...",
    "accessToken": "...",
    "deviceId": "..."
  }
}
```

---

# Endpoint: Obtener Mis Dispositivos

## Request

```http
GET /api/v1/auth/devices
```

Header:

```http
Authorization: Bearer <token>
```

---

## Response

```json
[
  {
    "id": "...",
    "deviceName": "Chrome on Windows",
    "deviceType": "web",
    "lastSeen": "...",
    "createdAt": "..."
  }
]
```

---

# WebSocket Security

## Conexión

Endpoint:

```text
/ws
```

Cliente:

```text
CONNECT
```

con:

```http
Authorization: Bearer <jwt>
```

---

## Validación

### StompJwtChannelInterceptor

Valida:

```text
JWT válido
```

↓

Genera:

```java
Authentication
```

↓

Disponible en:

```java
ChatWebSocketController
```

---

# Validación Device Ownership

## Problema

Frontend podía enviar:

```json
{
  "senderDeviceId": "device-de-otro-usuario"
}
```

---

## Solución

UseCase:

```java
ValidateUserDeviceOwnershipUseCase
```

Valida:

```text
senderDeviceId
    pertenece
        al
      userId
```

---

## Flujo

```text
JWT
 ↓
userId
 ↓
senderDeviceId
 ↓
ValidateUserDeviceOwnershipUseCase
 ↓
OK
```

---

# Validación Chat Participant

## Problema

Usuario autenticado podía intentar escribir en:

```text
chatRoom donde no participa
```

---

## Solución

UseCase:

```java
ValidateChatParticipantUseCase
```

Valida:

```text
chatRoomId
+
userId
```

↓

Existe participante activo

↓

Permitir

---

# Endpoint: Obtener Mis Chats

## Request

```http
GET /api/v1/chatroom/me
```

Header:

```http
Authorization: Bearer <token>
```

---

## Response

```json
[
  {
    "id": "...",
    "name": "General",
    "type": "group",
    "createdAt": "...",
    "updatedAt": "..."
  }
]
```

---

# Endpoint: Obtener Mensajes

## Request

```http
GET /api/v1/chatroom/{chatRoomId}/messages?page=0&size=50
```

Header:

```http
Authorization: Bearer <token>
```

---

## Seguridad

Antes de devolver mensajes:

```text
ValidateChatParticipantUseCase
```

↓

Si no pertenece:

```text
403
```

---

# WebSocket Message Request

## Destino

```text
/app/chat/sendMessage
```

---

## Payload

```json
{
  "chatRoomId": "uuid",
  "senderDeviceId": "uuid",
  "replyToMessageId": null,
  "payloads": [
    {
      "targetDeviceId": "uuid",
      "encryptedPayload": "hola websocket"
    }
  ]
}
```

---

# WebSocket Broadcast

## Topic

```text
/topic/chatrooms/{chatRoomId}
```

---

## Response

```json
{
  "id": "...",
  "chatRoomId": "...",
  "senderUserId": "...",
  "sentAt": "..."
}
```

---

# Flujo Completo Implementado

```text
Login
 ↓
RegisterOrRefreshUserDeviceUseCase
 ↓
JWT + deviceId
 ↓
Frontend guarda deviceId
 ↓
CONNECT WebSocket
 ↓
JWT Validation
 ↓
Authentication
 ↓
SendMessage
 ↓
ValidateUserDeviceOwnership
 ↓
ValidateChatParticipant
 ↓
SendMessageUseCase
 ↓
Persistencia
 ↓
Broadcast
 ↓
Recepción en clientes
```

---

# Estado Actual

## Completado

* JWT Authentication
* Device Registration
* Device Refresh
* Device Ownership Validation
* Chat Participant Validation
* WebSocket STOMP
* Send Message
* Persist Message
* Broadcast Message
* Get My Devices
* Get My Chat Rooms
* Get Chat Messages

---

# Próximos Pasos

## Backend

* Validar SUBSCRIBE a chatrooms
* Buscar usuarios
* Crear chats directos
* Último mensaje por chat
* Unread Count
* Presence Online/Offline
* Read Receipts
* E2EE real

## Frontend

* Sidebar dinámica
* Cargar chats reales
* Cargar historial
* Conectar WebSocket real
* Actualización en tiempo real
* UX estilo Slack
* Panel de perfil lateral
