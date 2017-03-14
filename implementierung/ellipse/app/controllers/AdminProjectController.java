// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.util.ArrayList;

import com.google.inject.Inject;

import data.Adviser;
import data.ElipseModel;
import data.GeneralData;
import data.Project;
import data.Semester;
import exception.ValidationException;
import form.Forms;
import form.IntValidator;
import form.StringValidator;
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

    private static final String INTERNAL_ERROR = "error.internalError";
    private static final String ERROR          = "error";

    @Inject
    FormFactory                 formFactory;

    /**
     * Diese Methode fügt ein neues Projekt in das System ein und leitet den
     * Administrator zurück auf die Seite zum Editieren des Projektes.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addProject() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        String projName = form.get("name");
        Project project = new Project(projName, "", "", "");
        project.save();
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addProject(project);
        });
        int projID = project.getId();
        return redirect(
                controllers.routes.AdminPageController.projectEditPage(projID));

    }

    /**
     * Diese Methode löscht ein Projekt und alle dazugehörenden Daten aus dem
     * System und leitet den Administrator weiter auf die Seite zum Editieren
     * und Hinzufügen von Projekten.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result removeProject() {
        synchronized (Project.class) {
            DynamicForm form = formFactory.form().bindFromRequest();
            if (form.data().isEmpty()) {
                return badRequest(ctx().messages().at(INTERNAL_ERROR));
            }
            Project project = ElipseModel.getById(Project.class,
                    Integer.parseInt(form.get("id")));
            project.doTransaction(() -> {
                project.setAdvisers(new ArrayList<>());
            });
            project.delete();
            return redirect(
                    controllers.routes.AdminPageController.projectPage());
        }
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
        synchronized (Adviser.class) {
            synchronized (Project.class) {
                // die projektdaten werden aus dem formular ausgelesen
                DynamicForm form = formFactory.form().bindFromRequest();
                if (form.data().isEmpty()) {
                    return badRequest(ctx().messages().at(INTERNAL_ERROR));
                }
                StringValidator notEmptyValidator = Forms
                        .getNonEmptyStringValidator();
                IntValidator minValidator = new IntValidator(0);

                String projName;
                String url;
                String institute;
                String description;
                int numberOfTeams;
                int minSize;
                int maxSize;

                int id;
                try {
                    id = minValidator.validate(form.get("id"));
                } catch (ValidationException e) {
                    flash(ERROR, ctx().messages().at(e.getMessage()));
                    return redirect(controllers.routes.AdminPageController
                            .projectEditPage(-1));
                }
                Project project = ElipseModel.getById(Project.class, id);
                // fehlerausgabe wenn projekt gelöscht wurde während der
                // bearbeitung
                if (project == null) {
                    flash(ERROR,
                            ctx().messages().at(Project.CONCURRENCY_ERROR));
                    return redirect(controllers.routes.AdminPageController
                            .projectPage());
                }
                // Prüfe alle ausgelesenen Werte auf nichtleere Strings und
                // Mindestwerte
                try {
                    projName = notEmptyValidator.validate(form.get("name"));
                    url = notEmptyValidator.validate(form.get("url"));
                    institute = notEmptyValidator
                            .validate(form.get("institute"));
                    description = notEmptyValidator
                            .validate(form.get("description"));
                    numberOfTeams = minValidator
                            .validate(form.get("teamCount"));
                    minSize = minValidator.validate(form.get("minSize"));
                    maxSize = minValidator.validate(form.get("maxSize"));
                } catch (ValidationException e) {
                    flash(ERROR, ctx().messages().at("error.wrongInput"));
                    return redirect(controllers.routes.AdminPageController
                            .projectEditPage(project.getId()));
                }
                // Prüfe, dass max >= min und dass Min und Max immer
                // gleichzeitig 0 oder
                // nichtnull sind
                if ((minSize == 0 ^ maxSize == 0) || (maxSize < minSize)) {
                    flash(ERROR, ctx().messages().at("error.wrongTeamSize"));
                    return redirect(controllers.routes.AdminPageController
                            .projectEditPage(project.getId()));
                }

                // Hole die Betreuer des Projektes aus der Datenbank
                ArrayList<Adviser> advisers = new ArrayList<>();
                String[] adviserIds = MultiselectList.getValueArray(form,
                        "adviser-multiselect");
                for (String adviserIdString : adviserIds) {
                    int adviserId;
                    try {
                        adviserId = Integer.parseInt(adviserIdString);
                    } catch (NumberFormatException e) {
                        flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
                        return redirect(controllers.routes.AdminPageController
                                .projectEditPage(project.getId()));
                    }
                    // hier wird überprüft ob der adviser während der
                    // bearbeitung gelöscht wurde
                    Adviser adviser = ElipseModel.getById(Adviser.class,
                            adviserId);
                    if (adviser == null) {
                        flash(ERROR,
                                ctx().messages().at(Adviser.CONCURRENCY_ERROR));
                        return redirect(controllers.routes.AdminPageController
                                .projectEditPage(project.getId()));
                    }
                    advisers.add(adviser);
                }

                // Füge alle ausgelesenen Daten dem Projekt hinzu
                project.doTransaction(() -> {
                    project.setAdvisers(advisers);
                    project.setInstitute(institute);
                    project.setMaxTeamSize(maxSize);
                    project.setMinTeamSize(minSize);
                    project.setName(projName);
                    project.setNumberOfTeams(numberOfTeams);
                    project.setProjectInfo(description);
                    project.setProjectURL(url);
                });

                return redirect(
                        controllers.routes.AdminPageController.projectPage());
            }
        }
    }
}
