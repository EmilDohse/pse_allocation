// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;

import com.google.inject.Inject;

import allocation.AllocationQueue;
import allocation.Configuration;
import allocation.Criterion;
import data.Administrator;
import data.Adviser;
import data.AllocationParameter;
import data.ElipseModel;
import data.GeneralData;
import data.LearningGroup;
import data.Project;
import data.SPO;
import data.Semester;
import data.Student;
import notificationSystem.Notifier;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import security.BlowfishPasswordEncoder;
import security.UserManagement;

/************************************************************/
/**
 * Dieser Controller ist für das Bearbeiten der Http-Requests zuständig, welche
 * abgeschickt werden, wenn Betreuer, Studenten oder Einteilungen hinzugefügt
 * oder gelöscht werden sollen.
 */
public class GeneralAdminController extends Controller {

    private static final String INTERNAL_ERROR = "error.internalError";
    private static final String GENERAL_ERROR  = "admin.allocation.error.generalError";

    @Inject
    FormFactory                 formFactory;

    @Inject
    Notifier                    notifier;

    /**
     * Diese Methode fügt einen Betreuer mit den Daten aus dem vom Administrator
     * auszufüllenden Formular zum System hinzu. Der Administrator wird
     * anschließend auf die Betreuerübersicht weitergeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addAdviser() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        String firstName = form.get("firstName");
        String lastName = form.get("lastName");
        String email = form.get("email");
        String password = form.get("password");
        String encPassword = new BlowfishPasswordEncoder().encode(password);
        try {
            Adviser adviser = new Adviser(email, encPassword, email, firstName,
                    lastName);
            adviser.save();
            notifier.sendAdviserPassword(adviser, password);
        } catch (EmailException e) {
            e.printStackTrace();
            // TODO
        }
        return redirect(controllers.routes.AdminPageController.adviserPage());

    }

    /**
     * Diese Methode entfernt einen Betreuer und dessen Daten aus dem System.
     * Der Administrator wird anschließend auf die Betreuerübersicht
     * weitergeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result removeAdviser() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        int adviserId = Integer.parseInt(form.get("id"));
        ElipseModel.getById(Adviser.class, adviserId).delete();
        return redirect(controllers.routes.AdminPageController.adviserPage());
    }

    /**
     * Diese Methode fügt eine Einteilungskonfiguration in die
     * Berechnungswarteschlange hinzu. Der Administrator wird anschließend auf
     * die Berechnungsübersichtsseite weitergeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addAllocation() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        String name = form.get("name");
        String preferedSizeString = form.get("preferedTeamSize");
        String minSizeString = form.get("minTeamSize");
        String maxSizeString = form.get("maxTeamSize");
        int preferedSize;
        int minSize;
        int maxSize;
        try {
            preferedSize = Integer.parseInt(preferedSizeString);
            minSize = Integer.parseInt(minSizeString);
            maxSize = Integer.parseInt(maxSizeString);
        } catch (NumberFormatException e) {
            flash("error", ctx().messages().at("error.wrongInput"));
            return redirect(
                    controllers.routes.AdminPageController.allocationPage());
        }
        List<AllocationParameter> allocParam;
        try {
            allocParam = createParameters(minSize, maxSize, preferedSize, form);
        } catch (NumberFormatException e) {
            flash("error", ctx().messages().at(INTERNAL_ERROR));
            return redirect(
                    controllers.routes.AdminPageController.allocationPage());
        }
        AllocationQueue queue = AllocationQueue.getInstance();
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        List<Student> students = semester.getStudents();
        List<LearningGroup> learningGroups = semester.getLearningGroups();
        List<Project> projects = semester.getProjects();
        // configuration wird erstellt und hinzugefügt
        Configuration configuration = new Configuration(name, students,
                learningGroups, projects, allocParam);
        queue.addToQueue(configuration);
        return redirect(
                controllers.routes.AdminPageController.allocationPage());
    }

    private List<AllocationParameter> createParameters(int minSize, int maxSize,
            int preferedSize, DynamicForm form) throws NumberFormatException {
        // Liste der Parameter wird erstellt
        List<AllocationParameter> result = new ArrayList<>(); // die
        result.add(new AllocationParameter("minSize", minSize));
        result.add(new AllocationParameter("maxSize", maxSize));
        result.add(new AllocationParameter("prefSize", preferedSize));
        for (Criterion criterion : AllocationQueue.getInstance().getAllocator()
                .getAllCriteria()) {
            int value = Integer.parseInt(form.get(criterion.getName()));
            result.add(new AllocationParameter(criterion.getName(), value));
        }
        return result;
    }

    /**
     * Diese Methode löscht eine Einteilungskonfiguration aus der
     * Berechnungswarteschlange. Der Administrator wird anschließend auf die
     * Berechnungsübersichtsseite weitergeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result removeAllocationFromQueue() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        String configName = form.get("queue");
        AllocationQueue allocationQueue = AllocationQueue.getInstance();
        allocationQueue.cancelAllocation(configName);
        return redirect(
                controllers.routes.AdminPageController.allocationPage());
    }

    /**
     * Diese Methode fügt einen Studenten in das System hinzu. Der Administrator
     * wird anschließend auf die Seite zum weiteren Hinzufügen und Löschen von
     * Studenten weitergeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addStudent() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        String firstName = form.get("firstName");
        String lastName = form.get("lastName");
        String matNrString = form.get("matrnr");
        String email = form.get("email");
        String password = form.get("password");
        String semesterString = form.get("semester");
        int matNr;
        int semester;
        String spoIdString = form.get("spo");
        int spoId;
        try {
            matNr = Integer.parseInt(matNrString);
            semester = Integer.parseInt(semesterString);
            spoId = Integer.parseInt(spoIdString);
        } catch (NumberFormatException e) {
            flash("error", ctx().messages().at("error.wrongInput"));
            return redirect(
                    controllers.routes.AdminPageController.studentEditPage());
        }
        if (Student.getStudent(matNr) != null) {
            flash("error",
                    ctx().messages().at("admin.studentEdit.matrNrExistsError"));
            return redirect(
                    controllers.routes.AdminPageController.studentEditPage());
        }
        // der username eines studenten ist seine matNr
        SPO spo = ElipseModel.getById(SPO.class, spoId);
        BlowfishPasswordEncoder b = new BlowfishPasswordEncoder();
        Student student = new Student(matNrString, b.encode(password), email,
                firstName, lastName, matNr, spo, spo.getNecessaryAchievements(),
                new ArrayList<>(), semester);
        student.save();

        LearningGroup l;

        l = new LearningGroup(student.getUserName(), "");
        l.save();
        l.doTransaction(() -> {
            l.addMember(student);
            l.setPrivate(true);
            // Ratings initialisieren
            for (Project p : GeneralData.loadInstance().getCurrentSemester()
                    .getProjects()) {
                l.rate(p, 3);
            }
        });
        Semester currentSemester = GeneralData.loadInstance()
                .getCurrentSemester();
        currentSemester.doTransaction(() -> {
            currentSemester.addLearningGroup(l);
            currentSemester.addStudent(student);
        });
        return redirect(
                controllers.routes.AdminPageController.studentEditPage());
    }

    /**
     * Diese Methode löscht einen Studenten aus dem System. Der Administrator
     * wird anschließend auf die Seite zum weiteren Hinzufügen und Löschen von
     * Studenten weitergeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result removeStudent() {
        // TODO javascript warnung vor löschen
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        String matNrString = form.get("matrnr2");
        int matNr;
        try {
            matNr = Integer.parseInt(matNrString);
        } catch (NumberFormatException e) {
            flash("error", ctx().messages().at(INTERNAL_ERROR));
            return redirect(
                    controllers.routes.AdminPageController.studentEditPage());
        }
        Student student = Student.getStudent(matNr);
        if (student == null) {
            flash("error", ctx().messages()
                    .at("admin.studentEdit.noSuchStudentError"));
            return redirect(
                    controllers.routes.AdminPageController.studentEditPage());
        }
        for (LearningGroup l : LearningGroup.getLearningGroups()) {
            if (l.getMembers().contains(student)) {
                if (l.getMembers().size() == 1) {
                    l.delete();
                }
            }
        }
        student.delete();
        return redirect(
                controllers.routes.AdminPageController.studentEditPage());
    }

    /**
     * Diese Methode editiert die Daten des Administrators, welche er auf der
     * Account-Seite geändert hat.
     * 
     * @return die Seite, die als Antwort verschickt wird.
     */
    public Result editAccount() {
        UserManagement user = new UserManagement();
        Administrator admin = (Administrator) user.getUserProfile(ctx());
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }

        if (form.get("passwordChange") != null) {
            System.out.println("passwordChange1");
            String oldpw = form.get("oldPassword");
            String pw = form.get("newPassword");
            String pwrepeat = form.get("newPasswordRepeat");

            boolean matches = new BlowfishPasswordEncoder().matches(oldpw,
                    admin.getPassword());

            if (!pw.equals(pwrepeat) || !matches) {
                flash("error",
                        ctx().messages().at("admin.account.error.passwords"));
                return redirect(
                        controllers.routes.AdminPageController.accountPage());
            }
            String pwEnc = new BlowfishPasswordEncoder().encode(pw);
            admin.doTransaction(() -> {
                admin.setPassword(pwEnc);
            });
        }
        return redirect(controllers.routes.AdminPageController.accountPage());
    }
}
