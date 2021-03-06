package security;

import java.util.Optional;

import javax.inject.Inject;

import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;

import data.Student;
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
    PlaySessionStore playSessionStore;
    /**
     * Methode um den in dem Webkontext gespeicherten Benutzer zu bekommen.
     * 
     * @param ctx
     *            den aktuellen Webkontext
     * @return den angemeldeten Benutzer
     */
    public <T extends User> T getUserProfile(Context ctx) {
        PlayWebContext webContext = new PlayWebContext(ctx,
                playSessionStore);
        ProfileManager<UserProfile<T>> profileManager = new ProfileManager<>(
                webContext);
        Optional<UserProfile<T>> profile = profileManager.get(true);
        if (profile.isPresent()) {
            return profile.get().getUser();
        } else {
            return null;
        }

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
        PlayWebContext webContext = new PlayWebContext(ctx,
                playSessionStore);
        ProfileManager<UserProfile<Student>> profileManager = new ProfileManager<>(
                webContext);
        Optional<UserProfile<Student>> profile = profileManager.get(true);
        if (profile.isPresent()
                && profile.get().getRoles().contains("ROLE_STUDENT_OLD")) {
            profile.get().addRole("ROLE_STUDENT");
            profileManager.save(true, profile.get(), false);
        }
    }
}
