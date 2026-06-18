# RegisterOrRefreshUserDeviceUseCase

## Descripción

El `RegisterOrRefreshUserDeviceUseCase` es el responsable de registrar nuevos dispositivos o actualizar dispositivos existentes asociados a un usuario.

Este caso de uso centraliza toda la lógica relacionada con la gestión de dispositivos dentro del sistema.

Durante el proceso:

* Se busca el dispositivo utilizando su identificador único.
* Si el dispositivo no existe, se registra uno nuevo.
* Si el dispositivo existe, se actualiza su información.
* Se sincroniza la clave pública del dispositivo.
* Se actualiza la fecha de última actividad.
* Se retorna el dispositivo persistido.

---

## Objetivo

Garantizar que el sistema mantenga información consistente y actualizada sobre los dispositivos utilizados por los usuarios.

Al finalizar exitosamente:

* El dispositivo existe en la base de datos.
* El dispositivo está asociado a un usuario válido.
* La clave pública almacenada está actualizada.
* La última actividad refleja la autenticación más reciente.

---

## Flujo de ejecución

```text
Request
  ↓
Buscar dispositivo por DeviceId
  ↓
¿Existe?
 ├─ No → Registrar dispositivo
 └─ Sí → Actualizar dispositivo
  ↓
Actualizar LastSeen
  ↓
Persistir cambios
  ↓
Retornar UserDevice
```

---

## Registro de dispositivo

Si el dispositivo no existe previamente:

```java
userDeviceRepository.findById(deviceId)
```

retorna vacío y el caso de uso procede a crear una nueva instancia.

Información registrada:

```java
DeviceId
UserId
DeviceName
DeviceType
PublicKey
CreatedAt
LastSeen
```

Posteriormente el dispositivo es persistido mediante:

```java
IUserDeviceRepository
```

---

## Actualización de dispositivo

Si el dispositivo ya existe:

* Se actualiza el nombre del dispositivo.
* Se actualiza la clave pública.
* Se actualiza la fecha de última actividad.

Esto permite mantener sincronizada la información reportada por el cliente.

---

## Clave pública

Cada dispositivo posee una clave pública asociada:

```java
PublicKey
```

La clave pública representa la identidad criptográfica actual del dispositivo.

Durante cada autenticación el sistema sincroniza este valor para garantizar que el estado almacenado refleje la información más reciente enviada por el cliente.

---

## Last Seen

Cada autenticación actualiza:

```java
lastSeen
```

Este campo permite:

* Detectar dispositivos inactivos.
* Auditar actividad reciente.
* Implementar futuras políticas de expiración o revocación.

---

## Resultado

El caso de uso retorna:

```java
UserDevice
```

Información incluida:

```java
DeviceId id
UserId userId
String deviceName
DeviceType deviceType
PublicKey publicKey
Instant createdAt
Instant lastSeen
Instant revokedAt
```

---

## Flujo interno

```text
Buscar dispositivo
    ↓
¿Existe?
    ↓
Crear o actualizar
    ↓
Actualizar PublicKey
    ↓
Actualizar LastSeen
    ↓
Guardar
    ↓
Retornar UserDevice
```

---

## Dependencias

### IUserDeviceRepository

Responsable de persistir y recuperar dispositivos.

```java
IUserDeviceRepository
```

---

## Validaciones implementadas

El caso de uso valida:

* El DeviceId debe ser válido.
* El UserId debe ser válido.
* La clave pública debe ser válida.
* El dispositivo no debe encontrarse revocado para ser reutilizado.

Si alguna validación falla se lanza la excepción correspondiente.

---

## Casos soportados

### Registro inicial

```text
DeviceId inexistente
    ↓
Crear dispositivo
    ↓
Persistir
```

---

### Reautenticación

```text
DeviceId existente
    ↓
Actualizar información
    ↓
Persistir
```

---

## Testing

Actualmente existen tests para:

### Casos válidos

* Registro de un nuevo dispositivo.
* Actualización de un dispositivo existente.
* Actualización de la clave pública.
* Actualización de LastSeen.
* Persistencia correcta del dispositivo.

### Casos inválidos

* Datos inválidos del dispositivo.
* Dispositivo revocado.
* Error de persistencia.

---

## Persistencia

Los dispositivos son almacenados en:

```text
user_devices
```

Campos principales:

```text
id
user_id
public_key
device_name
device_type
created_at
last_seen
revoked_at
```

---

## Consideraciones de diseño

### DeviceId como identidad permanente

Cada dispositivo es identificado por un:

```java
DeviceId
```

estable generado por el cliente.

Esto permite:

* Reconocer dispositivos entre sesiones.
* Mantener historial de actividad.
* Asociar material criptográfico a un dispositivo específico.

---

### Clave pública por dispositivo

La clave pública pertenece al dispositivo y no al usuario.

Esto permite:

* Múltiples dispositivos por usuario.
* Rotación independiente de claves.
* Identidades criptográficas separadas.

---

### Revocación futura

El modelo contempla:

```java
revokedAt
```

para soportar futuras funcionalidades de:

* Cierre remoto de sesiones.
* Eliminación de dispositivos.
* Revocación de acceso.

---

## Arquitectura aplicada

El módulo sigue una arquitectura basada en:

* Domain Driven Design (DDD)
* Clean Architecture
* Use Cases
* Ports & Adapters
* Spring Boot
* JPA/Hibernate
* Device Management
* Device-Based Authentication
* End-to-End Encryption Readiness
