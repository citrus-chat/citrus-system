# CODEX.md

Guidance for Codex and future contributors working in this repository.

## Project Snapshot

`citrus-system` is the central backend for CitrusChat: a Spring Boot REST and WebSocket API for an enterprise chat system consumed by mobile and web clients.

Core stack:

- Java 25
- Spring Boot 4.0.5
- Maven wrapper (`./mvnw`)
- Spring MVC, Spring Security, OAuth2 Resource Server JWT validation
- Spring WebSocket/STOMP
- Spring Data JPA/JDBC
- PostgreSQL for runtime, H2 for tests
- Lombok
- Springdoc OpenAPI
- Spotless, Checkstyle, Surefire

Runtime defaults:

- Server port: `8200`
- API base path: `/api/v1`
- WebSocket endpoint: `/ws`
- App STOMP prefix: `/app`
- Topic prefix: `/topic`
- PostgreSQL defaults: database `citruschat`, user `postgres`, password `admin`

## Commands

Use the Maven wrapper from the repository root.

```bash
./mvnw spring-boot:run
./mvnw test
./mvnw spotless:apply
./mvnw checkstyle:check
./mvnw verify
```

For local infrastructure:

```bash
docker compose up postgres
docker compose up
```

The pre-commit hook runs:

```bash
mvn spotless:apply
mvn checkstyle:check
mvn test
```

Enable it once with:

```bash
git config core.hooksPath hooks/
```

## Architecture

The codebase is organized by bounded context and follows Clean/Hexagonal Architecture.

Primary contexts:

- `identity`: users, login, validation, devices, JWT/session identity, admin access
- `messaging`: chat rooms, participants, roles, permissions, messages, per-device encrypted payloads, WebSocket delivery
- `shared`: cross-cutting value objects, constants, API response wrappers, security/web configuration, global exception handling

Within each context, keep the dependency direction strict:

- `domain`: enterprise rules, entities, value objects, factories, domain exceptions, policies
- `application`: commands, results, ports, use cases
- `infrastructure`: Spring controllers, DTOs, mappers, JPA entities/repositories, security, WebSocket adapters, bean configuration

Do not make domain or application code depend on web DTOs, JPA entities, Spring MVC, WebSocket APIs, or persistence implementation details. Infrastructure adapts external input/output into application commands/results.

## Layering Rules

Domain:

- Put invariant checks in domain models, value objects, factories, and domain exceptions.
- Prefer domain value objects such as `UserId`, `DeviceId`, `ChatRoomId`, `MessageId`, `PublicKey`, `UserEmail`, `Username`, and `PhoneNumber` over raw primitives inside domain/application code.
- Use factories for creation/reconstitution when a module already has them.
- Keep domain objects framework-light. Lombok is already used for getters/equality, but do not add persistence or web annotations to domain models.

Application:

- Use cases expose `execute(...)` methods and take command records/classes.
- Use ports from `application/ports` to access persistence or external services.
- Return result records/classes from `application/results`.
- Use cases should orchestrate business flow, enforce permissions/ownership, and call repositories through ports.
- Do not return JPA entities or web response DTOs from use cases.

Infrastructure:

- Controllers should be thin: validate request shape, map DTO to command, call one use case, map result to response.
- Keep routes in `shared/infrastructure/constants/ApiRoutes.java`.
- Wrap REST success responses with `ApiResponses` and `ApiResponse<T>`.
- Centralize REST error shape through `GlobalExceptionHandler`.
- Put request/response DTOs under `infrastructure/web/dto` or `infrastructure/websocket/dto`.
- Put DTO-command-result mapping in mapper classes such as `LoginWebMapper`, `ChatWebSocketMapper`, and `CreateChatRoomWebMapper`.
- JPA adapters implement application ports and translate through mapper classes. Spring Data repositories stay behind adapters.
- Bean composition for use cases/factories/mappers lives in module configuration classes such as `IdentityBeansConfiguration` and `MessagingBeansConfiguration`.

## REST API Conventions

- API routes should use `/api/v1`.
- Add or modify paths in `ApiRoutes`; controllers should reference constants, not hard-coded route strings.
- Public/auth/docs routes should be aligned with `SecurityRoutes` and `SecurityConfiguration`.
- Use Jakarta Bean Validation on request DTOs for syntactic validation.
- Use domain value objects and use-case validation for business validation.
- Return success through `ApiResponses.ok(...)`, `ApiResponses.created(...)`, or `ApiResponses.status(...)`.
- Keep response DTOs stable for mobile and web clients. Treat field renames and shape changes as API contract changes.
- Prefer explicit request/response records over maps or loose JSON structures.

## WebSocket Conventions

Current flow:

- Client logs in through REST and receives `accessToken` plus `deviceId`.
- Client connects to `/ws` with JWT.
- Client sends messages to `/app/chat/sendMessage`.
- Server broadcasts to `/topic/chatrooms/{chatRoomId}`.

Security and validation expectations:

- Validate JWT during handshake/channel processing.
- Use `Authentication.getName()` as the current user id when that is how the JWT subject is populated.
- Check device ownership before accepting client-provided `senderDeviceId`.
- Check chat participation before accepting room actions.
- Reject invalid WebSocket requests before calling use cases.
- Never trust `senderUserId` from the WebSocket payload; derive it from authentication.

## Messaging And E2EE Rules

Messaging is designed around multi-device encrypted delivery.

- A message belongs to a chat room, sender user, and sender device.
- Message content should be delivered as per-target-device encrypted payloads.
- Do not add server-side plaintext message content unless the product/security model explicitly changes.
- Validate that every target payload references a valid active device in the chat room.
- Exclude the sending device from the target device list unless a specific sync feature intentionally changes that behavior.
- Preserve permission checks such as `ChatPermissionList.CAN_SEND_MESSAGE`.
- Keep chat role/permission logic in the domain/policy layer, not in controllers.

## Identity And Device Rules

Login responsibilities include:

- Find user by email.
- Validate password.
- Register or refresh the client device.
- Persist/update the device public key and last activity.
- Generate a JWT containing user/device identity.
- Return user id, email, username, device id, access token, token type, and expiration.

Device expectations:

- Devices are first-class identity records.
- A login request includes device metadata and a public key.
- Use `RegisterOrRefreshUserDeviceUseCase` for device registration/update.
- Use `ValidateUserDeviceOwnershipUseCase` before accepting operations tied to a `deviceId`.
- Do not create duplicate device logic in controllers.

## Security

- HTTP is stateless; keep sessions disabled.
- JWT-protected endpoints should rely on Spring Security/OAuth2 Resource Server.
- Do not add public routes casually. If a route must be public, update `SecurityRoutes` intentionally and document why.
- Do not log passwords, JWTs, private keys, encrypted payloads, or public-key material beyond safe identifiers.
- If touching security configuration, prefer moving secrets/config values to properties/environment variables rather than hard-coding new secrets.
- Validate both authentication and authorization. Being authenticated is not enough for chat room/device operations.

## Persistence

- Runtime persistence uses PostgreSQL.
- Tests use H2 through `src/test/resources/application.properties`.
- `spring.jpa.open-in-view=false`, so fetch/mapping behavior should be explicit.
- Current dev config uses `spring.jpa.hibernate.ddl-auto=create-drop` and `data.sql`; be careful with production assumptions.
- Keep database table names centralized where existing constants are used.
- JPA entities live under `infrastructure/persistence/jpa/entity`.
- Spring Data repositories live under `infrastructure/persistence/jpa/repository`.
- Application code should depend on repository ports, not Spring Data interfaces.

## Error Handling

- Prefer project-specific `BusinessException` subclasses with `ErrorCode` for expected business failures.
- `GlobalExceptionHandler` maps business exceptions, validation failures, malformed JSON, `IllegalArgumentException`, and unexpected exceptions into API error responses.
- Do not expose sensitive internal details in client-facing errors.
- When replacing `IllegalArgumentException` in use cases, prefer a typed exception that can map to a useful HTTP status and stable error code.

## Testing Expectations

Match test scope to risk:

- Domain/value-object/factory change: focused unit tests for invariants and edge cases.
- Use-case change: unit tests with mocked ports, verifying success, failure, and important call ordering when relevant.
- Controller/API contract change: `MockMvc` tests for route, status, response shape, validation, and auth behavior.
- Security change: integration tests with valid, missing, and invalid JWTs.
- Persistence adapter/query change: repository or integration-style tests against H2 where feasible.
- WebSocket behavior change: test mapper/use-case logic at minimum; add integration coverage when security/routing changes.

Existing test style:

- JUnit 5
- Mockito for port mocking
- `@SpringBootTest` + `@AutoConfigureMockMvc` for integration/security tests
- AssertJ is not currently the dominant style; follow existing JUnit assertions unless introducing a clear local pattern.

Before finishing meaningful code changes, run at least:

```bash
./mvnw test
```

For formatting/lint-sensitive changes, also run:

```bash
./mvnw spotless:apply
./mvnw checkstyle:check
```

If environment restrictions prevent running checks, report the exact command that could not be run and the reason.

## Style And Formatting

- Keep code formatted by Spotless.
- Checkstyle is currently light, but still run it when touching Java code.
- Use constructor injection.
- Prefer records for simple immutable commands, results, and DTOs when consistent with nearby code.
- Keep comments useful and sparse. Do not narrate obvious assignments.
- Keep controllers, mappers, use cases, and adapters small and single-purpose.
- Do not introduce broad refactors while fixing a narrow bug.
- Preserve package naming under `com.javaee2026.citruschat`.
- Prefer explicit class names matching the local pattern: `XxxUseCase`, `XxxCommand`, `XxxResult`, `XxxWebMapper`, `JpaXxxRepositoryAdapter`, `SpringDataXxxRepository`.

## API Contract Discipline

This backend has mobile and web clients. Treat exposed REST and WebSocket payloads as contracts.

- Verify DTO field names before changing clients or server mappings.
- Do not add "guessing" parsers or alternate field fallbacks on the server without a compatibility reason.
- Prefer one canonical API shape and update OpenAPI/docs/tests with contract changes.
- Keep `Docs/` use-case documents in sync when a flow changes meaningfully.
- Preserve response envelope conventions unless the task explicitly changes them.

## Common File Map

- App entrypoint: `src/main/java/com/javaee2026/citruschat/CitruschatApplication.java`
- API routes: `src/main/java/com/javaee2026/citruschat/shared/infrastructure/constants/ApiRoutes.java`
- Security routes/config: `src/main/java/com/javaee2026/citruschat/shared/infrastructure/constants/SecurityRoutes.java`, `src/main/java/com/javaee2026/citruschat/shared/infrastructure/configuration/SecurityConfiguration.java`
- REST response helpers: `src/main/java/com/javaee2026/citruschat/shared/infrastructure/web/ApiResponses.java`
- Error handler: `src/main/java/com/javaee2026/citruschat/shared/infrastructure/web/GlobalExceptionHandler.java`
- Identity composition root: `src/main/java/com/javaee2026/citruschat/identity/infrastructure/configuration/IdentityBeansConfiguration.java`
- Messaging composition root: `src/main/java/com/javaee2026/citruschat/messaging/infrastructure/configuration/MessagingBeansConfiguration.java`
- WebSocket config: `src/main/java/com/javaee2026/citruschat/messaging/infrastructure/websocket/configuration`
- Use-case docs: `Docs/`
- Runtime config: `src/main/resources/application.properties`
- Test config: `src/test/resources/application.properties`

## Working Notes For Future Prompts

- Read the relevant use case, command/result, mapper, controller, port, adapter, and tests before editing.
- Follow the existing module boundary instead of adding shortcuts across layers.
- When adding a feature, start from the API/WS contract, then command/result, use case, domain/persistence changes, mapper, controller, tests, and docs.
- When fixing a bug, identify whether the source of truth is the domain model, application use case, infrastructure mapper, persistence adapter, or security configuration before editing.
- When a change affects both REST and WebSocket behavior, keep validation and authorization consistent across both entry points.
- When adding dependencies, justify them; most backend work should fit the current Spring/JPA/Security stack.
