package security;

import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.exception.AccountNotFoundException;
import org.pac4j.core.exception.BadCredentialsException;
import org.pac4j.core.exception.HttpAction;

import data.Administrator;
import data.Adviser;
import data.Student;
import data.User;

/**
 * Class for authenticating a user
 * 
 * @author daniel
 *
 */
public class UserAuthenticator
        implements Authenticator<UsernamePasswordCredentials> {

    @Override
    public void validate(UsernamePasswordCredentials credentials,
            WebContext context) throws HttpAction {
        PasswordEncoder encoder = new BlowfishPasswordEncoder();
        for (User u : Administrator.getAdministrators()) {
            if (credentials.getUsername().equals(u.getUserName())) {
                if (encoder.matches(credentials.getPassword(),
                        u.getPassword())) {
                    UserProfile profile = new UserProfile(u);
                    profile.addRole("ROLE_ADMIN");
                    credentials.setUserProfile(profile);
                    context.setSessionAttribute(Pac4jConstants.REQUESTED_URL,
                            "/admin");
                    return;
                } else {
                    throw new BadCredentialsException("Bad credentials for: "
                            + credentials.getUsername());
                }
            }
        }
        for (User u : Student.getStudents()) {
            if (credentials.getUsername().equals(u.getUserName())) {
                if (encoder.matches(credentials.getPassword(),
                        u.getPassword())) {
                    UserProfile profile = new UserProfile(u);
                    profile.addRole("ROLE_STUDENT");
                    credentials.setUserProfile(profile);
                    context.setSessionAttribute(Pac4jConstants.REQUESTED_URL,
                            "/student");
                    return;
                } else {
                    throw new BadCredentialsException("Bad credentials for: "
                            + credentials.getUsername());
                }
            }
        }
        for (User u : Adviser.getAdvisers()) {
            if (credentials.getUsername().equals(u.getUserName())) {
                if (encoder.matches(credentials.getPassword(),
                        u.getPassword())) {
                    UserProfile profile = new UserProfile(u);
                    profile.addRole("ROLE_ADVISER");
                    credentials.setUserProfile(profile);
                    context.setSessionAttribute(Pac4jConstants.REQUESTED_URL,
                            "/adviser");
                    return;
                } else {
                    throw new BadCredentialsException("Bad credentials for: "
                            + credentials.getUsername());
                }
            }
        }
        throw new AccountNotFoundException(
                "No account found for: " + credentials.getUsername());
    }

}