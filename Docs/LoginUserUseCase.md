# LoginUseCase

## Descripción

El `LoginUseCase` es el responsable de autenticar usuarios dentro del módulo de identidad.

Durante el proceso:

* Se valida que el usuario exista.
* Se validan las credenciales.
* Se registra o actualiza el dispositivo desde el cual se realiza el login.
* Se actualiza la última actividad del dispositivo.
* Se genera un JWT de autenticación.
* Se retorna la información de sesión del usuario y del dispositivo autenticado.

---

## Objetivo

Centralizar el proceso completo de autenticación de usuarios y vinculación de dispositivos.

Al finalizar exitosamente:

* El usuario queda autenticado.
* El dispositivo queda registrado o actualizado.
* La clave pública actual del dispositivo queda almacenada.
* El cliente recibe un JWT válido.
* El sistema conoce desde qué dispositivo se originó la sesión.

---

## Flujo de autenticación

```text
Request
  ↓
Controller
  ↓
LoginUseCase
  ↓
Validación de usuario
  ↓
Validación de contraseña
  ↓
Registro / actualización de dispositivo
  ↓
Generación JWT
  ↓
Response
```

---

## Registro de dispositivo

Durante el login el cliente debe informar información del dispositivo que intenta autenticarse.

Información requerida:

```java
UUID deviceId
String deviceName
DeviceType deviceType
PublicKey publicKey
```

La operación es delegada a:

```java
RegisterOrRefreshUserDeviceUseCase
```

Responsabilidades:

* Registrar un nuevo dispositivo si no existe.
* Actualizar la información de un dispositivo existente.
* Actualizar la clave pública del dispositivo.
* Actualizar la fecha de última actividad.
* Asociar el dispositivo al usuario autenticado.

---

## Clave pública del dispositivo

Cada dispositivo mantiene una clave pública única.

```java
PublicKey
```

Esta clave será utilizada posteriormente para:

* Intercambio seguro de claves.
* Distribución de bundles criptográficos.
* Establecimiento de sesiones End-to-End Encryption.
* Verificación de identidad del dispositivo.

Actualmente el login registra o actualiza esta clave durante cada autenticación.

---

## JWT

Luego de autenticar al usuario y registrar el dispositivo, se genera un JWT utilizando:

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

El token será utilizado para acceder a endpoints protegidos.

---

## Validaciones implementadas

El caso de uso valida:

* El usuario debe existir.
* La contraseña debe ser correcta.
* El dispositivo debe contener información válida.
* El dispositivo debe poder registrarse correctamente.

Si alguna validación falla se lanza la excepción correspondiente.

Ejemplos:

```java
UserNotFoundException
```

```java
InvalidCredentialsException
```

o excepciones derivadas del registro de dispositivos.

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
UUID deviceId
String accessToken
String tokenType
long expiresIn
```

---

## Flujo interno

```text
Buscar usuario
    ↓
Validar contraseña
    ↓
Registrar o actualizar dispositivo
    ↓
Generar JWT
    ↓
Retornar LoginResult
```

---

## Dependencias

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

---

## Testing

Actualmente existen tests para:

### Casos válidos

* Login exitoso.
* Validación correcta de credenciales.
* Generación correcta del JWT.
* Registro correcto del dispositivo.
* Actualización correcta de dispositivo existente.
* Retorno correcto de información de sesión.
* Verificación del orden de ejecución del flujo.

### Casos inválidos

* Usuario inexistente.
* Contraseña inválida.
* Dispositivo inválido.

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
  "email": "admin@citruschat.com",
  "password": "Admin123!",
  "deviceRequest": {
    "deviceId": "7fdcc08c-9f97-4352-b673-6a1c73cadc1c",
    "deviceName": "Google Chrome Web",
    "deviceType": "WEB",
    "publicKey": "XS+UkYt/iNkNlruc6jyuD9BxGR46qawzJ4k2tTfMIDc="
  }
}
```

---

## Respuesta exitosa

```json
{
  "userId": "91ae5825-9096-4c74-9447-1bf03004c36b",
  "email": "admin@citruschat.com",
  "username": "admin",
  "deviceId": "7fdcc08c-9f97-4352-b673-6a1c73cadc1c",
  "accessToken": "jwt-token",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

---

## Autenticación posterior

Una vez obtenido el JWT, el cliente deberá enviarlo en cada petición protegida.

Ejemplo:

```http
Authorization: Bearer <jwt>
```

Spring Security utilizará el token para reconstruir el contexto de autenticación del usuario.

---

## Consideraciones de diseño

### Dispositivos persistentes

Cada dispositivo posee una identidad propia dentro del sistema.

Esto permite:

* Múltiples dispositivos por usuario.
* Revocación individual de dispositivos.
* Seguimiento de actividad por dispositivo.
* Gestión independiente de claves públicas.

### Actualización de claves públicas

Durante el login la clave pública informada por el cliente se sincroniza con la almacenada por el servidor.

Esto permite:

* Detectar rotaciones de claves.
* Mantener información criptográfica actualizada.
* Preparar futuras funcionalidades de cifrado End-to-End.

### DeviceId como identificador estable

El dispositivo es identificado mediante:

```java
DeviceId
```

y no mediante sesiones temporales.

Esto permite reconocer un mismo dispositivo a través de múltiples autenticaciones.

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
* JPA/Hibernate
* Device-Based Authentication
