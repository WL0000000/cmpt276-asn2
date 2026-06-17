package com.example.staffratings.profile;

import com.example.staffratings.model.StaffRating;

public class ProfProfile extends StaffMemberProfile {

    public ProfProfile(StaffRating rating) {
        super(rating);
    }

    @Override
    public String displayTitle() {
        return "Prof. " + rating.getName();
    }
}
