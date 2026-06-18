# Staff Ratings

A CRUD web app for rating CS teaching staff (TAs, professors, instructors, staff).
Built for CMPT 276 Assignment 2.

Each rating records a staff member's name, email, role, and three scores (clarity,
niceness, knowledgeable, 1–10). The app computes an overall score as the average of the
three and supports full create / read / update / delete with server-side validation.

## Tech stack

Java 21 · Spring Boot 3.5 · Spring MVC · Spring Data JPA · Thymeleaf ·
Jakarta Bean Validation · PostgreSQL (prod) / H2 (tests) · Maven · JUnit 5

## Features

- List all ratings with name, role, and overall score
- View, create, edit, and delete ratings
- Server-side validation with per-field error messages (form repopulates on failure)
- Friendly handling of the unique-email constraint (no 500 on duplicates)
- Polymorphic role profiles (`displayTitle()`) shown on the detail page

---

## Run locally

### Prerequisites
- JDK 21
- PostgreSQL running locally

### 1. Create the database
Create a PostgreSQL database named `staffratings` (one-time):

```sql
CREATE DATABASE staffratings;
```

### 2. Configure local credentials
Copy the example config and fill in your real Postgres password:

```bash
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

`application-local.properties` is **gitignored** — never commit credentials. Set:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/staffratings
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run
The `local` Spring profile is enabled automatically (configured in `pom.xml`):

```bash
mvn spring-boot:run
```

On Windows you can use the wrapper: `mvnw.cmd spring-boot:run`.

Then open <http://localhost:8080/>. Tables are created automatically on first run
(`spring.jpa.hibernate.ddl-auto=update`).

### Run tests

```bash
mvn test
```

Tests use an in-memory H2 database, so no local PostgreSQL is required to run them.
See [TESTING.md](TESTING.md) for details.

---

## Deploy on Render

The production config (`application.properties`) reads database credentials from
**environment variables** — no secrets are committed.

### 1. Create a PostgreSQL instance
In Render, create a new **PostgreSQL** database and note its connection details
(internal/external host, database name, user, password).

### 2. Create a Web Service
Create a new **Web Service** pointing at this GitHub repository, with:

- **Build command:** `mvn package -DskipTests`
- **Start command:** `java -jar target/*.jar`

### 3. Set environment variables
On the Web Service, set:

| Variable | Example |
|----------|---------|
| `DATABASE_URL` | `jdbc:postgresql://<host>:5432/<dbname>` |
| `DB_USERNAME` | `<db user>` |
| `DB_PASSWORD` | `<db password>` |

> Note: `DATABASE_URL` must be a **JDBC** URL (starts with `jdbc:postgresql://`). If Render
> gives you a `postgres://...` URL, convert it to the `jdbc:postgresql://host:port/db` form
> and supply the username/password separately via `DB_USERNAME` / `DB_PASSWORD`.

Render builds and starts the service automatically on each push to the deployed branch.

---

## Configuration reference

| Property | Source | Used by |
|----------|--------|---------|
| `spring.datasource.url` | `${DATABASE_URL}` env var | production |
| `spring.datasource.username` | `${DB_USERNAME}` env var | production |
| `spring.datasource.password` | `${DB_PASSWORD}` env var | production |
| (same three, set directly) | `application-local.properties` | local dev |

---

## Known issues / limitations

- **No authentication or authorization.** All create/edit/delete actions are public —
  anyone with the URL can modify any rating. Intentional for this iteration.
- **No per-instructor aggregation.** Each row is an independent rating; the app does not
  average multiple ratings for the same person or rank staff by score yet.
- **One rating per email.** The unique-email constraint means a given staff member can
  only be rated once. Submitting a duplicate email shows a friendly form error (no crash),
  but there's no way to record multiple ratings for the same person.
- **Free Render tier cold starts.** The deployed instance spins down when idle, so the
  first request after inactivity can take ~30–60s to respond while it wakes up.
- **No pagination or search.** The index page lists every rating; this is fine at small
  scale but would need paging for large datasets.

## AI usage declaration

