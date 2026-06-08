# GetDevicePreKeysUseCase

## Propósito

`GetDevicePreKeysUseCase` es un caso de uso interno encargado de recuperar el conjunto de claves criptográficas (PreKey Bundle) asociado a un único dispositivo registrado en la plataforma.

Este caso de uso forma parte de la implementación del protocolo Signal y proporciona toda la información necesaria para que otro cliente pueda establecer una sesión cifrada con dicho dispositivo.

## Responsabilidades

El caso de uso debe:

1. Verificar que el dispositivo solicitado exista y se encuentre activo.
2. Obtener la Identity Key pública del dispositivo.
3. Obtener la Signed PreKey activa del dispositivo.
4. Obtener una One-Time PreKey disponible, si existe.
5. Marcar la One-Time PreKey como consumida para evitar reutilizaciones.
6. Persistir el cambio de estado de la One-Time PreKey.
7. Construir y devolver el PreKey Bundle correspondiente.

## Entrada

```java
public record GetDevicePreKeysCommand(
    UUID deviceId
) {
}
```

### Parámetros

| Campo    | Descripción                                                                |
| -------- | -------------------------------------------------------------------------- |
| deviceId | Identificador único del dispositivo del cual se desean obtener las claves. |

---

## Salida

```java
public record GetDevicePreKeysResult(
    UUID deviceId,
    String identityKey,
    int signedPreKeyId,
    String signedPreKey,
    String signedPreKeySignature,
    Integer oneTimePreKeyId,
    String oneTimePreKey
) {
}
```

### Datos retornados

| Campo                 | Descripción                                                    |
| --------------------- | -------------------------------------------------------------- |
| deviceId              | Identificador del dispositivo.                                 |
| identityKey           | Clave pública de identidad del dispositivo.                    |
| signedPreKeyId        | Identificador de la Signed PreKey activa.                      |
| signedPreKey          | Clave pública de la Signed PreKey.                             |
| signedPreKeySignature | Firma de la Signed PreKey realizada con la Identity Key.       |
| oneTimePreKeyId       | Identificador de la One-Time PreKey consumida. Puede ser null. |
| oneTimePreKey         | Clave pública de la One-Time PreKey consumida. Puede ser null. |

---

## Flujo de ejecución

1. Buscar el dispositivo activo.
2. Obtener la Identity Key asociada al dispositivo.
3. Obtener la Signed PreKey activa.
4. Buscar una One-Time PreKey disponible.
5. Si existe una One-Time PreKey:

    * Marcarla como consumida.
    * Persistir el cambio.
6. Construir el resultado.
7. Retornar el bundle al solicitante.

---

## Manejo de One-Time PreKeys

Las One-Time PreKeys son consumidas durante la ejecución de este caso de uso.

Cuando una One-Time PreKey es incluida en la respuesta:

* Se marca como utilizada.
* Se registra la fecha de consumo.
* No podrá volver a ser entregada en futuras solicitudes.

Si el dispositivo no posee One-Time PreKeys disponibles, el bundle seguirá siendo válido utilizando únicamente:

* Identity Key
* Signed PreKey

Este comportamiento es consistente con el protocolo Signal.

---

## Excepciones

### Dispositivo inexistente o inactivo

Se produce cuando no existe un dispositivo activo con el identificador especificado.

```java
IllegalArgumentException
```

---

### Identity Key inexistente

Se produce cuando el dispositivo no posee una Identity Key registrada.

```java
IllegalStateException
```

---

### Signed PreKey inexistente

Se produce cuando el dispositivo no posee una Signed PreKey activa.

```java
IllegalStateException
```

---

# Relación con GetUserPreKeyBundlesUseCase

## Contexto

Aunque este caso de uso puede utilizarse de forma independiente, su principal propósito es servir como bloque de construcción para el caso de uso de nivel superior:

```text
GetUserPreKeyBundlesUseCase
```

## Responsabilidad de GetUserPreKeyBundlesUseCase

Permitir que un cliente obtenga los bundles criptográficos de todos los dispositivos activos pertenecientes a un usuario.

### Flujo esperado

```text
Usuario destino
 ├── Dispositivo A
 ├── Dispositivo B
 └── Dispositivo C
```

Para cada dispositivo activo:

```text
GetUserPreKeyBundlesUseCase
            │
            ▼
GetDevicePreKeysUseCase(deviceA)
GetDevicePreKeysUseCase(deviceB)
GetDevicePreKeysUseCase(deviceC)
```

El resultado final será una colección de bundles, uno por dispositivo activo.

## Motivo de esta separación

La división entre ambos casos de uso permite:

* Reutilizar la lógica de construcción de bundles.
* Mantener una única implementación del proceso de consumo de One-Time PreKeys.
* Simplificar las pruebas unitarias.
* Facilitar futuras extensiones del protocolo Signal.
* Seguir el principio de responsabilidad única (SRP).

## Uso esperado desde el cliente

Cuando un usuario desea iniciar una conversación cifrada con otro usuario, normalmente no solicita las claves de un dispositivo específico.

En su lugar solicita:

```text
GetUserPreKeyBundlesUseCase
```

que devuelve los bundles de todos los dispositivos activos del destinatario para permitir el cifrado multidispositivo.
