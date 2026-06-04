# LoginUserUseCase

## Descripción

El `LoginUserUseCase` es el encargado de autenticar usuarios dentro del módulo de identidad.

Durante el proceso:

* Se valida que el usuario exista.
* Se validan las credenciales.
* Se registra o actualiza el dispositivo desde el que inicia sesión.
* Se registra el material criptográfico necesario para Signal Protocol.
* Se genera el JWT de autenticación.
* Finalmente se retorna la información de sesión del usuario.

---

## Objetivo

Este caso de uso centraliza todo el proceso de inicio de sesión y bootstrap criptográfico del dispositivo.

Al finalizar exitosamente:

* El usuario queda autenticado.
* El dispositivo queda registrado.
* Las llaves criptográficas quedan almacenadas.
* El cliente recibe un JWT válido.
* El cliente conoce cuántas One-Time PreKeys tiene disponibles.

---

## Flujo de autenticación

```text
Request
  ↓
Controller
  ↓
LoginUserUseCase
  ↓
Validación de usuario
  ↓
Validación de contraseña
  ↓
RegisterOrRefreshUserDeviceUseCase
  ↓
UploadPreKeysUseCase
  ↓
Generación JWT
  ↓
Response
```

---

## Registro de dispositivo

Antes de generar el token, el sistema registra o actualiza el dispositivo desde el cual se realiza el login.

Esto permite:

* Identificar dispositivos únicos.
* Asociar material criptográfico a cada dispositivo.
* Mantener múltiples dispositivos por usuario.
* Soportar cifrado End-to-End basado en Signal Protocol.

La operación es delegada a:

```java
RegisterOrRefreshUserDeviceUseCase
```

---

## Registro de llaves criptográficas

Una vez registrado el dispositivo, se almacenan las llaves criptográficas enviadas por el cliente.

Actualmente se registran:

### Identity Key

Clave pública permanente del dispositivo.

```java
publicIdentityKey
```

### Signed PreKey

Clave firmada utilizada durante el establecimiento inicial de sesiones.

```java
signedPreKey
```

### One-Time PreKeys

Conjunto de claves efímeras utilizadas para iniciar conversaciones de forma segura.

```java
oneTimePreKeys
```

La operación es delegada a:

```java
UploadPreKeysUseCase
```

---

## JWT

Luego de registrar el dispositivo y las llaves, se genera un JWT utilizando:

```java
JwtService
```

Información incluida:

```text
UserId
DeviceId
Email
Username
```

El token será utilizado posteriormente para acceder a los endpoints protegidos.

---

## Validaciones implementadas

El caso de uso actualmente valida:

* El usuario debe existir.
* La contraseña debe ser válida.
* El dispositivo debe poder registrarse correctamente.
* Las llaves criptográficas deben poder almacenarse correctamente.

Si cualquiera de estas validaciones falla:

```java
InvalidCredentialsException
```

o la excepción correspondiente del caso de uso invocado.

---

## Resultado

El login retorna:

```java
LoginResult
```

Información incluida:

```java
UUID userId
String email
String username
String accessToken
String tokenType
long expiresIn
UUID deviceId
int availableOneTimePreKeys
```

---

## Flujo interno

```text
Buscar usuario
    ↓
Validar contraseña
    ↓
Registrar dispositivo
    ↓
Subir llaves criptográficas
    ↓
Generar JWT
    ↓
Retornar LoginResult
```

---

## Dependencias

El caso de uso depende de:

### IUserRepository

Responsable de recuperar usuarios.

```java
IUserRepository
```

### PasswordEncoder

Responsable de validar contraseñas.

```java
PasswordEncoder
```

### JwtService

Responsable de generar tokens JWT.

```java
JwtService
```

### RegisterOrRefreshUserDeviceUseCase

Responsable de registrar o actualizar dispositivos.

```java
RegisterOrRefreshUserDeviceUseCase
```

### UploadPreKeysUseCase

Responsable de registrar las llaves criptográficas.

```java
UploadPreKeysUseCase
```

---

## Testing

Actualmente existen tests para:

### Casos válidos

* Login exitoso.
* Generación correcta del JWT.
* Registro correcto del dispositivo.
* Registro correcto de las llaves criptográficas.
* Retorno correcto de One-Time PreKeys disponibles.
* Verificación del orden de ejecución del flujo.

### Casos inválidos

* Usuario inexistente.
* Contraseña inválida.

---

## Testing vía Postman

### Endpoint

```http
POST /api/v1/auth/login
```

### Headers

```http
Content-Type: application/json
```

### Ejemplo Body

```json
{
  "email": "test@gmail.com",
  "password": "123456",
  "deviceId": null,
  "deviceName": "Chrome on Windows",
  "deviceType": "WEB",
  "publicIdentityKey": "BASE64_PUBLIC_IDENTITY_KEY",
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

## Respuesta exitosa

```json
{
  "userId": "91ae5825-9096-4c74-9447-1bf03004c36b",
  "email": "test@gmail.com",
  "username": "test_test",
  "accessToken": "jwt-token",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "deviceId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
  "availableOneTimePreKeys": 100
}
```

---

## Autenticación posterior

Una vez obtenido el JWT, el cliente deberá enviarlo en cada petición protegida.

Ejemplo:

```http
Authorization: Bearer <jwt>
```

El backend obtendrá la identidad mediante Spring Security y JWT Authentication.

---

## Consideraciones de diseño

### Registro de llaves durante Login

El sistema actual registra el material criptográfico durante el proceso de autenticación.

Esto garantiza que:

* Todo dispositivo autenticado posea llaves válidas.
* No existan dispositivos activos sin material criptográfico.
* El servidor pueda distribuir bundles de Signal Protocol inmediatamente.

### Dispositivo como entidad principal

Todas las llaves son asociadas a un:

```java
DeviceId
```

y no directamente al usuario.

Esto permite:

* Múltiples dispositivos por usuario.
* Sesiones independientes.
* Rotación de llaves por dispositivo.

---

## Arquitectura aplicada

El módulo sigue una arquitectura basada en:

* Domain Driven Design (DDD)
* Clean Architecture
* Use Cases
* Ports & Adapters
* Spring Boot
* Spring Security
* JWT Authentication
* Signal Protocol
* JPA/Hibernate
