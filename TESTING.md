# Testing

## Approach: slice tests

This project uses **slice tests** — each test targets one layer of the application in
isolation rather than booting the whole app for every case. This keeps the suite fast,
makes failures point directly at the layer at fault, and avoids needing a running
database or server for most tests.

Three slices are covered, matching the three concerns in the app:

| Slice | Annotation | What it isolates |
|-------|-----------|------------------|
| Validation | none (plain `Validator`) | Bean Validation annotations on the entity |
| Persistence | `@DataJpaTest` | JPA mapping + repository against an in-memory DB |
| Web / Controller | `@WebMvcTest` | HTTP routing, binding, redirects, error handling |

## How to run

```bash
mvn test
```

(or `./mvnw test` / `mvnw.cmd test` to use the bundled Maven wrapper).

Run a single class:

```bash
mvn -Dtest=StaffRatingControllerTest test
```

## Test classes

### 1. `StaffRatingValidationTest` (10 tests) — validation slice
Builds a `Validator` directly (no Spring context, no DB) and validates a `StaffRating`.
Each test starts from a fully valid object and mutates exactly one field invalid:

- valid object produces zero violations (baseline)
- invalid email rejected (`@Email`)
- score above max (11) and below min (0) rejected (`@Max`/`@Min`)
- missing/blank name, missing email, missing role, missing score rejected (`@NotBlank`/`@NotNull`)
- comment over 500 chars rejected (`@Size`)

### 2. `StaffRatingRepositoryTest` (3 tests) — persistence slice
`@DataJpaTest` against an in-memory H2 database:

- save then retrieve by id (and timestamps populated)
- delete removes the entry
- duplicate email throws `DataIntegrityViolationException` (unique constraint)

### 3. `StaffRatingControllerTest` (8 tests) — controller slice
`@WebMvcTest` with the repository mocked (`@MockitoBean`), no real database:

- `GET /` → 200, view `index`, model has `ratings`
- `GET /staff/new` → 200, blank form + role options
- `GET /staff/{id}` → 200, detail view, polymorphic `displayTitle` in model
- `POST /staff` valid → redirect to `/`, repository saved
- `POST /staff` invalid → 200, redisplay `create` with field errors, never saved
- `POST /staff` duplicate email → friendly email error, no 500
- `POST /staff/{id}/edit` valid → redirect to detail
- `POST /staff/{id}/delete` → redirect to `/`, `deleteById` called

### 4. `StaffratingsApplicationTests` (1 test)
Default `@SpringBootTest` smoke test confirming the application context loads.

## Test database

Web and validation tests need no database. The persistence slice uses an in-memory
**H2** database (test scope only) configured under `src/test/resources`, so `mvn test`
runs without a local PostgreSQL instance.

## Current status

All tests pass: **22 tests, 0 failures, 0 errors.**
