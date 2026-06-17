package com.example.staffratings.profile;

import com.example.staffratings.model.StaffRating;

/**
 * Abstract role profile for a staff member. Subclasses provide a polymorphic
 * {@link #displayTitle()} that renders the person's name with a role-appropriate prefix.
 */
public abstract class StaffMemberProfile {

    protected final StaffRating rating;

    protected StaffMemberProfile(StaffRating rating) {
        this.rating = rating;
    }

    /** Role-specific honorific + name, e.g. "Prof. Ada Lovelace". */
    public abstract String displayTitle();

    public StaffRating getRating() {
        return rating;
    }
}
