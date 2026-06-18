package com.example.staffratings.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a staff rating id in the URL does not exist. Mapped to HTTP 404 so
 * bad/stale ids render the friendly error page instead of a 500.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StaffRatingNotFoundException extends RuntimeException {

    public StaffRatingNotFoundException(Long id) {
        super("No staff rating found with id " + id);
    }
}
