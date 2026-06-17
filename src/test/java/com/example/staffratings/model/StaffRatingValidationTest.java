package com.example.staffratings.model;

import com.example.staffratings.enums.RoleType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validates the Jakarta Bean Validation annotations on {@link StaffRating} in isolation —
 * no Spring context or database, so these run fast.
 */
class StaffRatingValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    /** Builds a fully valid rating that each test then mutates into one invalid state. */
    private StaffRating validRating() {
        StaffRating rating = new StaffRating();
        rating.setName("Ada Lovelace");
        rating.setEmail("ada@sfu.ca");
        rating.setRoleType(RoleType.PROF);
        rating.setClarity(8);
        rating.setNiceness(9);
        rating.setKnowledgeableScore(10);
        rating.setComment("Great lecturer");
        return rating;
    }

    private boolean hasViolationFor(StaffRating rating, String property) {
        return validator.validate(rating).stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(property));
    }

    @Test
    void validRatingHasNoViolations() {
        Set<ConstraintViolation<StaffRating>> violations = validator.validate(validRating());
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidEmailIsRejected() {
        StaffRating rating = validRating();
        rating.setEmail("not-an-email");

        assertThat(hasViolationFor(rating, "email")).isTrue();
    }

    @Test
    void scoreAboveMaxIsRejected() {
        StaffRating rating = validRating();
        rating.setClarity(11);

        assertThat(hasViolationFor(rating, "clarity")).isTrue();
    }

    @Test
    void scoreBelowMinIsRejected() {
        StaffRating rating = validRating();
        rating.setNiceness(0);

        assertThat(hasViolationFor(rating, "niceness")).isTrue();
    }

    @Test
    void missingNameIsRejected() {
        StaffRating rating = validRating();
        rating.setName(null);

        assertThat(hasViolationFor(rating, "name")).isTrue();
    }

    @Test
    void blankNameIsRejected() {
        StaffRating rating = validRating();
        rating.setName("  ");

        assertThat(hasViolationFor(rating, "name")).isTrue();
    }

    @Test
    void missingEmailIsRejected() {
        StaffRating rating = validRating();
        rating.setEmail(null);

        assertThat(hasViolationFor(rating, "email")).isTrue();
    }

    @Test
    void missingRoleTypeIsRejected() {
        StaffRating rating = validRating();
        rating.setRoleType(null);

        assertThat(hasViolationFor(rating, "roleType")).isTrue();
    }

    @Test
    void missingScoreIsRejected() {
        StaffRating rating = validRating();
        rating.setKnowledgeableScore(null);

        assertThat(hasViolationFor(rating, "knowledgeableScore")).isTrue();
    }

    @Test
    void commentOverMaxLengthIsRejected() {
        StaffRating rating = validRating();
        rating.setComment("x".repeat(501));

        assertThat(hasViolationFor(rating, "comment")).isTrue();
    }
}
