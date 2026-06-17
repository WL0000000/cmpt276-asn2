package com.example.staffratings.profile;

import com.example.staffratings.model.StaffRating;

public class InstructorProfile extends StaffMemberProfile {

    public InstructorProfile(StaffRating rating) {
        super(rating);
    }

    @Override
    public String displayTitle() {
        return "Instructor " + rating.getName();
    }
}
