package com.example.staffratings.profile;

import com.example.staffratings.model.StaffRating;

/** Maps a {@link StaffRating}'s role to the matching {@link StaffMemberProfile} subclass. */
public final class StaffMemberProfileFactory {

    private StaffMemberProfileFactory() {
    }

    public static StaffMemberProfile from(StaffRating rating) {
        return switch (rating.getRoleType()) {
            case TA -> new TaProfile(rating);
            case PROF -> new ProfProfile(rating);
            case INSTRUCTOR -> new InstructorProfile(rating);
            case STAFF -> new StaffProfile(rating);
        };
    }
}
