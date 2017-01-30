// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.util.Calendar;

import javax.inject.Inject;

import data.ElipseModel;
import data.SPO;
import data.Semester;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

/************************************************************/
/**
 * Dieser Controller ist für das Bearbeiten der Http-Requests zuständig, welche
 * beim Ändern der Einstellungen abgeschickt werden.
 */
public class AdminPropertiesController extends Controller {

    @Inject
    FormFactory formFactory;

    /**
     * Diese Methode lässt den Administrator ein neues Semester erstellen und
     * anschließend konfigurieren. Nach dem Erstellen wird der Administrator
     * deshalb auf die Einstellungsseite für das Semester weitergeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addSemester() {
        Semester semester = new Semester("newSemester", true, Calendar.getInstance().get(Calendar.YEAR));
        semester.save();
        return redirect(controllers.routes.AdminPageController.propertiesPage(""));
    }

    /**
     * Diese Methode lässt den Administrator ein Semester löschen, wenn mit
     * diesem keine Studentendaten verbunden sind. Der Administrator wird
     * daraufhin zur Einstellungsseite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result removeSemester() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String semesterIdString = form.get("id");
        int semesterId;
        try {
            semesterId = Integer.parseInt(semesterIdString);
        } catch (NumberFormatException e) {
            return redirect(controllers.routes.AdminPageController
                    .propertiesPage(ctx().messages().at("admin.allocation.error.generalError")));
        }
        Semester semster = ElipseModel.getById(Semester.class, semesterId);
        semster.delete();
        return redirect(controllers.routes.AdminPageController.propertiesPage(""));
    }

    /**
     * Diese Methode fügt eine neue leere SPO, mit einem vom Administrator
     * bestimmten Namen, hinzu. Der Administrator wird daraufhin auf die
     * Einstellungsseite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addSPO() {
        SPO spo = new SPO("newSPO");
        spo.save();
        return redirect(controllers.routes.AdminPageController.propertiesPage(""));
    }

    /**
     * Diese Methode löscht eine bereits vorhandene SPO. Die SPO kann nur
     * gelöscht werden, wenn kein Student diese SPO verwendet. Der Administrator
     * wird daraufhin auf die Einstellungsseite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result removeSPO() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String spoIdString = form.get("id");
        int spoId;
        try {
            spoId = Integer.parseInt(spoIdString);
        } catch (NumberFormatException e) {
            return redirect(controllers.routes.AdminPageController
                    .propertiesPage(ctx().messages().at("admin.allocation.error.generalError")));
        }
        ElipseModel.getById(SPO.class, spoId).delete();
        return redirect(controllers.routes.AdminPageController.propertiesPage(""));
    }

    /**
     * Diese Methode übernimmt die Änderungen, welche der Administrator im
     * Semester-ändern-Formular festgelegt hat. Dazu gehören die Deadlines und
     * die allgemeinen Informationen.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result editSemester() {
        // TODO
        return null;
    }

    /**
     * Diese Methode fügt eine neue Teilleistung zu einer bereits vorhandenen
     * SPO hinzu. Der Administrator kann die Teilleistung als notwendig oder als
     * nicht notwendig deklarieren und deren Namen ändern. Der Administrator
     * wird daraufhin zur Einstellungsseite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addAchievement() {
        // TODO
        return null;
    }

    /**
     * Diese Methode übernimmt änderungen an der SPO. Der Administrator wird
     * daraufhin zur Einstellungsseite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result changeSPO() {
        // TODO
        return null;
    }
}
