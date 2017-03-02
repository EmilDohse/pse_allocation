// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.util.ArrayList;
import java.util.List;

import allocation.AllocationQueue;
import data.Adviser;
import data.Allocation;
import data.ElipseModel;
import data.GeneralData;
import data.Project;
import data.SMTPOptions;
import data.SPO;
import data.Semester;
import deadline.StateStorage;
import play.mvc.Controller;
import play.mvc.Result;
import views.AdminMenu;
import views.Menu;

/************************************************************/
/**
 * Dieser Controller ist zuständig für das Bearbeiten der Http-Requests, welche
 * durch das Klicken eines Links und nicht eines Buttons versendet werden.
 */
public class AdminPageController extends Controller {

    private static final String ERROR       = "error";
    private static final String NO_SEMESTER = "admin.error.noSemester";

    /**
     * Diese Methode gibt die Seite zurück, auf der der Administrator Projekte
     * sieht, neue hinzufügen, sowie existierende löschen kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result projectPage() {
        play.twirl.api.Html content;
        if (GeneralData.loadInstance().getCurrentSemester() == null) {
            String error = flash(ERROR) + "\n";
            flash(ERROR, error + ctx().messages().at(ctx().messages().at(NO_SEMESTER)));
            content = views.html.adminProjects.render(new ArrayList<>());
        } else {
            content = views.html.adminProjects.render(GeneralData.loadInstance().getCurrentSemester().getProjects());
        }
        Menu menu = new AdminMenu(ctx(), ctx().request().path());
        return ok(views.html.admin.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Administrator alle
     * Projektbetreuer sehen, neue hinzufügen oder bereits existierende
     * entfernen kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result adviserPage() {
        play.twirl.api.Html content;

        content = views.html.adminAdvisers.render(Adviser.getAdvisers());

        Menu menu = new AdminMenu(ctx(), ctx().request().path());
        return ok(views.html.admin.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Administrator
     * Einteilungen berechnen und vorher Parameter einstellen kann. Außerdem
     * sieht er noch zu berechnende Konfigurationen und kann diese aus der
     * Berechnungsliste entfernen.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result allocationPage() {
        ArrayList<allocation.Criterion> criteria = new ArrayList<>(
                AllocationQueue.getInstance().getAllocator().getAllCriteria());

        play.twirl.api.Html content = views.html.adminAllocation.render(AllocationQueue.getInstance(), criteria);

        Menu menu = new AdminMenu(ctx(), ctx().request().path());
        return ok(views.html.admin.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Administrator die
     * Ergebnisse der Berechnungen sehen, vergleichen und editieren kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result resultsPage() {
        ArrayList<qualityCriteria.QualityCriterion> criteria = new ArrayList<>(
                qualityCriteria.QualityCriteriaLoader.getAllQualityCriteria());
        play.twirl.api.Html content;
        if (GeneralData.loadInstance().getCurrentSemester() == null) {
            String error = flash(ERROR) + "\n";
            flash(ERROR, error + ctx().messages().at(ctx().messages().at(NO_SEMESTER)));
            content = views.html.adminResults.render(new ArrayList<>(), criteria);
        } else {
            List<Allocation> allocations = GeneralData.loadInstance().getCurrentSemester().getAllocations();
            if (!allocations.isEmpty()) {
                content = views.html.adminResults.render(allocations, criteria);
            } else {
                content = views.html.noAllocationYet.render();
            }
        }
        Menu menu = new AdminMenu(ctx(), ctx().request().path());
        return ok(views.html.admin.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Administrator
     * Einteilungen, Studentendaten, SPOs, Projekte und CMS-Daten ex- und
     * importieren kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result exportImportPage() {
        play.twirl.api.Html content = views.html.adminExportImport.render(SPO.getSPOs(),
                GeneralData.loadInstance().getCurrentSemester().getAllocations());
        Menu menu = new AdminMenu(ctx(), ctx().request().path());
        return ok(views.html.admin.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Administrator Studenten
     * manuell hinzufügen oder löschen kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result studentEditPage() {
        play.twirl.api.Html content;
        if (GeneralData.loadInstance().getCurrentSemester() == null) {
            String error = flash(ERROR) + "\n";
            flash(ERROR, error + ctx().messages().at(ctx().messages().at(NO_SEMESTER)));
            content = views.html.adminStudentEdit.render(new ArrayList<>());
        } else {
            content = views.html.adminStudentEdit.render(GeneralData.loadInstance().getCurrentSemester().getSpos());
        }
        Menu menu = new AdminMenu(ctx(), ctx().request().path());
        return ok(views.html.admin.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Administrator die
     * Semester-Einstellungen vornehmen kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result propertiesPage() {
        play.twirl.api.Html content = views.html.adminProperties.render(GeneralData.loadInstance().getCurrentSemester(),
                Semester.getSemesters(), SPO.getSPOs(), SMTPOptions.getInstance());
        Menu menu = new AdminMenu(ctx(), ctx().request().path());
        return ok(views.html.admin.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Administrator ein
     * Projekt editieren kann.
     * 
     * @param id
     *            die Id des Projekts
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result projectEditPage(int id) {
        // kein Element ausgewählt
        if (id == -1) {
            if (GeneralData.loadInstance().getCurrentSemester().getProjects().isEmpty()) {
                return redirect(controllers.routes.AdminPageController.projectPage());
            } else {
                id = GeneralData.loadInstance().getCurrentSemester().getProjects().get(0).getId();
            }

        }
        Project project = ElipseModel.getById(Project.class, id);
        // wenn die ID über die URL verändert wurde
        if (project == null) {
            flash(ERROR, ctx().messages().at(Project.NOT_EXISTENT));
            return redirect(controllers.routes.AdminPageController.projectPage());
        }
        play.twirl.api.Html content = views.html.projectEdit.render(project, false, Adviser.getAdvisers());
        Menu menu = new AdminMenu(ctx(), ctx().request().path());
        return ok(views.html.admin.render(menu, content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Administrator sein
     * Passwort ändern kann.
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result accountPage() {
        play.twirl.api.Html content = views.html.adminAccount.render();
        Menu menu = new AdminMenu(ctx(), ctx().request().path());
        return ok(views.html.admin.render(menu, content));
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
        case BEFORE_REGISTRATION_PHASE:
            flash("info", ctx().messages().at("admin.beforeRegistration.actionNotAllowed"));
            break;
        case REGISTRATION_PHASE:
            flash("info", ctx().messages().at("admin.registration.actionNotAllowed"));
            break;
        case AFTER_REGISTRATION_PHASE:
            flash("info", ctx().messages().at("admin.afterRegistration.actionNotAllowed"));
            break;
        default:
            flash("info", ctx().messages().at("state.actionNotAllowed"));
            break;
        }

        return redirect(url);
    }
}
