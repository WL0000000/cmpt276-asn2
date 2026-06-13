package com.example.staffratings.repository;

import com.example.staffratings.enums.RoleType;
import com.example.staffratings.model.StaffRating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class StaffRatingRepositoryTest {

    @Autowired
    private StaffRatingRepository repository;

    @Test
    void saveAndFindById() {
        StaffRating rating = sampleRating("Ada Lovelace", "ada@sfu.ca");
        StaffRating saved = repository.save(rating);

        Optional<StaffRating> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ada Lovelace");
        assertThat(found.get().getEmail()).isEqualTo("ada@sfu.ca");
        assertThat(found.get().getCreatedAt()).isNotNull();
    }

    @Test
    void deleteRemovesEntry() {
        StaffRating saved = repository.save(sampleRating("Grace Hopper", "grace@sfu.ca"));

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    @Test
    void duplicateEmailIsRejected() {
        repository.save(sampleRating("First User", "duplicate@sfu.ca"));

        StaffRating duplicate = sampleRating("Second User", "duplicate@sfu.ca");

        assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(duplicate));
    }

    private StaffRating sampleRating(String name, String email) {
        StaffRating rating = new StaffRating();
        rating.setName(name);
        rating.setEmail(email);
        rating.setRoleType(RoleType.PROF);
        rating.setClarity(8);
        rating.setNiceness(9);
        rating.setKnowledgeableScore(10);
        rating.setComment("Great lecturer");
        return rating;
    }
}
