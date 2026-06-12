# CLAUDE.md — CMPT 276 Assignment 2

## What This Project Is
A "Rate CS Teaching Staff" CRUD web app built with Java Spring Boot, Thymeleaf, and PostgreSQL.
Users can create, view, edit, and delete ratings for TAs / Profs / Instructors / Staff.
Deployed on Render with a Render Postgres database.

## Tech Stack
- Java 17+ / Spring Boot 3.x / Maven
- Spring Data JPA (Hibernate) → PostgreSQL
- Thymeleaf (server-side HTML templates)
- Bean Validation (Jakarta) for form validation
- JUnit 5 + MockMvc + @DataJpaTest for testing

## Key Domain Entities

### StaffRating (JPA entity — the only persisted model)
| Field               | Type    | Constraints                              |
|---------------------|---------|------------------------------------------|
| id                  | Long    | auto-generated PK                        |
| name                | String  | @NotBlank, @Size(min=2, max=100)         |
| email               | String  | @NotBlank, @Email, unique DB constraint  |
| roleType            | Enum    | TA / PROF / INSTRUCTOR / STAFF           |
| clarity             | Integer | @NotNull, @Min(1), @Max(10)              |
| niceness            | Integer | @NotNull, @Min(1), @Max(10)              |
| knowledgeableScore  | Integer | @NotNull, @Min(1), @Max(10)             |
| comment             | String  | optional, @Size(max=500)                 |
| createdAt           | LocalDateTime | auto-set on persist               |
| updatedAt           | LocalDateTime | auto-set on update                |

Overall score = average of clarity + niceness + knowledgeableScore (calculated, not stored).

### OO Design Requirement
- Abstract class (or interface) `StaffMemberProfile` with `displayTitle()` method
- Concrete subclasses: `TaProfile`, `ProfProfile` (minimum two)
- Used in Thymeleaf detail page to show role-specific display title

## URL / Controller Map
| Method | URL                  | Action                          |
|--------|----------------------|---------------------------------|
| GET    | /                    | list all ratings (index)        |
| GET    | /staff/new           | show create form                |
| POST   | /staff               | save new rating                 |
| GET    | /staff/{id}          | detail view                     |
| GET    | /staff/{id}/edit     | show edit form                  |
| POST   | /staff/{id}/edit     | save edits                      |
| POST   | /staff/{id}/delete   | delete rating, redirect to /    |

## Validation Rules (all server-side)
1. name — non-empty, 2–100 chars
2. email — valid format (@Email), non-empty, unique (handle DB constraint in controller)
3. clarity / niceness / knowledgeableScore — integer, 1–10, required
4. comment — max 500 chars (optional)
All forms must redisplay with per-field error messages on failure (use `th:errors`).
Never crash (500) on bad user input.

## Testing Strategy (slice tests — see TESTING.md)
Three required categories:
1. **Validation** — Bean Validation on entity or via MockMvc POST with invalid data
2. **Controller** — @WebMvcTest + MockMvc: GET index (200 + model), POST create (success redirect / failure form)
3. **Persistence** — @DataJpaTest: save/retrieve, delete, unique email constraint

Run all tests: `mvn test`

## Environment Variables (never hardcode credentials)
```
DATABASE_URL      — JDBC URL (Render sets this as DATABASE_URL in postgres:// format, needs conversion to jdbc:postgresql://)
DB_USERNAME       — postgres username
DB_PASSWORD       — postgres password
```
Local dev: copy `application-local.properties.example` → `application-local.properties` (gitignored).

## Deployment (Render)
- Render Web Service: Java / Maven build
- Render Postgres: attach to the web service, set env vars
- Build command: `mvn package -DskipTests`
- Start command: `java -jar target/*.jar`

## Deliverables Checklist
- [ ] CRUD fully working at Render URL
- [ ] Validation with error messages on all forms
- [ ] OO polymorphism (StaffMemberProfile hierarchy used in UI)
- [ ] Test suite passing (`mvn test`) — covers all 3 categories
- [ ] TESTING.md — explains approach and how to run
- [ ] README.md — "How to run locally" + "How to deploy" sections
- [ ] No secrets committed (DB creds in env vars only)
- [ ] rana276 added as GitHub collaborator (marking TA)
- [ ] Canvas submission: single text file with Render URL + GitHub URL + Known Issues

## Marking Breakdown
| Area                          | Points |
|-------------------------------|--------|
| CRUD correctness + UX clarity | 8      |
| Validation + error handling   | 3      |
| Database + persistence        | 2 (rubric shows 3 — go by rubric) |
| Test suite quality            | 4      |
| OO design / polymorphism      | 3      |

## Known Issues / Future Work (update before submission)
- No average-per-instructor aggregation yet (intentional for iteration 1)
- No authentication — all actions are public
