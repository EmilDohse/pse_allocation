package security;

import java.util.Optional;

import javax.inject.Inject;

import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;

import data.User;
import play.mvc.Http.Context;

/**
 * Management Klasse, welche verwendet wird um den Benutzer zu bekommen.
 * 
 * @author daniel
 *
 */
public class UserManagement {

    @Inject
    protected PlaySessionStore playSessionStore;

    /**
     * Methode um den in dem Webkontext gespeicherten Benutzer zu bekommen.
     * 
     * @param ctx
     *            den aktuellen Webkontext
     * @return den angemeldeten Benutzer
     */
    public User getUserProfile(Context ctx) {
        PlayWebContext webContext = new PlayWebContext(ctx, playSessionStore);
        ProfileManager<UserProfile> profileManager = new ProfileManager<UserProfile>(
                webContext);
        Optional<UserProfile> profile = profileManager.get(true);
        return profile.get().getUser();
    }
}
