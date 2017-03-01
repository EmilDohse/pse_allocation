// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.apache.commons.mail.EmailException;

import com.google.inject.Inject;

import data.Allocation;
import data.ElipseModel;
import data.GeneralData;
import data.Semester;
import data.Student;
import data.Team;
import exception.AllocationEditUndoException;
import exception.ValidationException;
import form.IntValidator;
import notificationSystem.Notifier;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

/************************************************************/
/**
 * Dieser Controller ist für das Bearbeiten der Http-Requests zuständig, welche
 * beim Editieren einer Einteilung abgeschickt werden.
 */
public class AdminEditAllocationController extends Controller {

    private static final String                 ERROR          = "error";

    @Inject
    FormFactory                                 formFactory;

    @Inject
    Notifier                                    notifier;

    /**
     * Commando Stack. Static, weil Play nicht garantiert, dass es nur einen
     * Controller gibt.
     */
    private static Deque<EditAllocationCommand> undoStack      = new ArrayDeque<>();

    private static final String                 ALLOCATION_ID  = "allocationID";
    private static final String                 INTERNAL_ERROR = "error.internalError";

    /**
     * Diese Methode editiert die Einteilung.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result editAllocation() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        String[] selectedIdsString = MultiselectList.getValueArray(form,
                "selected-students");
        ArrayList<Integer> selectedIds = new ArrayList<>();

        // Ziehe die ausgewählten Studenten-IDs aus dem Formular
        for (String s : selectedIdsString) {
            try {
                selectedIds.add(new IntValidator(0).validate(s));
            } catch (ValidationException e) {
                e.printStackTrace();
                flash(ERROR, ctx().messages().at(e.getMessage()));
                return redirect(controllers.routes.AdminPageController
                        .resultsPage());
            }
        }

        // Prüfe, welche Aktion ausgewählt werden soll
        if (form.get("move") != null) {
            return moveStudents(form, selectedIds);
        } else if (form.get("exchange") != null) {
            return swapStudents(form, selectedIds);
        } else {
            flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }
    }

    /**
     * Diese Methode tauscht zwei Studenten, welche der Administrator vorher in
     * einem Formular ausgewählt hat. Ein Tausch innerhalb eines Teams wird
     * nicht unterbunden, hat jedoch keine Auswirkung. Anschließend wird der
     * Administrator auf die Seite zur Einteilungs-Bearbeitung zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    private Result swapStudents(DynamicForm form, List<Integer> ids) {

        // Ziehe die bearbeitete Einteilung aus dem Formular und lade die Seite
        // bei einem Fehler neu
        String allocationIdString = form.get(ALLOCATION_ID);
        int allocationId;
        try {
            allocationId = new IntValidator(0).validate(allocationIdString);
        } catch (ValidationException e) {
            e.printStackTrace();
            flash(ERROR, ctx().messages().at(e.getMessage()));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }
        Allocation allocation = ElipseModel.getById(Allocation.class,
                allocationId);

        if (GeneralData.loadInstance().getCurrentSemester()
                .getFinalAllocation().equals(allocation)) {
            flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }

        // Prüfe, ob genau zwei Studenten ausgewählt wurden
        if (ids.size() != 2) {
            flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }

        // Tausche die Teams der Studenten und lade die Seite neu
        Student firstStudent = ElipseModel.getById(Student.class, ids.get(0));
        Student secondStudent = ElipseModel.getById(Student.class, ids.get(1));
        SwapStudentCommand command = new SwapStudentCommand(allocation,
                firstStudent, secondStudent);
        command.execute();
        undoStack.push(command);

        return redirect(controllers.routes.AdminPageController.resultsPage());
    }

    /**
     * Diese Methode verschiebt einen oder mehrere ausgewählte Studenten in ein
     * anderes Team. Das Verschieben in das gleiche Team wird nicht unterbunden,
     * hat jedoch keine Auswirkung. Anschließend wird der Administrator auf die
     * Seite zur Einteilungs-Bearbeitung zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    private Result moveStudents(DynamicForm form, List<Integer> ids) {

        // Ziehe das Team, in das die Studenten geschoben werden sollen, sowie
        // die zu bearbeitende Einteilung aus dem Formular
        String teamIdString = form.get("project-selection");
        String allocationIdString = form.get(ALLOCATION_ID);
        int allocationId;
        int teamId;
        try {
            teamId = new IntValidator().validate(teamIdString);
            allocationId = new IntValidator(0).validate(allocationIdString);
        } catch (ValidationException e) {
            e.printStackTrace();
            flash(ERROR, ctx().messages().at(e.getMessage()));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }

        // Hole die benötigten Daten aus der Datenbank
        Team newTeam = ElipseModel.getById(Team.class, teamId);
        Allocation allocation = ElipseModel.getById(Allocation.class,
                allocationId);

        if (GeneralData.loadInstance().getCurrentSemester()
                .getFinalAllocation().equals(allocation)) {
            flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }

        List<Student> students = new ArrayList<>();
        for (int id : ids) {
            Student s = ElipseModel.getById(Student.class, id);
            students.add(s);
        }

        // Prüfe, ob Studenten ausgewählt wurden
        if (students.isEmpty()) {
            flash(ERROR, ctx().messages().at("admin.edit.noStudentSelected"));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }

        // Führe den Command aus
        MoveStudentCommand command = new MoveStudentCommand(allocation,
                students, newTeam);
        command.execute();
        undoStack.push(command);

        return redirect(controllers.routes.AdminPageController.resultsPage());
    }

    /**
     * Diese Methode veröffentlicht eine Einteilung. Dazu gehört, die Einteilung
     * als final zu deklarieren und Betreuer und Studenten per E-Mail über deren
     * Einteilung zu informieren. Der Administrator wird anschließend auf die
     * Einteilungs-Bearbeitungs-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result publishAllocation() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }

        // Prüfe, ob es sich um eine gültige ID handelt
        String allocationIdString = form.get(ALLOCATION_ID);
        int allocationId;
        try {
            allocationId = new IntValidator(0).validate(allocationIdString);
        } catch (ValidationException e) {
            e.printStackTrace();
            flash(ERROR, ctx().messages().at(e.getMessage()));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }
        Allocation allocation = ElipseModel.getById(Allocation.class,
                allocationId);
        Semester semester = GeneralData.loadInstance().getCurrentSemester();

        // Prüfe, ob es schon eine finale Einteilung gibt
        if (semester.getFinalAllocation() != null) {
            flash(ERROR, ctx().messages().at("admin.edit.noFinalAllocation"));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }

        // Setze finales Semester und benachrichtige alle User
        semester.doTransaction(() -> {
            semester.setFinalAllocation(allocation);
        });
        try {
            notifier.notifyAllUsers(allocation);
        } catch (EmailException e) {
            flash(ERROR, ctx().messages().at("email.couldNotSend"));
            e.printStackTrace();
        }
        return redirect(controllers.routes.AdminPageController.resultsPage());
    }

    /**
     * Diese Methode erstellt eine Kopie einer kompletten Einteilung. Diese
     * Funktion ist dafür gedacht, dass der Administrator sehen kann, ob durch
     * seine manuelle Änderungen ein besseres Ergebnis entstand. Der
     * Administrator wird anschließend auf die Seite zur Einteilungs-Bearbeitung
     * zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result duplicateAllocation() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }

        // Prüfe, ob es sich um eine gültige Id handelt
        String allocationIdString = form.get(ALLOCATION_ID);
        int allocationId;
        try {
            allocationId = new IntValidator(0).validate(allocationIdString);
        } catch (ValidationException e) {
            e.printStackTrace();
            flash(ERROR, ctx().messages().at(e.getMessage()));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }
        Allocation allocation = ElipseModel.getById(Allocation.class,
                allocationId);

        // Dupliziere die Einteilung
        Allocation clonedAllocation = new Allocation(allocation);
        clonedAllocation.save();
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addAllocation(clonedAllocation);
        });
        return redirect(controllers.routes.AdminPageController.resultsPage());
    }

    /**
     * Diese Methode löscht eine bereits vorhandene Einteilung. Der
     * Administrator wird anschließend auf die Seite zur Einteilungs-Bearbeitung
     * zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result removeAllocation() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }

        // Prüfe auf gültige ID
        String allocationIdString = form.get(ALLOCATION_ID);
        int allocationId;
        try {
            allocationId = new IntValidator(0).validate(allocationIdString);
        } catch (ValidationException e) {
            e.printStackTrace();
            flash(ERROR, ctx().messages().at(e.getMessage()));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }
        Allocation allocation = ElipseModel.getById(Allocation.class,
                allocationId);

        // Prüfe, ob die Allocation die finale ist
        if (allocation.equals(GeneralData.loadInstance().getCurrentSemester()
                .getFinalAllocation())) {
            flash(ERROR, ctx().messages()
                    .at("admin.edit.removeFinalAllocation"));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }

        // Lösche die Einteilung
        allocation.delete();
        return redirect(controllers.routes.AdminPageController.resultsPage());
    }

    /**
     * Diese Methode macht die letzte Editierung rückgängig. Dies ist jedoch
     * nicht session-übergreifend möglich. Der Administrator wird anschließend
     * auf die Seite zur Einteilungs-Bearbeitung zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result undoAllocationEdit() {
        // Fehlermeldung, falls es nichts rückgängig zu machen gibt
        if (undoStack.isEmpty()) {
            flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
            return redirect(controllers.routes.AdminPageController
                    .resultsPage());
        }

        // Führe den Command aus
        try {
            undoStack.pop().undo();
        } catch (AllocationEditUndoException e) {
            flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
        }

        return redirect(controllers.routes.AdminPageController.resultsPage());
    }
}
