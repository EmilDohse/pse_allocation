// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.io.File;

import com.google.inject.Inject;

import data.Allocation;
import data.ElipseModel;
import data.GeneralData;
import data.SPO;
import exception.ImporterException;
import form.IntValidator;
import form.ValidationException;
import importExport.Importer;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

/************************************************************/
/**
 * Dieser Controller ist für das Bearbeiten der Http-Requests zuständig, welche
 * Importieren und Exportieren auf der Import/Export-Seite regeln.
 */
public class AdminImportExportController extends Controller {

    private static final String ATTACHMENT = "attachment";
    private static final String ERROR = "error";
    private static final String INTERNAL_ERROR    = "error.internalError";
    private static final String NO_FILE           = "importer.noFile";
    private static final String NOTHING_TO_EXPORT = "admin.eximport.nothingToExport ";

    @Inject
    FormFactory                 formFactory;

    /**
     * Diese Methode importiert eine Datei aus einem Formular.
     * 
     * @return Die Seite, die angezeigt werden soll.
     */
    public Result importGeneral() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.get("allocation") != null) {
            return importAllocation();
        } else if (form.get("spo") != null) {
            return importSPO();
        } else if (form.get("students") != null) {
            return importStudents();
        } else if (form.get("projects") != null) {
            return importProjects();
        } else {
            flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
            return redirect(controllers.routes.AdminPageController.exportImportPage());
        }
    }

    /**
     * Diese Methode importiert eine Einteilung, sodass sie in der
     * Einteilungsübersicht des aktuellen semesters erscheint. Der Administrator
     * wird daraufhin auf die Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result importAllocation() {

        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> importData = body.getFile("file");

        if (importData != null) {
            File file = importData.getFile();
            importExport.Importer importer = new Importer();
            try {
                importer.importAllocation(file, GeneralData.loadInstance().getCurrentSemester());
                return redirect(controllers.routes.AdminPageController.exportImportPage());
            } catch (ImporterException e) {
                flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
                return redirect(controllers.routes.AdminPageController.exportImportPage());
            }
        }

        flash(ERROR, ctx().messages().at(ctx().messages().at(NO_FILE)));
        return redirect(controllers.routes.AdminPageController.exportImportPage());

    }

    /**
     * Diese Methode lässt den Administrator eine csv-Datei downloaden, welche
     * eine Einteilung speichert. Der Administrator wird daraufhin auf die
     * Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result exportAllocation() {
        importExport.Importer importer = new Importer();
        File file = new File("exportAllocation.csv");
        DynamicForm form = formFactory.form().bindFromRequest();
        int allocationId;
        IntValidator intValidator = new IntValidator(0);
        try {
            allocationId = intValidator.validate(form.get("allocation-selection"));
        } catch (ValidationException e) {
            flash(ERROR, ctx().messages().at(ctx().messages().at(NOTHING_TO_EXPORT)));
            return redirect(controllers.routes.AdminPageController.exportImportPage());
        }
        try {
            importer.exportAllocation(file, ElipseModel.getById(Allocation.class, allocationId));
        } catch (ImporterException e) {
            flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
            return redirect(controllers.routes.AdminPageController.exportImportPage());
        }
        return ok(file).withHeader(CONTENT_DISPOSITION, ATTACHMENT);

    }

    /**
     * Diese Methode importiert eine SPO, sodass sie in der SPO-Auswahl eines
     * Semesters erscheint. Der Administrator wird daraufhin auf die
     * Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result importSPO() {
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> importData = body.getFile("file");

        if (importData != null) {
            File file = importData.getFile();
            importExport.Importer importer = new Importer();
            try {
                importer.importSPO(file);
                return redirect(controllers.routes.AdminPageController.exportImportPage());
            } catch (ImporterException e) {
                flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
                return redirect(controllers.routes.AdminPageController.exportImportPage());
            }
        }
        flash(ERROR, ctx().messages().at(ctx().messages().at(NO_FILE)));
        return redirect(controllers.routes.AdminPageController.exportImportPage());

    }

    /**
     * Diese Methode lässt den Administrator eine csv-Datei downloaden, welche
     * eine SPO speichert. Der Administrator wird daraufhin auf die
     * Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result exportSPO() {
        importExport.Importer importer = new Importer();
        File file = new File("exportSPO.csv");
        DynamicForm form = formFactory.form().bindFromRequest();

        int spoId;
        try {
            IntValidator intValidator = new IntValidator(0);
            spoId = intValidator.validate(form.get("spo-selection"));
        } catch (ValidationException e) {
            flash(ERROR, ctx().messages().at(ctx().messages().at(NOTHING_TO_EXPORT)));
            return redirect(controllers.routes.AdminPageController.exportImportPage());
        }
        try {
            importer.exportSPO(file, ElipseModel.getById(SPO.class, spoId));
        } catch (ImporterException e) {
            flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
            return redirect(controllers.routes.AdminPageController.exportImportPage());
        }

        return ok(file).withHeader(CONTENT_DISPOSITION, ATTACHMENT);
    }

    /**
     * Diese Methode importiert eine Liste an Projekten, welche daraufhin zum
     * aktuellen Semester hinzugefügt werden. Der Administrator wird daraufhin
     * auf die Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result importProjects() {
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> importData = body.getFile("file");

        if (importData != null) {
            File file = importData.getFile();
            importExport.Importer importer = new Importer();
            try {
                importer.importProjects(file, GeneralData.loadInstance().getCurrentSemester());
                return redirect(controllers.routes.AdminPageController.exportImportPage());

            } catch (ImporterException e) {
                flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
                return redirect(controllers.routes.AdminPageController.exportImportPage());
            }
        }
        flash(ERROR, ctx().messages().at(ctx().messages().at(NO_FILE)));
        return redirect(controllers.routes.AdminPageController.exportImportPage());
    }

    /**
     * Diese Methode lässt den Administrator eine csv-Datei downloaden, welche
     * alle Projekte des aktuellen Semesters abspeichert. Der Administrator wird
     * daraufhin auf die Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result exportProjects() {
        importExport.Importer importer = new Importer();
        File file = new File("exportProjects.csv");

        try {
            importer.exportProjects(file, GeneralData.loadInstance().getCurrentSemester());
        } catch (ImporterException e) {
            flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
            return redirect(controllers.routes.AdminPageController.exportImportPage());
        }
        return ok(file).withHeader(CONTENT_DISPOSITION, ATTACHMENT);
    }

    /**
     * Diese Methode importiert eine csv-Datei mit Daten aus dem CMS
     * (CampusManagementSystem) und fügt die Daten zu den bereits vorhandenen
     * hinzu (im aktuellen semester). Der Administrator wird daraufhin auf die
     * Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result importCMSData() {
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> importData = body.getFile("file");

        if (importData != null) {
            File file = importData.getFile();
            importExport.Importer importer = new Importer();
            try {
                importer.importCMSData(file.getAbsolutePath(), GeneralData.loadInstance().getCurrentSemester());
                return redirect(controllers.routes.AdminPageController.exportImportPage());
            } catch (ImporterException e) {
                flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
                return redirect(controllers.routes.AdminPageController.exportImportPage());
            }
        }
        flash(ERROR, ctx().messages().at(ctx().messages().at(NO_FILE)));
        return redirect(controllers.routes.AdminPageController.exportImportPage());
    }

    /**
     * Diese Methode lässt den Administrator eine csv-Datei downloaden, welche
     * die Studenten des aktuellen Semester mit den eingetragenen TSE und PSE
     * Noten enthält. Der Administrator wird daraufhin auf die
     * Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result exportCMSData() {
        importExport.Importer importer = new Importer();
        File file = new File("exportCMS.csv");
        try {
            importer.exportCMSData(file.getAbsolutePath(), GeneralData.loadInstance().getCurrentSemester());
        } catch (ImporterException e) {
            flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
            return redirect(controllers.routes.AdminPageController.exportImportPage());
        }

        return ok(file).withHeader(CONTENT_DISPOSITION, ATTACHMENT);
    }

    /**
     * Diese Methode importiert eine Liste an Studenten, welche daraufhin zum
     * aktuelle Semester hinzugefügt werden. Der Administrator wird daraufhin
     * auf die Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result importStudents() {
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> importData = body.getFile("file");

        if (importData != null) {
            File file = importData.getFile();
            importExport.Importer importer = new Importer();
            try {
                importer.importStudents(file, GeneralData.loadInstance().getCurrentSemester());
                return redirect(controllers.routes.AdminPageController.exportImportPage());
            } catch (ImporterException e) {
                flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
                return redirect(controllers.routes.AdminPageController.exportImportPage());
            }
        }
        flash(ERROR, ctx().messages().at(ctx().messages().at(NO_FILE)));
        return redirect(controllers.routes.AdminPageController.exportImportPage());
    }

    /**
     * Diese Methode lässt den Administrator eine csv-Datei downloaden, welche
     * alle Studenten des aktuellen Semesters abspeichert. Der Administrator
     * wird daraufhin auf die Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result exportStudents() {
        importExport.Importer importer = new Importer();
        File file = new File("exportStudents.csv");
        try {
            importer.exportStudents(file, GeneralData.loadInstance().getCurrentSemester());
        } catch (ImporterException e) {
            flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
            return redirect(controllers.routes.AdminPageController.exportImportPage());
        }

        return ok(file).withHeader(CONTENT_DISPOSITION, ATTACHMENT);
    }

    /**
     * Diese Methode lässt den Administrator eine csv-Datei downloaden, welche
     * alle Noten der Studenten des aktuellen Semesters abspeichert. Der
     * Administrator wird daraufhin auf die Import/Export-Seite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result exportGrades() {
        importExport.Importer importer = new Importer();
        File file = new File("exportGrades.csv");
        try {
            importer.exportGrades(file, GeneralData.loadInstance().getCurrentSemester());
        } catch (ImporterException e) {
            flash(ERROR, ctx().messages().at(ctx().messages().at(e.getMessage())));
            return redirect(controllers.routes.AdminPageController.exportImportPage());
        }

        return ok(file).withHeader(CONTENT_DISPOSITION, ATTACHMENT);
    }
}
