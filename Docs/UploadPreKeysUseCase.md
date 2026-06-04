# UploadPreKeysUseCase

## Descripción

El `UploadPreKeysUseCase` es el encargado de registrar el material criptográfico inicial de un dispositivo dentro del módulo de identidad.

Durante el proceso:

* Se valida el dispositivo.
* Se valida que la identidad criptográfica no exista previamente.
* Se valida la integridad de las One-Time PreKeys.
* Se registra la Identity Key.
* Se registra la Signed PreKey.
* Se registran las One-Time PreKeys.
* Finalmente se retorna la cantidad de llaves disponibles para el dispositivo.

---

## Objetivo

Este caso de uso permite inicializar el material criptográfico necesario para Signal Protocol.

Al finalizar exitosamente:

* El dispositivo posee una Identity Key registrada.
* El dispositivo posee una Signed PreKey registrada.
* El dispositivo posee un conjunto de One-Time PreKeys disponibles.
* El servidor puede distribuir PreKey Bundles a otros usuarios.

---

## Flujo de registro

```text
Request
  ↓
Controller / LoginUserUseCase
  ↓
UploadPreKeysUseCase
  ↓
Validaciones
  ↓
Registrar Identity Key
  ↓
Registrar Signed PreKey
  ↓
Registrar One-Time PreKeys
  ↓
Persistencia
  ↓
Response
```

---

## Flujo interno

```text
Validar DeviceId
        ↓
Verificar Identity existente
        ↓
Validar One-Time PreKeys
        ↓
Crear DeviceIdentity
        ↓
Persistir Identity
        ↓
Crear SignedPreKey
        ↓
Persistir SignedPreKey
        ↓
Crear One-Time PreKeys
        ↓
Persistir One-Time PreKeys
        ↓
Retornar cantidad disponible
```

---

## Material criptográfico registrado

Actualmente se registran tres tipos de llaves.

### Identity Key

Representa la identidad permanente del dispositivo.

```java
PublicIdentityKey
```

Cada dispositivo puede tener únicamente una.

Persistencia:

```java
DeviceIdentity
```

---

### Signed PreKey

Clave pública firmada por la Identity Key.

Se utiliza durante el establecimiento inicial de sesiones Signal.

Persistencia:

```java
DeviceSignedPreKey
```

Información almacenada:

```java
keyId
publicKey
signature
```

---

### One-Time PreKeys

Conjunto de claves efímeras consumibles.

Se utilizan durante la creación de nuevas sesiones cifradas.

Persistencia:

```java
DeviceOneTimePreKey
```

Información almacenada:

```java
keyId
publicKey
```

---

## Validaciones implementadas

Actualmente el caso de uso valida:

### DeviceId obligatorio

```java
if(command.deviceId() == null)
```

Resultado:

```java
IllegalArgumentException
```

---

### Identity Key única

Un dispositivo únicamente puede poseer una Identity Key activa.

Validación:

```java
identityRepository.existsByDeviceId(deviceId)
```

Resultado:

```java
IllegalStateException
```

Mensaje:

```text
Identity key already registered
```

---

### One-Time PreKeys obligatorias

El dispositivo debe proporcionar al menos una One-Time PreKey.

Validación:

```java
command.oneTimePreKeys().isEmpty()
```

Resultado:

```java
IllegalArgumentException
```

Mensaje:

```text
You must provide at least one one-time prekey
```

---

### One-Time PreKey IDs únicos

No se permiten IDs duplicados dentro de la misma solicitud.

Ejemplo inválido:

```text
KeyId: 1
KeyId: 1
```

Resultado:

```java
IllegalArgumentException
```

Mensaje:

```text
Duplicate one-time prekey id
```

---

## Transaccionalidad

El caso de uso se ejecuta dentro de una transacción.

```java
@Transactional
```

Esto garantiza que:

* Todas las llaves se registren correctamente.
* Ninguna llave quede persistida parcialmente.
* El sistema mantenga consistencia criptográfica.

---

## Persistencia

La persistencia se realiza mediante tres repositorios.

### IDeviceIdentityRepository

Responsable de almacenar:

```java
DeviceIdentity
```

---

### IDeviceSignedPreKeyRepository

Responsable de almacenar:

```java
DeviceSignedPreKey
```

---

### IDeviceOneTimePreKeyRepository

Responsable de almacenar:

```java
DeviceOneTimePreKey
```

---

## Entidades persistidas

### DeviceIdentity

Representa la identidad criptográfica permanente del dispositivo.

Campos principales:

```java
DeviceId
PublicIdentityKey
createdAt
```

---

### DeviceSignedPreKey

Representa la Signed PreKey activa.

Campos principales:

```java
DeviceId
keyId
SignedPreKeyPublicKey
SignedPreKeySignature
createdAt
rotatedAt
```

---

### DeviceOneTimePreKey

Representa una One-Time PreKey disponible.

Campos principales:

```java
DeviceId
keyId
OneTimePreKeyPublicKey
createdAt
```

---

## Resultado

El caso de uso retorna:

```java
UploadPreKeysResult
```

Contenido:

```java
int availableKeys
```

Representa la cantidad de One-Time PreKeys registradas para el dispositivo.

---

## Integración con Login

Actualmente este caso de uso es invocado por:

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

Esto garantiza que todo dispositivo autenticado posea material criptográfico válido antes de recibir acceso al sistema.

---

## Testing

Actualmente existen tests para:

### Casos válidos

* Registro correcto de Identity Key.
* Registro correcto de Signed PreKey.
* Registro correcto de One-Time PreKeys.
* Retorno correcto de cantidad disponible.
* Persistencia de todas las entidades.

### Casos inválidos

* DeviceId nulo.
* Identity Key ya registrada.
* Lista vacía de One-Time PreKeys.
* IDs duplicados en One-Time PreKeys.

---

## Ejemplo de Request

```json
{
  "deviceId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
  "publicIdentityKey": "BASE64_IDENTITY_KEY",
  "signedPreKey": {
    "keyId": 1,
    "publicKey": "BASE64_SIGNED_PREKEY",
    "signature": "BASE64_SIGNATURE"
  },
  "oneTimePreKeys": [
    {
      "keyId": 1,
      "publicKey": "BASE64_PREKEY_1"
    },
    {
      "keyId": 2,
      "publicKey": "BASE64_PREKEY_2"
    }
  ]
}
```

---

## Ejemplo de Response

```json
{
  "availableKeys": 100
}
```

---

## Consideraciones de diseño

### Device-Centric Encryption

Todas las llaves criptográficas se asocian a:

```java
DeviceId
```

y no directamente al usuario.

Esto permite:

* Múltiples dispositivos por usuario.
* Sesiones independientes.
* Revocación selectiva.
* Rotación de llaves por dispositivo.

---

### Identity Key única

La Identity Key representa la identidad criptográfica permanente del dispositivo.

Por diseño:

```text
1 Device
      ↓
1 Identity Key
```

No se permite registrar una segunda identidad sobre el mismo dispositivo.

---

### One-Time PreKeys consumibles

Las One-Time PreKeys están diseñadas para ser utilizadas una única vez.

Su ciclo de vida es:

```text
Upload
   ↓
Disponible
   ↓
Consumida
   ↓
Eliminada
```

Esto sigue el modelo definido por Signal Protocol.

---

## Arquitectura aplicada

El módulo sigue una arquitectura basada en:

* Domain Driven Design (DDD)
* Clean Architecture
* Use Cases
* Ports & Adapters
* Signal Protocol
* Spring Boot
* JPA/Hibernate
* Transaction Management
