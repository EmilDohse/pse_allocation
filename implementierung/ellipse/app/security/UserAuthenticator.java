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

import data.Administrator;
import data.Adviser;
import data.GeneralData;
import data.Student;
import play.mvc.Http.Context;

/**
 * Class for authenticating a user
 * 
 * @author daniel
 *
 */
public class UserAuthenticator
        implements Authenticator<UsernamePasswordCredentials> {

    private static final String USER_NO_VALID_CREDENTIALS = "user.noValidCredentials";
    private static final String ERROR                     = "error";
    private static final String BAD_CRED                  = "Bad credentials for: ";

    @Override
    public void validate(UsernamePasswordCredentials credentials,
            WebContext context) throws HttpAction {
        PasswordEncoder encoder = new BlowfishPasswordEncoder();
        PlayWebContext playContext = (PlayWebContext) context;
        Context ctx = playContext.getJavaContext();
        for (Administrator admin : Administrator.getAdministrators()) {
            if (credentials.getUsername().equals(admin.getUserName())) {
                if (encoder.matches(credentials.getPassword(),
                        admin.getPassword())) {
                    UserProfile<Administrator> profile = new UserProfile<>(
                            admin);
                    profile.addRole("ROLE_ADMIN");
                    credentials.setUserProfile(profile);
                    // Leite den Admin zur Passwort-Ändern-Seite, falls das
                    // Passwort noch das Start-Passwort ist
                    System.out.println("ADMIN");
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
                    ctx.session().put(ERROR,
                            ctx.messages().at(USER_NO_VALID_CREDENTIALS));
                    throw new BadCredentialsException(
                            BAD_CRED + credentials.getUsername());
                }
            }
        }
        for (Student student : GeneralData.loadInstance().getCurrentSemester()
                .getStudents()) {
            if (credentials.getUsername().equals(student.getUserName())) {
                if (encoder.matches(credentials.getPassword(),
                        student.getPassword())) {
                    UserProfile<Student> profile = new UserProfile<>(student);
                    profile.addRole("ROLE_STUDENT");
                    credentials.setUserProfile(profile);
                    if (!student.isEmailVerified()) {
                        ctx.flash().put("info", ctx.messages()
                                .at("student.info.notVerifiedEmail"));
                    }
                    context.setSessionAttribute(Pac4jConstants.REQUESTED_URL,
                            "/student");
                    return;
                } else {
                    ctx.session().put(ERROR,
                            ctx.messages().at(USER_NO_VALID_CREDENTIALS));
                    throw new BadCredentialsException(
                            BAD_CRED + credentials.getUsername());
                }
            }
        }
        for (Adviser adviser : Adviser.getAdvisers()) {
            if (credentials.getUsername().equals(adviser.getUserName())) {
                if (encoder.matches(credentials.getPassword(),
                        adviser.getPassword())) {
                    UserProfile<Adviser> profile = new UserProfile<>(adviser);
                    profile.addRole("ROLE_ADVISER");
                    credentials.setUserProfile(profile);
                    context.setSessionAttribute(Pac4jConstants.REQUESTED_URL,
                            "/adviser");
                    return;
                } else {
                    ctx.session().put(ERROR,
                            ctx.messages().at(USER_NO_VALID_CREDENTIALS));
                    throw new BadCredentialsException(
                            BAD_CRED + credentials.getUsername());
                }
            }
        }
        for (Student student : Student.getStudents()) {
            if (credentials.getUsername().equals(student.getUserName())) {
                if (encoder.matches(credentials.getPassword(),
                        student.getPassword())) {
                    UserProfile<Student> profile = new UserProfile<>(student);
                    profile.addRole("ROLE_STUDENT_OLD");
                    credentials.setUserProfile(profile);
                    context.setSessionAttribute(Pac4jConstants.REQUESTED_URL,
                            controllers.routes.StudentPageController
                                    .changeFormPage().path());
                    return;
                } else {
                    ctx.session().put(ERROR,
                            ctx.messages().at(USER_NO_VALID_CREDENTIALS));
                    throw new BadCredentialsException(
                            BAD_CRED + credentials.getUsername());
                }
            }
        }
        throw new AccountNotFoundException(
                "No account found for: " + credentials.getUsername());
    }

}