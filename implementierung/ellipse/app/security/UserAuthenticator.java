package security;

import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.exception.AccountNotFoundException;
import org.pac4j.core.exception.BadCredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.play.PlayWebContext;

import ch.qos.logback.core.net.SyslogOutputStream;
import data.Administrator;
import data.Adviser;
import data.GeneralData;
import data.Student;
import data.User;
import play.mvc.Http.Context;

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
        PlayWebContext playContext = (PlayWebContext) context;
        Context ctx = playContext.getJavaContext();
        for (User u : Administrator.getAdministrators()) {
            if (credentials.getUsername().equals(u.getUserName())) {
                if (encoder.matches(credentials.getPassword(),
                        u.getPassword())) {
                    UserProfile profile = new UserProfile(u);
                    profile.addRole("ROLE_ADMIN");
                    credentials.setUserProfile(profile);
                    // Leite den Admin zur Passwort-Ändern-Seite, falls das
                    // Passwort noch das Start-Passwort ist
                    if (credentials.getPassword()
                            .equals(Administrator.START_PASSWORD)) {
                        ctx.flash().put("error", ctx.messages()
                                .at("admin.account.pleaseChangePassword"));
                        context.setSessionAttribute(
                                Pac4jConstants.REQUESTED_URL,
                                controllers.routes.AdminPageController
                                        .accountPage().path());
                    } else {
                        context.setSessionAttribute(
                                Pac4jConstants.REQUESTED_URL, "/admin");
                    }
                    return;
                } else {
                    ctx.flash().put("error",
                            ctx.messages().at("user.noVlidCredentials"));
                    throw new BadCredentialsException("Bad credentials for: "
                            + credentials.getUsername());
                }
            }
        }
        for (Student u : GeneralData.loadInstance().getCurrentSemester()
                .getStudents()) {
            if (credentials.getUsername().equals(u.getUserName())) {
                if (encoder.matches(credentials.getPassword(),
                        u.getPassword())) {
                    UserProfile profile = new UserProfile(u);
                    profile.addRole("ROLE_STUDENT");
                    credentials.setUserProfile(profile);
                    if (!u.isEmailVerified()) {
                        ctx.flash().put("info", ctx.messages()
                                .at("student.info.notVerifiedEmail"));
                    }
                    context.setSessionAttribute(Pac4jConstants.REQUESTED_URL,
                            "/student");
                    return;
                } else {
                    ctx.flash().put("error",
                            ctx.messages().at("user.noVlidCredentials"));
                    throw new BadCredentialsException("Bad credentials for: "
                            + credentials.getUsername());
                }
            }
        }
        for (User u : Adviser.getAdvisers()) {
            if (credentials.getUsername().equals(u.getUserName())) {
                System.out.println("Username matsches");
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
        for (User u : Student.getStudents()) {
            if (credentials.getUsername().equals(u.getUserName())) {
                if (encoder.matches(credentials.getPassword(),
                        u.getPassword())) {
                    UserProfile profile = new UserProfile(u);
                    profile.addRole("ROLE_STUDENT_OLD");
                    credentials.setUserProfile(profile);
                    context.setSessionAttribute(Pac4jConstants.REQUESTED_URL,
                            controllers.routes.StudentPageController
                                    .changeFormPage().path());
                    return;
                } else {
                    ctx.flash().put("error",
                            ctx.messages().at("user.noVlidCredentials"));
                    throw new BadCredentialsException("Bad credentials for: "
                            + credentials.getUsername());
                }
            }
        }
        throw new AccountNotFoundException(
                "No account found for: " + credentials.getUsername());
    }

}