# spring-crud-generator-demo

This project was generated using [Spring CRUD Generator](https://github.com/mzivkovicdev/spring-crud-generator).

## Build & Test

```bash
# Build
mvn clean install -DskipTests

# Build with tests
mvn clean install

# Run unit tests
mvn test
```

## Run

```bash
mvn spring-boot:run
```

Or with Docker Compose (starts app + database + cache):

```bash
docker-compose up -d
```

## Architecture

Spring Boot application with a layered architecture generated from `src/main/resources/crud-spec.yaml`.

**Database:** POSTGRESQL
**Cache:** REDIS
**APIs:** REST + GraphQL

### Generated Layers

Package names are configurable via the `packages` section in `crud-spec.yaml`.

| Package Key | Purpose |
|---------|---------|
| `packages.models` | JPA entities (database tables) |
| `packages.repositories` | Spring Data JPA repositories |
| `packages.services` | CRUD service interfaces and implementations |
| `packages.businessservices` | Business logic layer |
| `packages.transferobjects` | Transfer Objects (request/response DTOs) |
| `packages.mappers` | Entity ↔ DTO mappers |
| `packages.controllers` | REST controllers |
| `packages.configurations` | Application configuration |
| `packages.exceptions` | Exception types and error handling |
| `packages.resolvers` | GraphQL resolvers and schema |
### Entities

- `ProductModel`
- `OrderTable`
- `UserEntity`
- `Details`
- `ProductDetails`
## API Documentation

OpenAPI specs: `src/main/resources/swagger/`
## GraphQL

Endpoint: `http://localhost:8080/graphql`

## Important Rules

- **Never edit generated files directly.** Changes will be overwritten on next generator run. Re-run with `mvn clean install -Pgenerate-resources` instead.
- Custom business logic goes in the package mapped by `packages.businessservices` or in new classes outside generated packages.
- `crud-spec.yaml` defines all entities, fields, and configuration — this is the single source of truth.
- `generator-state.json` must not be deleted (tracks incremental generation state).
- Flyway migration files in `src/main/resources/db/migration/` must never be renamed or reordered.
- **REDIS must be running** before starting the application.

## Scope

Keep changes focused on explicit task requirements and avoid speculative rewrites.

## Workflow

- Understand the task before editing code.
- Prefer small, reviewable changes with clear intent.
- Preserve existing architecture and conventions unless a refactor is explicitly requested.

## Code Quality

- Keep code readable, typed, and production-ready.
- Add concise comments only when intent is not obvious from code.

## Git Guidelines

- Use feature branches for all non-trivial changes.
- Use Conventional Commit messages for every commit.
- Require pull requests with human review before merging.

## Delivery Guidelines

- Run relevant tests before considering the task complete.
- Keep changes within the requested scope and avoid unrelated rewrites.
