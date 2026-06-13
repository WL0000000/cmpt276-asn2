Phase 1: Foundation (get data flowing)
1. Local database setup
Copy application-local.properties.example → application-local.properties
Create a local Postgres DB
Why first: nothing else can be verified without a working DB connection



2. StaffRating entity
All fields from the assignment (id, name, email, roleType, scores, comment, createdAt, updatedAt)
Jakarta validation annotations (@NotBlank, @Email, @Min, @Max, etc.)
Unique constraint on email
getOverallScore() helper (average of the 3 scores — calculated, not stored)
@PrePersist / @PreUpdate for timestamps
Why: this is your single source of truth for validation and persistence



3. StaffRatingRepository
JpaRepository<StaffRating, Long>
Why: controller needs this before any CRUD works
Phase 2: Core CRUD (biggest marks — 8 pts)


4. StaffRatingController — Read first
GET / → list all ratings
GET /staff/{id} → detail view
Why: read is simplest (no form binding/validation yet); you can confirm DB + JPA work


5. index.html + detail.html
Index: name, roleType, overall score, link to detail
Detail: all fields, Edit + Delete buttons
Why: you can manually test list/detail before tackling forms


6. Create flow
GET /staff/new → create form
POST /staff → save + redirect to / on success
create.html with th:field, th:errors
Why: create is the first write operation; once it works, edit reuses the same patterns


7. Update flow
GET /staff/{id}/edit → edit form (pre-filled)
POST /staff/{id}/edit → save changes
edit.html (can share a fragment with create)
Why: edit is create + loading existing data + handling validation redisplay


8. Delete flow
POST /staff/{id}/delete → delete + redirect to /
Confirmation on detail page (form with “Are you sure?” or similar)
Why: delete is last because it’s one endpoint and depends on detail page existing



Phase 3: Validation + error handling (3 pts)
9. Server-side validation end-to-end
@Valid on controller POST methods
Redisplay forms with errors on failure (preserve user input)
Handle duplicate email DB exception → friendly field error (no 500)
Why: validation only matters once CRUD routes exist; doing it earlier means fixing things twice
Phase 4: OO design / polymorphism (3 pts)


10. StaffMemberProfile hierarchy
Abstract class or interface with displayTitle()
TaProfile, ProfProfile (and optionally InstructorProfile, StaffProfile)
Factory/helper: RoleType → correct profile instance
Use displayTitle() on detail or index page
Why: independent of CRUD plumbing; easier to add once detail page works


Phase 5: Tests (4 pts)
11. Persistence tests (@DataJpaTest)
Save and retrieve
Delete removes entry
Duplicate email rejected
Why: tests the data layer in isolation — fast, no web layer needed


12. Validation tests
Invalid email rejected
Out-of-range score rejected
Missing required fields rejected
Why: can test on entity directly or via MockMvc; confirms annotations work


13. Controller tests (@WebMvcTest + MockMvc)
GET / → 200 + model has ratings
POST /staff success → redirect
POST /staff failure → form with errors
Why: last test layer; mocks repository, verifies HTTP behavior



14. TESTING.md
Which test style you chose (slice tests) and why
How to run: mvn test
Why: required deliverable; write it while tests are fresh



Phase 6: Polish + deploy
15. UX polish
styles.css (you already have the file)
Empty state on index (“No ratings yet”)
Consistent nav/links across pages
Why: cosmetic; don’t block CRUD on this


16. README.md
How to run locally
How to deploy on Render
Env vars (DATABASE_URL, DB_USERNAME, DB_PASSWORD)
Why: required for submission; write after you’ve actually run it locally


17. Deploy to Render
Render Web Service + Postgres
Set env vars (no secrets in repo)
Build: mvn package -DskipTests
Start: java -jar target/*.jar
Why: deploy only when local CRUD + tests pass — avoids debugging prod and local at once


18. Final submission prep
Add TA rana276 as GitHub collaborator
Canvas text file: Render URL, GitHub URL, Known Issues / Future Work
Why: last step once everything works