Me hice este archivo de lista para tareas pendientes:

1- Modificar UserDevice para manejar encriptación ⌛
   - Definir las clases:
     - UserDevice ✅ 
     - ├── DeviceIdentity ✅
     - ├──────── Repository ✅
     - ├──────── JPA ✅
     - ├──────── Mapper ✅
     - ├── DeviceSignedPreKey ✅
     - ├──────── Repository ✅
     - ├──────── JPA ✅
     - ├──────── Mapper ✅
     - ├── DeviceOneTimePreKey ✅
     - ├──────── Repository ✅
     - ├──────── JPA ✅
     - └───────── Mapper ✅

2- Implementar el caso de uso RegisterDeviceUseCase ✅
   - Evaluar si debe cambiarse el Login o incluir el UseCase dentro del mismo. ✅

3- Implementar el caso de uso UploadPreKeysUseCase ❌

4- Implementar el caso de uso GetDevicePreKeysUseCase ❌

5- Implementar endpoint para /messages ❌
   - Definir separación entre mensajes directos y grupales ❌

6- Implementar el caso de uso CreateChatRoom para mensajes directos ❌

7- Modificar el caso de uso SendMessageUseCase para mensajes directos ❌

8- Crear el caso de uso SendGroupMessageUseCase para mensajes grupales ❌
   - Implementar SenderKeys para mandar mensajes grupales ❌
