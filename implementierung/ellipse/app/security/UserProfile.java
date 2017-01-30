package security;

import org.pac4j.core.profile.CommonProfile;

import data.User;

public class UserProfile extends CommonProfile {

    private User user;

    public UserProfile(User user) {
        super();
        this.user = user;
    }
}