// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import javax.inject.Inject;

import data.GeneralData;
import data.SPO;
import data.Student;
import notificationSystem.Notifier;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

/************************************************************/
/**
 * Dieser Controller ist zuständig für alle Http-Requests, die in dem Bereich
 * aufkommen, welche ohne Anmeldung zugänglich sind. Dazu zählt neben der
 * Index-Seite auch die Passwort vergessen-Seite und die
 * E-Mail-Verifikations-Seite.
 */
public class IndexPageController extends Controller {

    /**
     * 
     */
    private Notifier notifier;

    @Inject
    FormFactory      formFactory;

    /**
     * Diese Methode gibt die Startseite zurück. Auf dieser Seite können sich
     * Administrator, Betreuer und Studenten anmelden oder aktuelle
     * Informationen einsehen.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result indexPage(String error) {
        // TODO
        play.twirl.api.Html content = views.html.indexInformation.render("Hier könnte ihre Werbung stehen!", error);
        return ok(views.html.index.render(content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der sich ein Student
     * registrieren kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result registerPage(String error) {
        // TODO
        // Test Code
        java.util.ArrayList<data.SPO> spos = new java.util.ArrayList<>();
        data.SPO spo1 = new data.SPO();
        spo1.setName("SPO 2008");
        spo1.setAdditionalAchievements(new java.util.ArrayList<>());
        spo1.setNecessaryAchievements(new java.util.ArrayList<>());
        data.Achievement a1 = new data.Achievement("LA 1");
        a1.setId(1);
        spo1.addAdditionalAchievement(a1);
        data.Achievement a2 = new data.Achievement("LA 2");
        a2.setId(2);
        spo1.addAdditionalAchievement(a2);
        data.Achievement a3 = new data.Achievement("HM 1");
        a3.setId(3);
        spo1.addNecessaryAchievement(a3);
        data.Achievement a4 = new data.Achievement("HM 3");
        a4.setId(4);
        spo1.addNecessaryAchievement(a4);
        data.SPO spo2 = new data.SPO();
        spo2.setName("SPO 2015");
        spo2.setAdditionalAchievements(new java.util.ArrayList<>());
        spo2.setNecessaryAchievements(new java.util.ArrayList<>());
        data.Achievement a5 = new data.Achievement("Algo 3");
        a5.setId(5);
        spo2.addAdditionalAchievement(a5);
        data.Achievement a6 = new data.Achievement("Algo 1");
        a6.setId(6);
        spo2.addAdditionalAchievement(a6);
        data.Achievement a7 = new data.Achievement("SWT1");
        a7.setId(7);
        spo2.addNecessaryAchievement(a7);
        data.Achievement a8 = new data.Achievement("SWT 3");
        a8.setId(8);
        spo2.addNecessaryAchievement(a8);
        spos.add(spo1);
        spos.add(spo2);
        play.twirl.api.Html content = views.html.indexRegistration.render(spos, error);
        // End Test Code
        // play.twirl.api.Html content = views.html.indexRegistration
        // .render(GeneralData.getCurrentSemester().getSpos(), error);
        return ok(views.html.index.render(content));
    }

    /**
     * Diese Methode initiiert die Login-Prozedur und leitet den Anzumeldenden
     * je nach Autorisierung auf die passende Seite weiter.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result login() {
        // TODO
        return null;
    }

    /**
     * Diese Methode registriert einen Studenten und fügt diesen in die
     * Datenbank ein, sofern alle notwendigen Teillestungen als bestanden
     * angegeben wurden.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result register() {

        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().size() == 0) {
            return badRequest("Expceting some data");
        } else {

            String firstName = form.get("firstName");
            String lastName = form.get("lastName");
            String email = form.get("email");
            String password = form.get("pw");
            String pwRepeat = form.get("rpw");
            String matNrString = "";
            int matNr = -1;
            try {
                matNrString = form.get("matrnr");
                matNr = Integer.parseInt(matNrString);
            } catch (NumberFormatException e) {
                return redirect(controllers.routes.IndexPageController
                        .registerPage(ctx().messages().at("index.registration.error.genError")));
            }

            boolean trueData = false;

            if (form.get("trueData") != null) { // TODO überprüfen ob man das so
                                                // überprüft
                trueData = true;
            }

            if (password.equals(pwRepeat) && trueData) {
                if (Student.getStudent(matNr) == null) {
                    Student student = new Student(matNrString, password, email, firstName, lastName, matNr,
                            new SPO() /* enter spo eleements here */,
                            null/* completed achevevents */,
                            null /* oral achevements here */,
                            0/* semester here */);
                    // TODO get student data from view
                    GeneralData.getCurrentSemester().addStudent(student);
                    return redirect(controllers.routes.IndexPageController.indexPage(""));
                    // TODO falls nötig noch emial verification einleiten
                } else {
                    return redirect(controllers.routes.IndexPageController
                            .registerPage(ctx().messages().at("index.registration.error.matNrExists")));
                }
            }

        } // TODO braucht man hmehr als nur eine gererelle fehlermeldung?
        return redirect(controllers.routes.IndexPageController
                .registerPage(ctx().messages().at("index.registration.error.genError")));
    }

    /**
     * Diese Methode gibt die Seite zurück, die ein Passwort-Rücksetz-Formular
     * für Studenten und Betreuer anzeigt.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result passwordResetPage(String error) {
        // TODO
        return null;
    }

    /**
     * Diese Methode schickt eine E-Mail anhand der Daten aus dem
     * Passwort-Rücksetz-Formular an den Studenten oder den Betreuer, welche ein
     * neues Passwort enthält.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result passwordReset() {
        // TODO
        return null;
    }

    /**
     * Diese Methode gibt die Seite zurück, welche einen Studenten verifiziert.
     * Dies funktioniert, indem der Student eine Mail mit einen Link auf diese
     * Seite erhält, welche noch einen Code als Parameter übergibt. Anhand
     * dieses Parameters wird der Student verifiziert.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result verificationPage(String code) {
        // TODO
        return null;
    }
}
