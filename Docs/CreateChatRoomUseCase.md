# CreateChatRoomUseCase

## Descripción

El `CreateChatRoomUseCase` es el encargado de crear salas de chat dentro del módulo de mensajería.

Durante el proceso:

* Se valida el creador del chat.
* Se validan los participantes.
* Se valida el tipo de chat.
* Se inicializan los roles por defecto.
* Se asignan permisos a cada rol.
* Se crean los participantes iniciales.
* Se asignan roles a cada participante.
* Finalmente se persiste toda la estructura del chat.

---

## Tipos de Chat soportados

Actualmente existen dos tipos:

```java
DIRECT
GROUP
```

### DIRECT

* Debe contener exactamente 2 usuarios.
* Pensado para conversaciones privadas.

### GROUP

* Puede contener múltiples usuarios.
* Soporta jerarquías de roles y permisos.

---

## Roles por defecto

Cada chat creado inicializa automáticamente los siguientes roles:

```java
OWNER
ADMIN
MEMBER
```

### OWNER

Posee control total del chat.

### ADMIN

Puede administrar participantes y moderar contenido.

### MEMBER

Usuario estándar del chat.

---

## Permisos

Los permisos se cargan desde la base de datos y son asociados automáticamente a cada rol.

Ejemplos:

```java
CAN_SEND_MESSAGE
CAN_DELETE_MESSAGE
CAN_EDIT_MESSAGE
CAN_VIEW_MESSAGE
CAN_ATTACH_FILE
CAN_START_CALL
CAN_ADD_CHAT_PARTICIPANT
CAN_REMOVE_CHAT_PARTICIPANT
CAN_MODIFY_CHAT
```

---

## Flujo de creación

```text
Request
  ↓
Controller
  ↓
CreateChatRoomUseCase
  ↓
Validaciones
  ↓
Inicialización de roles
  ↓
Inicialización de participantes
  ↓
Asignación de permisos
  ↓
Persistencia JPA
  ↓
Response
```

---

## Validaciones implementadas

El caso de uso actualmente valida:

* El creator no puede ser null.
* Todos los participantes deben existir.
* El tipo de chat debe ser válido.
* Un chat DIRECT no puede tener más de 2 participantes.
* El chat debe tener roles antes de inicializar participantes.

---

## Persistencia

Actualmente se persisten automáticamente:

### ChatRoom

Tabla:

```sql
chat_rooms
```

### ChatParticipant

Tabla:

```sql
chat_participants
```

### ChatRole

Tabla:

```sql
chat_roles
```

### Relación Role -> Permission

Tabla:

```sql
chat_role_permissions
```

### Relación Participant -> Role

Tabla:

```sql
chat_participant_roles
```

---

## Testing

Actualmente existen tests para:

### Casos válidos

* Crear chat GROUP correctamente.
* Crear chat DIRECT correctamente.
* Persistencia de participantes.
* Persistencia de roles.
* Persistencia de permisos.
* Persistencia de relaciones participant-role.

### Casos inválidos

* Creator null.
* Usuario inexistente.
* Tipo de chat inválido.
* DIRECT con más de 2 participantes.

---

## Testing vía Postman

### Endpoint

```http
POST /api/v1/chatroom/create
```

### Headers

```http
Authorization: Bearer <jwt>
Content-Type: application/json
```

### Ejemplo Body

```json
{
  "type": "GROUP",
  "name": "JavaEE Chat",
  "participantIds": [
    "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
    "cccccccc-cccc-cccc-cccc-cccccccccccc"
  ]
}
```

---

## Autenticación

El endpoint utiliza JWT Authentication mediante Spring Security.

El `creatorId` se obtiene automáticamente desde:

```java
@AuthenticationPrincipal Jwt jwt
```

y luego:

```java
UUID creatorId = UUID.fromString(jwt.getSubject());
```

Por lo tanto:

* El usuario debe existir.
* Debe autenticarse previamente.
* Debe enviar un JWT válido.

---

## Consideraciones de diseño

### ChatParticipant

`ChatParticipant` es tratado como entidad hija de `ChatRoom`.

Por ese motivo:

* No posee repository independiente.
* No posee factory independiente.
* Se persiste únicamente a través de `ChatRoom`.

### ChatRole

Los roles sí son entidades completas ya que:

* Poseen permisos.
* Poseen jerarquía.
* Poseen prioridad.
* Se relacionan con múltiples participantes.

---

## Arquitectura aplicada

El módulo sigue una arquitectura basada en:

* Domain Driven Design (DDD)
* Clean Architecture
* Use Cases
* Ports & Adapters
* JPA/Hibernate
* Spring Boot

