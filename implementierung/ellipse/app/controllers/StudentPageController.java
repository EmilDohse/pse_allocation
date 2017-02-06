// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.util.ArrayList;

import com.google.inject.Inject;

import data.GeneralData;
import data.LearningGroup;
import data.Project;
import data.Rating;
import data.Student;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import security.BlowfishPasswordEncoder;
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

    @Inject
    FormFactory formFactory;

    /**
     * Diese Methode gibt die Seite zurück, auf der der Student sieht in welcher
     * Lerngruppe er ist, oder wenn er in keiner aktuell ist, eine erstellen
     * oder einer beitreten kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result learningGroupPage(String error) {
        UserManagement user = new UserManagement();
        Student student = (Student) user.getUserProfile(ctx());
        play.twirl.api.Html content = views.html.studentLearningGroup
                .render(GeneralData.getInstance().getCurrentSemester()
                        .getLearningGroupOf(student), error);
        Menu menu = new StudentMenu(ctx(), ctx().request().path());
        return ok(views.html.student.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Student seine
     * Bewertungen abgeben kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result ratingPage(String error) {
        play.twirl.api.Html content = views.html.studentRating.render(
                GeneralData.getInstance().getCurrentSemester().getProjects(),
                error);
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
    public Result resultsPage(String error) {
        // TODO überprüft man no final allocation wirklich über ob es null ist?
        if (GeneralData.getInstance().getCurrentSemester()
                .getFinalAllocation() == null) {
            play.twirl.api.Html content = views.html.noAllocationYet.render();
            Menu menu = new StudentMenu(ctx(), ctx().request().path());
            return ok(views.html.student.render(menu, content));

        }
        UserManagement user = new UserManagement();
        Student student = (Student) user.getUserProfile(ctx());
        play.twirl.api.Html content = views.html.studentResult
                .render(GeneralData.getInstance().getCurrentSemester()
                        .getFinalAllocation().getTeam(student), error);
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
        UserManagement user = new UserManagement();
        Student student = (Student) user.getUserProfile(ctx());
        DynamicForm form = formFactory.form().bindFromRequest();
        LearningGroup lg = GeneralData.getInstance().getCurrentSemester()
                .getLearningGroupOf(student);
        ArrayList<Rating> ratings = new ArrayList<>();
        for (Project project : GeneralData.getInstance().getCurrentSemester()
                .getProjects()) {
            Rating rating = new Rating(
                    Integer.parseInt(
                            form.get(Integer.toString(project.getId()))),
                    project);
            // holt sich das rating des studenten aus dem formular
            ratings.add(rating);
        }
        lg.setRatings(ratings);
        return redirect(
                controllers.routes.StudentPageController.learningGroupPage(""));
    }

    /**
     * Diese Methode erstellt eine neue Lerngruppe im System und fügt den
     * Ersteller der Lerngruppe als erstes Mitglied in diese ein. Der Student
     * wird anschließend auf die Lerngruppen-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result createLearningGroup() {
        UserManagement user = new UserManagement();
        Student student = (Student) user.getUserProfile(ctx());
        DynamicForm form = formFactory.form().bindFromRequest();
        String name = form.get("learningGroupname");
        String password = form.get("learningGroupPassword");
        // TODO stimmt hier der rückgabewert in html
        if (!(LearningGroup.getLearningGroup(name,
                GeneralData.getInstance().getCurrentSemester()) == null)) {
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage(ctx().messages().at(
                            "student .learningGroup.error.existsAllready")));
        }
        LearningGroup lg = new LearningGroup(name, password, student, false);
        GeneralData.getInstance().getCurrentSemester()
                .getLearningGroupOf(student).delete();
        // TODO falls man die alten bewertungen wieder will muss man hier die
        // alte lerngruppe behalten
        GeneralData.getInstance().getCurrentSemester().addLearningGroup(lg);
        return redirect(
                controllers.routes.StudentPageController.learningGroupPage(""));
    }

    /**
     * Diese Methode entfernt den Student aus der aktuellen Lerngruppe.
     * Anschließend wird der Student auf die Lerngruppen-Seite zurück geleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result leaveLearningGroup() {
        UserManagement user = new UserManagement();
        Student student = (Student) user.getUserProfile(ctx());
        if (GeneralData.getInstance().getCurrentSemester()
                .getLearningGroupOf(student).getMembers().size() == 1) {
            LearningGroup lg = new LearningGroup(
                    "private" + student.getUserName(), "", student, true);
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage(""));
        } // hier wurde der student wieder in seine privat elerngruppe eingefügt
        LearningGroup lg = GeneralData.getInstance().getCurrentSemester()
                .getLearningGroupOf(student);
        lg.removeMember(student);
        lg.save();
        return redirect(
                controllers.routes.StudentPageController.learningGroupPage(""));

    }

    /**
     * Diese Methode fügt den Studenten zu einer Lerngruppe hinzu, falls eine
     * Lerngruppe mit dem Namen und dem zugehörigen Passwort existiert und die
     * Lerngruppe noch nicht größergleich der maximalen Lerngruppenqröße ist .
     * Anschließend wird der Student auf die Lerngruppen-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result joinLearningGroup() {
        UserManagement user = new UserManagement();
        Student student = (Student) user.getUserProfile(ctx());
        DynamicForm form = formFactory.form().bindFromRequest();
        String name = form.get("learningGroupname");
        String pw = form.get("learningGroupPassword");
        LearningGroup lgOld = GeneralData.getInstance().getCurrentSemester()
                .getLearningGroupOf(student);
        LearningGroup lgNew = LearningGroup.getLearningGroup(name,
                GeneralData.getInstance().getCurrentSemester());
        if (lgNew.getMembers().size() >= GeneralData.getInstance()
                .getCurrentSemester().getMaxGroupSize()) {
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage(ctx().messages().at(
                            "student .learningGroup.error.learningGroupFull")));
        } // wenn die lerngruppe bereits voll ist wird ein fehler zurückgegeben

        if (lgNew.getPassword().equals(pw)) {
            lgOld.delete(); // die private lerngruppe wird gelöscht
            lgNew.addMember(student);
            return redirect(controllers.routes.StudentPageController
                    .learningGroupPage(""));
        }

        return redirect(controllers.routes.StudentPageController
                .learningGroupPage(ctx().messages()
                        .at("student .learningGroup.error.wrongPW")));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Student seine
     * Studentendaten wie E-Mail-Adresse und Passwort ändern kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result accountPage(String error) {
        UserManagement user = new UserManagement();
        Student student = (Student) user.getUserProfile(ctx());
        play.twirl.api.Html content = views.html.studentAccount.render(student,
                error);
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
        UserManagement user = new UserManagement();
        Student student = (Student) user.getUserProfile(ctx());
        DynamicForm form = formFactory.form().bindFromRequest();

        if (form.get("passwordChange") != null) {
            String pw = form.get("newPassword");
            String pwrepeat = form.get("newPasswordRepeat");
            if (!pw.equals(pwrepeat)) {
                // TODO error message
                return redirect(controllers.routes.StudentPageController
                        .accountPage("error"));
            }
            String pwEnc = new BlowfishPasswordEncoder().encode(pw);
            student.setPassword(pwEnc);
        }
        if (form.get("emailChange") != null) {
            String email = form.get("newEmail");
            student.setEmailAddress(email);
            // TODO hier verifikation
        }
        student.save();
        return redirect(
                controllers.routes.StudentPageController.accountPage(""));
    }

    /**
     * Diese Methode verschickt einen neuen Verifikations-Code an die aktuelle
     * E-Mail-Adresse.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result sendNewVerificationLink() {
        return redirect(
                controllers.routes.StudentPageController.accountPage(""));
    }
}
