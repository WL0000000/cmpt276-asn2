package com.example.staffratings.repository;

import com.example.staffratings.model.StaffRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRatingRepository extends JpaRepository<StaffRating, Long> {

    Optional<StaffRating> findByEmail(String email);
}
