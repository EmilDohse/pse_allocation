// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import com.google.inject.Inject;

import data.ElipseModel;
import data.GeneralData;
import data.Project;
import notificationSystem.Notifier;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

/************************************************************/
/**
 * Dieser Controller ist für das Bearbeiten der Http-Requests zuständig, welche
 * beim Hinzufügen, Ändern und Löschen eines Projektes im Administratorbereich
 * abgeschickt werden.
 */
public class AdminProjectController extends Controller {

    private Notifier notifier;

    @Inject
    FormFactory      formFactory;

    /**
     * Diese Methode fügt ein neues Projekt in das System ein und leitet den
     * Administrator zurück auf die Seite zum Editieren des Projektes.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addProject() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String projName = form.get("name");
        Project project = new Project(projName, null);
        // TODO muss man hier nicht
        // igendwie den adcviser
        // weglassen können?
        GeneralData.getCurrentSemester().addProject(project);
        return redirect(controllers.routes.AdminPageController
                .projectEditPage(project.getId()));
    }

    /**
     * Diese Methode löscht ein Projekt und alle dazugehörenden Daten aus dem
     * System und leitet den Administrator weiter auf die Seite zum Editieren
     * und Hinzufügen von Projekten.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result removeProject() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String projName = form.get("name");
        Project project = Project.getProject(projName,
                GeneralData.getCurrentSemester());
        // TODO hier eine warnmeldung ausgeben ob das projekt wirklich gelöscht
        // werden soll
        GeneralData.getCurrentSemester().removeProject(project);
        return redirect(controllers.routes.AdminPageController.projectEditPage(
                GeneralData.getCurrentSemester().getProjects().get(0).getId()));
    }

    /**
     * Diese Methode editiert ein bereits vorhandenes Projekt. Die zu
     * editierenden Daten übermittelt der Administrator über ein Formular,
     * welches er zum Editieren abschickt. Anschließend wird der Administrator
     * auf die Seite zum Hinzufügen und Editieren von Projekten weitergeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result editProject() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String projName = form.get("name");
        String url = form.get("url");
        String institute = form.get("institute");
        String description = form.get("description");
        String numberOfTeamsString = form.get("teamCount");
        String minSizeString = form.get("minSize");
        String maxSizeString = form.get("maxSize");
        String idString = form.get("id");
        int numberOfTeams;
        int minSize;
        int maxSize;
        int id = Integer.parseInt(idString);
        Project project = ElipseModel.getById(Project.class, id);
        try {
            numberOfTeams = Integer.parseInt(numberOfTeamsString);
            minSize = Integer.parseInt(minSizeString);
            maxSize = Integer.parseInt(maxSizeString);
        } catch (NumberFormatException e) {
            return redirect(controllers.routes.AdminPageController
                    .projectEditPage(project.getId()));
        }

        project.setInstitute(institute);
        project.setMaxTeamSize(maxSize);
        project.setMinTeamSize(minSize);
        project.setName(projName);
        project.setNumberOfTeams(numberOfTeams);
        project.setProjectInfo(description);
        project.setProjectURL(url);
        project.save();
        return redirect(controllers.routes.AdminPageController
                .projectEditPage(project.getId()));
    }
}
