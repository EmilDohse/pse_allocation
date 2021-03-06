// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;

import com.google.inject.Inject;

import data.Achievement;
import data.ElipseModel;
import data.GeneralData;
import data.LearningGroup;
import data.Project;
import data.Rating;
import data.SPO;
import data.Semester;
import data.Student;
import deadline.StateStorage;
import exception.ValidationException;
import form.Forms;
import form.IntValidator;
import form.StringValidator;
import notificationSystem.Notifier;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import security.BlowfishPasswordEncoder;
import security.EmailVerifier;
import security.UserManagement;
import views.Menu;
import views.StudentMenu;

/************************************************************/
/**
 * Dieser Controller ist zuständig für alle Http-Requests, welche im
 * Studentenbereich aufkommen. Dazu zählen das Senden einer neuen HTML-Seite bei
 * einem Klick auf einen Link, als auch das Reagieren auf Benutzereingaben, wie
 * das Abschicken eines Formulars.
 */
public class StudentPageController extends Controller {

    private static final String STATE_ACTION_NOT_ALLOWED = "state.actionNotAllowed";
    private static final String ERROR                    = "error";
    private static final String INTERNAL_ERROR           = "error.internalError";
    private static final String GEN_ERROR                = "index.registration.error.genError";
    private static final String ALREADY_IN_OTHER_GROUP   = "student.learningGroup.error.alreadyInOtherGroup";

    @Inject
    FormFactory                 formFactory;

    @Inject
    Notifier                    notifier;

    @Inject
    UserManagement              userManagement;

    /**
     * Diese Seite stellt das Formular dar, das ein Student ausfüllen muss, wenn
     * er zwar einen Account hat, aber nicht im aktuellen PSE-Semester ist. Hier
     * darf er dann seine Studierendendaten aktualisieren.
     * 
     * @param error
     *            Fehlermeldung, die angezeigt werden soll
     * @return Die Seite, die angezeigt wird.
     */
    public Result changeFormPage() {
        play.twirl.api.Html content = views.html.studentChangeData.render(
                GeneralData.loadInstance().getCurrentSemester().getSpos());
        Menu menu = new Menu();
        return ok(views.html.student.render(menu, content));
    }

    /**
     * Hier wird das Formular aus {@code changeFormPage} ausgewertet und er wird
     * wenn alles korrekt ist in das Studentenportal weitergeleitet wird.
     * 
     * @return die Seite, die angezeigt wird.
     */
    public Result changeData() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        } else {
            // die felder werden ausgelesen
            String semesterString = form.get("semester");
            String spoIdString = form.get("spo");
            int spoId;
            int semester;
            try {
                IntValidator validator = new IntValidator(0);
                semester = validator.validate(semesterString);
                spoId = validator.validate(spoIdString);
            } catch (ValidationException e) {
                flash(ERROR, ctx().messages().at(e.getMessage()));
                return redirect(controllers.routes.StudentPageController
                        .changeFormPage());
            }
            SPO spo = ElipseModel.getById(SPO.class, spoId);
            boolean trueData = false;

            if (form.get("trueData") != null) {
                // wenn der student angekreuzt hat dass seine Angaben der
                // Wahrheit entsprechen
                trueData = true;
            }
            List<Achievement> completedAchievements;
            List<Achievement> nonCompletedAchievements;
            try {
                completedAchievements = MultiselectList.createAchievementList(
                        form, "completed-" + spoIdString + "-multiselect");
            } catch (NumberFormatException e) {
                flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
                return redirect(controllers.routes.StudentPageController
                        .changeFormPage());
            }
            try {
                nonCompletedAchievements = MultiselectList
                        .createAchievementList(form,
                                "due-" + spoIdString + "-multiselect");
            } catch (NumberFormatException e) {
                flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
                return redirect(controllers.routes.StudentPageController
                        .changeFormPage());
            }
            if (trueData) {
                List<Achievement> temp = new ArrayList<>(completedAchievements);
                temp.addAll(nonCompletedAchievements);
                if (temp.containsAll(spo.getNecessaryAchievements())) {
                    Student student = userManagement.getUserProfile(ctx());
                    student.doTransaction(() -> {
                        student.setSPO(spo);
                        student.setSemester(semester);
                        student.setCompletedAchievements(completedAchievements);
                        student.setOralTestAchievements(
                                nonCompletedAchievements);
                    });
                    LearningGroup l = new LearningGroup(student.getUserName(),
                            "");
                    l.save();
                    l.doTransaction(() -> {
                        l.addMember(student);
                        l.setPrivate(true);
                        // Ratings kopieren
                        for (Project p : GeneralData.loadInstance()
                                .getCurrentSemester().getProjects()) {
                            l.rate(p, 3);
                        }
                    });

                    Semester currentSemester = GeneralData.loadInstance()
                            .getCurrentSemester();
                    currentSemester.doTransaction(() -> {
                        currentSemester.addLearningGroup(l);
                        currentSemester.addStudent(student);
                    });
                    userManagement.addStudentRoleToOldStudent(ctx());
                    return redirect(controllers.routes.StudentPageController
                            .learningGroupPage());
                }
                return redirect(controllers.routes.StudentPageController
                        .learningGroupPage());
            }
            flash(ERROR, ctx().messages().at("error.registration.trueDate"));
            return redirect(
                    controllers.routes.StudentPageController.changeFormPage());
        }

    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Student sieht in welcher
     * Lerngruppe er ist, oder wenn er in keiner aktuell ist, eine erstellen
     * oder einer beitreten kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result learningGroupPage() {
        Student student = userManagement.getUserProfile(ctx());
        play.twirl.api.Html content = views.html.studentLearningGroup
                .render(GeneralData.loadInstance().getCurrentSemester()
                        .getLearningGroupOf(student));
        Menu menu = new StudentMenu(ctx(), ctx().request().path());
        return ok(views.html.student.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Student seine
     * Bewertungen abgeben kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result ratingPage() {
        Student student = userManagement.getUserProfile(ctx());
        play.twirl.api.Html content = views.html.studentRating.render(student,
                GeneralData.loadInstance().getCurrentSemester().getProjects());
        Menu menu = new StudentMenu(ctx(), ctx().request().path());
        return ok(views.html.student.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Student das Ergebnis der
     * Einteilungsberechnung einsehen kann. Er sieht also sein Projekt und seine
     * Teammitglieder.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result resultsPage() {
        if (GeneralData.loadInstance().getCurrentSemester()
                .getFinalAllocation() == null) {
            play.twirl.api.Html content = views.html.noAllocationYet.render();
            Menu menu = new StudentMenu(ctx(), ctx().request().path());
            return ok(views.html.student.render(menu, content));

        }
        Student student = userManagement.getUserProfile(ctx());
        play.twirl.api.Html content = views.html.studentResult
                .render(GeneralData.loadInstance().getCurrentSemester()
                        .getFinalAllocation().getTeam(student));
        Menu menu = new StudentMenu(ctx(), ctx().request().path());
        return ok(views.html.student.render(menu, content));
    }

    /**
     * Diese Methode fügt die Daten der Bewertungen eines Studenten in das
     * System ein und leitet den Studenten wieder zurück auf die
     * Bewertungsseite, wo er nun seine eingegebene Bewertungen sehen kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result rate() {
        synchronized (LearningGroup.class) {
            Student student = userManagement.getUserProfile(ctx());
            DynamicForm form = formFactory.form().bindFromRequest();
            if (form.data().isEmpty()) {
                return badRequest(ctx().messages().at(INTERNAL_ERROR));
            }
            LearningGroup lg = GeneralData.loadInstance().getCurrentSemester()
                    .getLearningGroupOf(student);
            lg.doTransaction(() -> {
                for (Project project : GeneralData.loadInstance()
                        .getCurrentSemester().getProjects()) {
                    lg.rate(project, Integer.parseInt(
                            form.get(Integer.toString(project.getId()))));
                    // holt sich das rating des studenten aus dem formular
                }
            });
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage());
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn der Student einer Lerngruppe
     * beitreten oder eine erstellen will.
     * 
     * @return Die Seite, die als Antwort verschickt wird
     */
    public Result setLearningGroup() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.get("create") != null) {
            return createLearningGroup();
        } else if (form.get("join") != null) {
            return joinLearningGroup();
        } else {
            flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage());
        }
    }

    /**
     * Diese Methode erstellt eine neue Lerngruppe im System und fügt den
     * Ersteller der Lerngruppe als erstes Mitglied in diese ein. Der Student
     * wird anschließend auf die Lerngruppen-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    private Result createLearningGroup() {
        Student student = userManagement.getUserProfile(ctx());
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        if (!semester.getLearningGroupOf(student).isPrivate()) {
            flash(ERROR, ctx().messages().at(ALREADY_IN_OTHER_GROUP));
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage());
        }
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        String name = form.get("learningGroupname");
        if (name.matches("\\d*")) {
            // Wenn Name leer ist oder nur aus Ziffern besteht
            flash(ERROR, ctx().messages()
                    .at("student.learningGroup.error.nameFormat"));
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage());
        }
        StringValidator passwordValidator = Forms.getPasswordValidator();

        String password;
        try {
            password = passwordValidator
                    .validate(form.get("learningGroupPassword"));
        } catch (ValidationException e) {
            flash(ERROR, ctx().messages().at(e.getMessage()));
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage());
        }
        String encPassword = new BlowfishPasswordEncoder().encode(password);
        LearningGroup learningGroup = LearningGroup.getLearningGroup(name,
                semester);
        if (learningGroup != null) {
            flash(ERROR, ctx().messages()
                    .at("student.learningGroup.error.existsAlready"));
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage());
        }
        LearningGroup oldLg = semester.getLearningGroupOf(student);
        LearningGroup lg = new LearningGroup(name, encPassword);
        lg.save();
        lg.doTransaction(() -> {
            lg.addMember(student);
            lg.setPrivate(false);
            // Ratings kopieren
            for (Rating r : oldLg.getRatings()) {
                lg.rate(r.getProject(), r.getRating());
            }
        });
        // Lösche die private Lerngruppe
        oldLg.doTransaction(() -> {
            oldLg.getMembers().remove(0);
        });
        oldLg.delete();
        semester.refresh();
        semester.doTransaction(() -> {
            semester.addLearningGroup(lg);
        });
        return redirect(
                controllers.routes.StudentPageController.learningGroupPage());
    }

    /**
     * Diese Methode entfernt den Student aus der aktuellen Lerngruppe.
     * Anschließend wird der Student auf die Lerngruppen-Seite zurück geleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result leaveLearningGroup() {
        synchronized (LearningGroup.class) {
            Student student = userManagement.getUserProfile(ctx());
            LearningGroup lg = GeneralData.loadInstance().getCurrentSemester()
                    .getLearningGroupOf(student);
            if (lg.isPrivate()) {
                flash(ERROR, ctx().messages()
                        .at("student.learningGroup.error.noLearningGroup"));
                return redirect(controllers.routes.StudentPageController
                        .learningGroupPage());
            }

            lg.doTransaction(() -> {
                lg.removeMember(student);
            });
            lg.refresh();
            // Hier wird der student wieder in seine private Lerngruppe
            // eingefügt
            LearningGroup lgNew = new LearningGroup(student.getUserName(), "");
            lgNew.save();
            lgNew.doTransaction(() -> {
                lgNew.addMember(student);
                lgNew.setPrivate(true);
                // Ratings kopieren
                for (Rating r : lg.getRatings()) {
                    lgNew.rate(r.getProject(), r.getRating());
                }
            });
            if (lg.getMembers().size() == 0) {
                // Leeres Team löschen
                lg.delete();
            }
            Semester semester = GeneralData.loadInstance().getCurrentSemester();
            semester.doTransaction(() -> {
                semester.addLearningGroup(lgNew);
            });
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage());
        }
    }

    /**
     * Diese Methode fügt den Studenten zu einer Lerngruppe hinzu, falls eine
     * Lerngruppe mit dem Namen und dem zugehörigen Passwort existiert und die
     * Lerngruppe noch nicht größergleich der maximalen Lerngruppenqröße ist .
     * Anschließend wird der Student auf die Lerngruppen-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    private Result joinLearningGroup() {
        synchronized (LearningGroup.class) {
            Student student = userManagement.getUserProfile(ctx());
            DynamicForm form = formFactory.form().bindFromRequest();
            if (form.data().isEmpty()) {
                return badRequest(ctx().messages().at(INTERNAL_ERROR));
            }
            StringValidator stringValidator = Forms
                    .getNonEmptyStringValidator();
            String name;
            try {
                name = stringValidator.validate(form.get("learningGroupname"));
            } catch (ValidationException e) {
                flash(ERROR, ctx().messages().at(e.getMessage()));
                return redirect(controllers.routes.StudentPageController
                        .learningGroupPage());
            }
            String pw = form.get("learningGroupPassword");
            LearningGroup lgOld = GeneralData.loadInstance()
                    .getCurrentSemester().getLearningGroupOf(student);
            LearningGroup lgNew = LearningGroup.getLearningGroup(name,
                    GeneralData.loadInstance().getCurrentSemester());
            if (lgNew == null) {
                flash(ERROR, ctx().messages().at(
                        "student.learningGroup.error.learningGroupDoesntExist"));
                return redirect(controllers.routes.StudentPageController
                        .learningGroupPage());
            }
            // Wenn die Lerngruppe bereits voll ist, wird ein Fehler
            // zurückgegeben
            if (lgNew.getMembers().size() >= GeneralData.loadInstance()
                    .getCurrentSemester().getMaxGroupSize()) {
                flash(ERROR, ctx().messages()
                        .at("student.learningGroup.error.learningGroupFull"));
                return redirect(controllers.routes.StudentPageController
                        .learningGroupPage());
            }
            if (!lgOld.isPrivate()) {
                flash(ERROR, ctx().messages().at(ALREADY_IN_OTHER_GROUP));
                return redirect(controllers.routes.StudentPageController
                        .learningGroupPage());
            }
            if (lgNew.isPrivate()) {
                flash(ERROR, ctx().messages()
                        .at("student.learningGroup.error.joinProhibited"));
                return redirect(controllers.routes.StudentPageController
                        .learningGroupPage());
            }

            if (new BlowfishPasswordEncoder().matches(pw,
                    lgNew.getPassword())) {
                lgOld.doTransaction(() -> {
                    lgOld.getMembers().remove(0);
                });
                lgOld.delete(); // die private lerngruppe wird gelöscht
                lgNew.doTransaction(() -> {
                    lgNew.addMember(student);
                });
                return redirect(controllers.routes.StudentPageController
                        .learningGroupPage());
            } else {
                flash(ERROR, ctx().messages()
                        .at("student.learningGroup.error.wrongPW"));
                return redirect(controllers.routes.StudentPageController
                        .learningGroupPage());
            }
        }
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Student seine
     * Studentendaten wie E-Mail-Adresse und Passwort ändern kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result accountPage() {
        Student student = userManagement.getUserProfile(ctx());
        play.twirl.api.Html content = views.html.studentAccount.render(student);
        Menu menu = new StudentMenu(ctx(), ctx().request().path());
        return ok(views.html.student.render(menu, content));
    }

    /**
     * Diese Methode editiert die Daten des Studenten, welche er auf der
     * Account-Seite geändert hat.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result editAccount() {
        Student student = userManagement.getUserProfile(ctx());
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        if (form.get("passwordChange") != null) {
            String oldpw = form.get("oldPassword");
            String pw;
            try {
                StringValidator validator = Forms.getPasswordValidator();
                pw = validator.validate(form.get("newPassword"));
            } catch (ValidationException e) {
                flash(ERROR, ctx().messages().at(e.getMessage()));
                return redirect(
                        controllers.routes.StudentPageController.accountPage());
            }
            String pwrepeat = form.get("newPasswordRepeat");

            boolean matches = new BlowfishPasswordEncoder().matches(oldpw,
                    student.getPassword());

            if (!matches) {
                flash(ERROR, ctx().messages()
                        .at("student.account.error.pwsDontMatch"));
                return redirect(
                        controllers.routes.StudentPageController.accountPage());
            }
            if (!pw.equals(pwrepeat)) {
                flash(ERROR,
                        ctx().messages().at("student.account.error.wrongPW"));
                return redirect(
                        controllers.routes.StudentPageController.accountPage());
            }

            String pwEnc = new BlowfishPasswordEncoder().encode(pw);
            student.doTransaction(() -> {
                student.setPassword(pwEnc);
            });
            flash("info",
                    ctx().messages().at("admin.account.success.passwords"));
        }
        if (form.get("emailChange") != null) {
            String email;
            try {
                StringValidator emailValidator = Forms.getEmailValidator();
                email = emailValidator.validate(form.get("newEmail"));
            } catch (ValidationException e) {
                flash(ERROR, ctx().messages().at(e.getMessage()));
                return redirect(
                        controllers.routes.StudentPageController.accountPage());
            }
            student.doTransaction(() -> {
                student.setEmailAddress(email);
                student.setIsEmailVerified(false);
            });
            String verificationCode = EmailVerifier.getInstance()
                    .getVerificationCode(student);
            // Versuche Verifikationsmail zu schicken.
            try {
                String protocol = request().secure() ? "https://" : "http://";
                String url = request().host()
                        + controllers.routes.IndexPageController
                                .verificationPage(verificationCode).url();
                notifier.sendVerificationMail(student, protocol + url);
                flash("info",
                        ctx().messages().at("admin.account.success.email"));
                return redirect(controllers.routes.StudentPageController
                        .sendNewVerificationLink());
            } catch (EmailException e) {
                return redirect(
                        controllers.routes.StudentPageController.accountPage());
            }
        }
        return redirect(controllers.routes.StudentPageController.accountPage());
    }

    /**
     * Diese Methode verschickt einen neuen Verifikations-Code an die aktuelle
     * E-Mail-Adresse.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result sendNewVerificationLink() {
        Student student = userManagement.getUserProfile(ctx());
        String verificationCode = EmailVerifier.getInstance()
                .getVerificationCode(student);
        try {
            String protocol;
            if (request().secure()) {
                protocol = "https://";
            } else {
                protocol = "http://";
            }
            String url = request().host()
                    + controllers.routes.IndexPageController
                            .verificationPage(verificationCode).url();
            notifier.sendVerificationMail(student, protocol + url);
            flash("info", ctx().messages()
                    .at("student.email.verificationLinkSuccess"));
        } catch (EmailException e) {
            e.printStackTrace();
            // EmailException
            // wird gethrowed teilweise, obwohl Mail-Versand
            // funktioniert hat
        }
        return redirect(controllers.routes.StudentPageController.accountPage());
    }

    /**
     * Methode, welche zum Redirect verwendet wird, wenn die Aktion (der
     * Request) im aktuellen Zustand (s. deadline).
     * 
     * @param url
     *            die url zum Redirecten
     * @return die Seite, die angezeigt werden soll
     */
    public Result notAllowedInCurrentState(String url) {
        switch (StateStorage.getInstance().getCurrentState()) {
        case AFTER_REGISTRATION_PHASE:
            flash("info", ctx().messages()
                    .at("student.afterRegistration.actionNotAllowed"));
            break;
        default:
            flash("info", ctx().messages().at(STATE_ACTION_NOT_ALLOWED));
            break;
        }

        return redirect(url);
    }
}
