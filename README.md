# lab-javaee-2026
Laboratorio de Java EE 2026

# Cómo usar Git Hooks?
Dentro del directorio `hooks` podemos ver el `pre-commit` hook.
Este hook lo que hace es correr varios tests antes de commitear, para que no podamos mandar nada roto.
Para utilizarlo, hay que hacer lo siguiente:

- Tienen que tener configurado Git bash si estan en Windows. O usar Linux.
- Asegurarse de que en el `pom.xml` esten estos 3 plugins: Checkstyle (Linter), Spotless (Formatter) y Surefire (Tests).
- Usar el comando `git config core.hooksPath hooks/` (desde la raíz del proyecto)

Ahora, cada vez que hagan un commit, se van a correr esos 3 procesos. Pueden usar la opción `--no-verify` para saltearse esto, pero no lo hagan.