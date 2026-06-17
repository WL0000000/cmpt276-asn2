package com.example.staffratings.profile;

import com.example.staffratings.model.StaffRating;

public class TaProfile extends StaffMemberProfile {

    public TaProfile(StaffRating rating) {
        super(rating);
    }

    @Override
    public String displayTitle() {
        return "TA " + rating.getName();
    }
}
