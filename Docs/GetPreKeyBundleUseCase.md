GetPreKeyBundleUseCase
Cuando Alice quiere hablar con Bob:

Alice
  -> pide bundle de Bob Device A

Backend:

Busca:
- Identity Key
- Signed PreKey activa
- OneTimePreKey disponible

Marca OneTimePreKey como consumida
Devuelve bundle