package security;

import org.pac4j.core.profile.CommonProfile;

import data.User;

/**
 * Erbt von pac4j CommonProfile die akutellen Credentials und speichert den
 * aktuellen User.
 */
public class UserProfile<T extends User> extends CommonProfile {

    private T user;

    public UserProfile(T user) {
        super();
        this.user = user;
    }

    public T getUser() {
        return user;
    }

}