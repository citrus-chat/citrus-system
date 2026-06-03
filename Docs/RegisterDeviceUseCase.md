RegisterDeviceUseCase

Cuando un dispositivo se registra:

Cliente
├─ Identity Key
├─ Signed PreKey
└─ 100 One Time PreKeys

Backend:

Crear UserDevice
Guardar Identity
Guardar SignedPreKey
Guardar OneTimePreKeys