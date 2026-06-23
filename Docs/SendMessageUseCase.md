# SendMessage API

## Objetivo

Permitir que un dispositivo envíe un mensaje cifrado a una sala de chat.

El endpoint recibe la solicitud, construye el comando de dominio correspondiente y ejecuta el caso de uso `SendMessageUseCase`, encargado de persistir el mensaje.

La sincronización hacia otros dispositivos se realiza posteriormente mediante los mecanismos de sync existentes.

---

# Endpoint

## POST /api/chat/messages

### Request

```json
{
  "senderId": "user-uuid",
  "messageId": "message-uuid",
  "chatRoomId": "chatroom-uuid",
  "senderDeviceId": "device-uuid",
  "replyMessageId": "message-uuid",
  "keyVersion": 1,
  "iv": "base64-iv",
  "ciphertext": "base64-ciphertext"
}
```

### Campos

| Campo          | Obligatorio | Descripción                                           |
| -------------- | ----------- | ----------------------------------------------------- |
| senderId       | Sí          | Usuario que envía el mensaje                          |
| messageId      | Sí          | Identificador único generado por el cliente           |
| chatRoomId     | Sí          | Sala de chat destino                                  |
| senderDeviceId | Sí          | Dispositivo que origina el mensaje                    |
| replyMessageId | No          | Mensaje al que se responde                            |
| keyVersion     | Sí          | Versión de la clave utilizada para cifrar             |
| iv             | Sí          | Vector de inicialización utilizado durante el cifrado |
| ciphertext     | Sí          | Contenido cifrado                                     |

---

# Flujo

## 1. Recepción

El controlador recibe el request y valida los datos de entrada.

## 2. Construcción del comando

Se construye un `SendMessageCommand`.

```java
new SendMessageCommand(
    new UserId(senderId),
    new MessageId(messageId),
    new ChatRoomId(chatRoomId),
    new DeviceId(senderDeviceId),
    replyMessageId != null
        ? new MessageId(replyMessageId)
        : null,
    keyVersion,
    iv,
    ciphertext
);
```

## 3. Ejecución

```java
sendMessageUseCase.execute(command);
```

## 4. Persistencia

El caso de uso construye la entidad de dominio correspondiente y la persiste utilizando el repositorio de mensajes.

## 5. Respuesta

La API devuelve una respuesta exitosa indicando que el mensaje fue aceptado.

```json
{
  "success": true,
  "message": "Message sent successfully."
}
```

---

# SendMessageUseCase

## Responsabilidad

Persistir un mensaje cifrado dentro de una sala de chat.

Este caso de uso no se encarga de:

* Sincronización de mensajes
* Entrega a dispositivos
* WebSockets
* Notificaciones push

Su responsabilidad actual es únicamente validar y persistir.

---

## Entrada

```java
SendMessageCommand
```

Contiene:

* SenderId
* MessageId
* ChatRoomId
* SenderDeviceId
* ReplyMessageId (opcional)
* KeyVersion
* Iv
* Ciphertext

---

## Reglas de negocio

### La sala debe existir

El chat de destino debe existir en el sistema.

### El usuario debe pertenecer al chat

El remitente debe ser participante de la sala.

### El mensaje debe ser único

No puede existir otro mensaje con el mismo MessageId.

### El mensaje respondido debe existir

Si se especifica un ReplyMessageId, debe existir previamente.

---

## Persistencia

Se almacena la información necesaria para reconstruir el mensaje posteriormente.

### Datos persistidos

```text
messageId
chatRoomId
senderId
senderDeviceId
replyMessageId
keyVersion
iv
ciphertext
createdAt
```

---

# Entidades involucradas

## ChatMessage

Representa un mensaje dentro de una conversación.

Contiene:

```text
MessageId
ChatRoomId
SenderId
SenderDeviceId
ReplyMessageId
KeyVersion
Iv
Ciphertext
CreatedAt
```

---

# Pendientes

## Adjuntos

Actualmente el caso de uso contempla únicamente mensajes cifrados de texto.

El soporte para archivos debería implementarse mediante un flujo separado, posiblemente compuesto por:

1. Upload del archivo.
2. Generación de referencia al recurso.
3. Envío de mensaje que referencia dicho recurso.

Esto probablemente derive en uno o más casos de uso específicos relacionados con attachments y almacenamiento de archivos.
