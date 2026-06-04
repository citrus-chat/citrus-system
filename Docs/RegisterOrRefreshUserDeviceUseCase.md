# RegisterOrRefreshUserDeviceUseCase

## Descripción

El `RegisterOrRefreshUserDeviceUseCase` es el encargado de registrar o actualizar dispositivos dentro del módulo de identidad.

Cada dispositivo representa una sesión autenticada de un usuario y sirve como unidad principal para asociar:

* Tokens JWT.
* Material criptográfico de Signal Protocol.
* Identity Keys.
* Signed PreKeys.
* One-Time PreKeys.

---

## Objetivo

Este caso de uso permite:

* Registrar nuevos dispositivos.
* Reutilizar dispositivos existentes cuando corresponda.
* Actualizar información de actividad.
* Mantener trazabilidad de sesiones activas.

Al finalizar exitosamente se obtiene un:

```java
UUID deviceId
```

que será utilizado por otros casos de uso del sistema.

---

## Flujo de registro

```text
Request
  ↓
Controller
  ↓
RegisterOrRefreshUserDeviceUseCase
  ↓
Buscar dispositivo existente
  ↓
¿Existe?
 ├─ Sí → Actualizar información
 └─ No → Crear dispositivo
  ↓
Persistencia
  ↓
Response
```

---

## Flujo interno

```text
Validar DeviceType
        ↓
Buscar Device por DeviceId + UserId
        ↓
¿Existe?
        ↓
Sí ---------------------- No
↓                         ↓
Actualizar lastSeen       Crear Device
Actualizar nombre         Generar DeviceId
↓                         ↓
Persistir                 Persistir
        ↓
Retornar DeviceId
```

---

## Comportamiento de creación

Si el cliente no proporciona un `deviceId`, o el dispositivo no existe para el usuario indicado, se crea un nuevo dispositivo.

Internamente:

```java
UserDevice.createNew(...)
```

genera automáticamente:

```java
DeviceId.newId()
```

y establece:

```java
createdAt
lastSeen
```

con la fecha actual.

---

## Comportamiento de actualización

Si el cliente proporciona un `deviceId` válido perteneciente al usuario:

```java
findActiveByIdAndUserId(deviceId, userId)
```

el dispositivo es actualizado.

Actualmente se actualizan:

```java
deviceName
lastSeen
```

mediante:

```java
refreshDevice(...)
```

---

## Normalización del nombre

Los nombres de dispositivo son normalizados automáticamente.

### Nombre nulo

```java
null
```

Resultado:

```text
Unknown device
```

### Nombre vacío

```java
""
```

Resultado:

```text
Unknown device
```

### Nombre válido

```java
" Chrome on Windows "
```

Resultado:

```text
Chrome on Windows
```

---

## Tipos de dispositivo soportados

Actualmente existen tres tipos:

```java
MOBILE
WEB
DESKTOP
```

---

### MOBILE

Representa aplicaciones móviles.

Ejemplos:

```text
Android
iOS
```

---

### WEB

Representa navegadores web.

Ejemplos:

```text
Chrome
Firefox
Edge
```

---

### DESKTOP

Representa aplicaciones de escritorio.

Ejemplos:

```text
Windows
Linux
macOS
```

---

## Valor por defecto

Si el cliente no especifica un tipo:

```java
null
```

se utiliza automáticamente:

```java
DeviceType.WEB
```

---

## Entidad UserDevice

La entidad principal administrada por este caso de uso es:

```java
UserDevice
```

---

### Campos principales

```java
DeviceId id
UserId userId
String deviceName
DeviceType deviceType
Instant lastSeen
Instant createdAt
Instant revokedAt
```

---

### lastSeen

Representa la última actividad conocida del dispositivo.

Se actualiza automáticamente cada vez que el dispositivo es reutilizado.

---

### createdAt

Fecha de creación del dispositivo.

Permanece inmutable.

---

### revokedAt

Fecha de revocación del dispositivo.

Si posee valor:

```java
device.isRevoked()
```

retorna:

```java
true
```

---

## Gestión de dispositivos activos

Todas las búsquedas de reutilización utilizan:

```java
findActiveByIdAndUserId(...)
```

Por lo tanto:

* Los dispositivos revocados no pueden reutilizarse.
* Solo los dispositivos activos pueden actualizarse.
* El propietario debe coincidir.

---

## Persistencia

La persistencia se realiza mediante:

```java
IUserDeviceRepository
```

---

### Métodos utilizados

```java
findActiveByIdAndUserId(...)
save(...)
```

---

### Tabla principal

```sql
user_devices
```

---

## Resultado

El caso de uso retorna:

```java
RegisterOrRefreshUserDeviceResult
```

Contenido:

```java
UUID deviceId
```

---

## Integración con Login

Este caso de uso es invocado directamente por:

```java
LoginUserUseCase
```

Flujo:

```text
Login
  ↓
RegisterOrRefreshUserDeviceUseCase
  ↓
UploadPreKeysUseCase
  ↓
JWT
```

El `deviceId` generado será utilizado posteriormente para:

* Asociar llaves criptográficas.
* Generar JWT.
* Identificar el dispositivo autenticado.

---

## Validaciones implementadas

Actualmente se valida:

* Existencia de dispositivo activo.
* Propiedad del dispositivo.
* Normalización de nombre.
* Valor por defecto de DeviceType.

---

## Testing

Actualmente existen tests para:

### Casos válidos

* Crear dispositivo nuevo.
* Reutilizar dispositivo existente.
* Actualizar lastSeen.
* Actualizar nombre del dispositivo.
* Aplicar DeviceType por defecto.
* Normalizar nombres.

### Casos inválidos

* DeviceId inexistente.
* DeviceId perteneciente a otro usuario.
* Nombre vacío.
* Tipo de dispositivo nulo.

---

## Testing vía Postman

Este caso de uso no expone endpoint propio.

Actualmente es utilizado de forma interna por:

```http
POST /api/v1/auth/login
```

---

## Consideraciones de diseño

### Device como identidad criptográfica

En CitrusChat el material criptográfico no se asocia directamente al usuario.

Se asocia a:

```java
DeviceId
```

Esto permite:

* Múltiples dispositivos por usuario.
* Rotación independiente de llaves.
* Revocación individual de sesiones.
* Compatibilidad con Signal Protocol.

---

### Sesión = Dispositivo

Actualmente el comportamiento funcional esperado es:

```text
Login
   ↓
Crear Device

Logout
   ↓
Revocar Device

Próximo Login
   ↓
Crear nuevo Device
```

Por lo tanto, aunque el caso de uso soporta actualización de dispositivos existentes, el flujo habitual de la aplicación consiste en crear un nuevo dispositivo para cada sesión autenticada.

---

## Arquitectura aplicada

El módulo sigue una arquitectura basada en:

* Domain Driven Design (DDD)
* Clean Architecture
* Use Cases
* Ports & Adapters
* Spring Boot
* JWT Authentication
* Signal Protocol
* JPA/Hibernate
