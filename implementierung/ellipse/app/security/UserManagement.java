package security;

import java.util.Optional;

import javax.inject.Inject;

import org.pac4j.core.config.ConfigSingleton;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;

import data.User;
import play.mvc.Http.Context;

/**
 * Management Klasse, welche verwendet wird um den Benutzer zu bekommen.
 * 
 * @author daniel
 *
 */
public class UserManagement {

    /**
     * Methode um den in dem Webkontext gespeicherten Benutzer zu bekommen.
     * 
     * @param ctx
     *            den aktuellen Webkontext
     * @return den angemeldeten Benutzer
     */
    public User getUserProfile(Context ctx) {
        @SuppressWarnings("unchecked")
        PlayWebContext webContext = new PlayWebContext(ctx,
                ConfigSingleton.getConfig().getSessionStore());
        ProfileManager<UserProfile> profileManager = new ProfileManager<UserProfile>(
                webContext);
        Optional<UserProfile> profile = profileManager.get(true);
        return profile.get().getUser();
    }

    /**
     * Diese Klasse fügt dem aktuellen angemeldeten User noch die Rolle des
     * Studenten zu, jedoch nur einem "alten" Studenten, also einem Studenten
     * der in der Datenbank existiert, jedoch nicht im alten Semester ist.
     * 
     * @param ctx
     *            der aktuelle Kontext
     */
    public void addStudentRoleToOldStudent(Context ctx) {
        @SuppressWarnings("unchecked")
        PlayWebContext webContext = new PlayWebContext(ctx,
                ConfigSingleton.getConfig().getSessionStore());
        ProfileManager<UserProfile> profileManager = new ProfileManager<UserProfile>(
                webContext);
        Optional<UserProfile> profile = profileManager.get(true);
        if (profile.get().getRoles().contains("ROLE_STUDENT_OLD")) {
            profile.get().addRole("ROLE_STUDENT");
            profileManager.save(true, profile.get(), false);
        }
    }
}
