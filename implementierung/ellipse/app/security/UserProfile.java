package security;

import org.pac4j.core.profile.CommonProfile;

import data.User;

/**
 * Erbt von pac4j CommonProfile die akutellen Credentials und speichert den
 * aktuellen User.
 */
public class UserProfile extends CommonProfile {

    private User user;

    public UserProfile(User user) {
        super();
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}