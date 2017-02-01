// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import com.google.inject.Inject;

import data.Adviser;
import data.ElipseModel;
import data.GeneralData;
import data.Project;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import security.UserManagement;

/************************************************************/
/**
 * Dieser Controller ist für das Bearbeiten der Http-Requests zuständig, welche
 * im Betreuerbereich aufkommen.
 */
public class AdviserPageController extends Controller {

    @Inject
    FormFactory formFactory;

    /**
     * Diese Methode gibt die Seite zurück, auf der der Betreuer Projekte sieht,
     * Projekte hinzufügen, editieren oder entfernen kann. Editieren und
     * Entfernen eines Projektes ist beschränkt auf Betreuer, welche dem Projekt
     * beigetreten sind.
     * 
     * @param id
     *            Die ID des Projekts
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result projectsPage(int id) {
        UserManagement user = new UserManagement();
        Adviser adviser = (Adviser) user.getUserProfile(ctx());
        boolean isAdviser = adviser.getProjects().contains(ElipseModel.getById(Project.class, id));
        play.twirl.api.Html content = views.html.projectEdit.render(ElipseModel.getById(Project.class, id), isAdviser);
        return ok(views.html.adviser.render(content));
    }

    /**
     * Diese Methode fügt ein neues Projekt in das System ein und leitet den
     * Betreuer zurück auf die Seite zum Editieren des Projektes.
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result addProject() {
        UserManagement user = new UserManagement();
        Adviser adviser = (Adviser) user.getUserProfile(ctx());
        Project project = new Project("new Project" + adviser.getFirstName() + adviser.getLastName(), adviser);
        project.save();
        return redirect(controllers.routes.AdviserPageController.projectsPage(project.getId()));
    }

    /**
     * Diese Methode löscht ein Projekt und alle dazugehörenden Daten aus dem
     * System und leitet den Betreuer auf die Seite zum Editieren und Hinzufügen
     * von Projekten. Nur Betreuer welche dem Projekt beigetreten sind können
     * dieses editieren.
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result removeProject() {
        UserManagement user = new UserManagement();
        Adviser adviser = (Adviser) user.getUserProfile(ctx());
        DynamicForm form = formFactory.form().bindFromRequest();
        int id = Integer.parseInt(form.get("id"));
        Project project = ElipseModel.getById(Project.class, id);
        if (adviser.getProjects().contains(project)) {
            project.delete();

        }
        return redirect(controllers.routes.AdviserPageController
                .projectsPage(GeneralData.getInstance().getCurrentSemester().getProjects().get(0).getId()));
    }

    /**
     * Diese Methode editiert ein bereits vorhandenes Projekt. Die zu
     * editierenden Daten übermittelt der Betreuer über ein Formular, welches er
     * zum Editieren abschickt. Nur Betreuer welche dem Projekt beigetreten sind
     * können dieses editieren. Anschließend wird der Betreuer auf die Seite zum
     * Hinzufügen und Editieren von Projekten weitergeleitet.
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result editProject() {
        UserManagement user = new UserManagement();
        Adviser adviser = (Adviser) user.getUserProfile(ctx());

        DynamicForm form = formFactory.form().bindFromRequest();
        String projName = form.get("name");
        String url = form.get("url");
        String institute = form.get("institute");
        String description = form.get("description");
        String numberOfTeamsString = form.get("teamCount");
        String minSizeString = form.get("minSize");
        String maxSizeString = form.get("maxSize");
        String idString = form.get("id"); // TODO im view die ID hinzufügen
        int id = Integer.parseInt(idString);
        int numberOfTeams;
        int minSize;
        int maxSize;
        Project project = ElipseModel.getById(Project.class, id);
        boolean isAdviser = adviser.getProjects().contains(project);
        if (!isAdviser) {
            return redirect(controllers.routes.AdviserPageController.projectsPage(id));
        }
        try {
            numberOfTeams = Integer.parseInt(numberOfTeamsString);
            minSize = Integer.parseInt(minSizeString);
            maxSize = Integer.parseInt(maxSizeString);
        } catch (NumberFormatException e) {
            return redirect(controllers.routes.AdviserPageController.projectsPage(id));
        }

        project.setInstitute(institute);
        project.setMaxTeamSize(maxSize);
        project.setMinTeamSize(minSize);
        project.setName(projName);
        project.setNumberOfTeams(numberOfTeams);
        project.setProjectInfo(description);
        project.setProjectURL(url);
        project.save();

        return redirect(controllers.routes.AdviserPageController.projectsPage(project.getId()));
    }

    /**
     * Diese Methode fügt einen Betreuer zu einem bereits existierenden Projekt
     * hinzu, sodass dieser auch die Möglichkeit besitzt das Projekt zu
     * editieren oder zu löschen und nach der Veröffentlichung einer Einteilung
     * auch Teams und deren Mitglieder sieht. Nach dem Beitreten wird der
     * Betreuer auf die Seite zum Hinzufügen und Editieren von Projekten
     * weitergeleitet.
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result joinProject() {
        UserManagement user = new UserManagement();
        Adviser adviser = (Adviser) user.getUserProfile(ctx());
        DynamicForm form = formFactory.form().bindFromRequest();
        int id = Integer.parseInt(form.get("id"));
        Project project = ElipseModel.getById(Project.class, id);
        if (!adviser.getProjects().contains(project)) {
            project.addAdviser(adviser);
            project.save();
        }
        return redirect(controllers.routes.AdviserPageController.projectsPage(project.getId()));
    }

    /**
     * Diese Methode entfernt einen Betreuer aus einem existierenden Projekt,
     * sodass dieser nicht mehr das Projekt editieren oder löschen kann.
     * Anschließend wird der Betreuer auf die Seite zum Hinzufügen und Editieren
     * von Projekten weitergeleitet.
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result leaveProject() {
        // TODO wenn es nicht in ordnung ist das ein projekt ohne beteruer
        // existiert hier einbauen
        UserManagement user = new UserManagement();
        Adviser adviser = (Adviser) user.getUserProfile(ctx());
        DynamicForm form = formFactory.form().bindFromRequest();
        int id = Integer.parseInt(form.get("id"));
        Project project = ElipseModel.getById(Project.class, id);
        if (adviser.getProjects().contains(project)) {
            project.removeAdviser(adviser);
            project.save();
        }
        return redirect(controllers.routes.AdviserPageController.projectsPage(project.getId()));
    }

    /**
     * Diese Methode speichert alle von einem Betreuer in ein Formular
     * eingegebenen Noten, sodass diese vom Administrator in das CMS importiert
     * werden können. Anschließend wird der Betreuer auf die Projektseite des
     * jeweiligen Projektes weitergeleitet.
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result saveStudentsGrades() {
        // TODO
        return null;
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der der Betreuer seine
     * Benutzerdaten wie E-Mail-Adresse und Passwort ändern kann.
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result accountPage(String error) {
        play.twirl.api.Html content = views.html.adviserAccount.render(error);
        // TODO muss hier noch ein param mitgegeben werden?

        return ok(views.html.adviser.render(content));
    }

    /**
     * Diese Methode editiert die Daten des Betreuers, welche er auf der
     * Account-Seite geändert hat.
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result editAccount() {
        // TODO
        return null;
    }
}
