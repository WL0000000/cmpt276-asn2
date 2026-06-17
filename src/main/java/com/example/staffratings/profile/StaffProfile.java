package com.example.staffratings.profile;

import com.example.staffratings.model.StaffRating;

public class StaffProfile extends StaffMemberProfile {

    public StaffProfile(StaffRating rating) {
        super(rating);
    }

    @Override
    public String displayTitle() {
        return "Staff " + rating.getName();
    }
}
