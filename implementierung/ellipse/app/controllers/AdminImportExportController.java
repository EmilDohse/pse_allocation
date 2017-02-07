// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.io.File;
import java.util.ArrayList;

import com.google.inject.Inject;

import data.Allocation;
import data.GeneralData;
import data.SPO;
import exception.ImporterException;
import importExport.Importer;
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

    @Inject
    FormFactory formFactory;

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
            String fileName = importData.getFilename();
            String contentType = importData.getContentType();
            File file = importData.getFile();
            importExport.Importer importer = new Importer();
            try {// TODO wenn wir wollen können wir hier das file übergeben
                 // (api änderung)
                importer.importAllocation(file.getAbsolutePath(),
                        GeneralData.loadInstance().getCurrentSemester());
                return redirect(controllers.routes.AdminPageController
                        .exportImportPage(""));
            } catch (ImporterException e) {
                return redirect(controllers.routes.AdminPageController
                        .exportImportPage(ctx().messages().at(e.getMessage())));
            }
        }
        // TODO error message
        return redirect(controllers.routes.AdminPageController
                .exportImportPage("error"));

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
        File file = new File("/imExport/exportAllocation.csv");
        importer.exportAllocation(file.getAbsolutePath(),
                new Allocation(new ArrayList<>(), "hallo", new ArrayList<>()));
        return ok(file); // TODO auswahl der einteilungen

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
            String fileName = importData.getFilename();
            String contentType = importData.getContentType();
            File file = importData.getFile();
            importExport.Importer importer = new Importer();
            try {// TODO wenn wir wollen können wir hier das file übergeben
                 // (api änderung)
                importer.importSPO(file);
                return redirect(controllers.routes.AdminPageController
                        .exportImportPage(""));
            } catch (ImporterException e) {
                return redirect(controllers.routes.AdminPageController
                        .exportImportPage(ctx().messages().at(e.getMessage())));
            }
        }
        // TODO error message
        return redirect(controllers.routes.AdminPageController
                .exportImportPage("error"));

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
        try {
            importer.exportSPO(file, new SPO("haool"));
            // TODO spo auswahl
        } catch (ImporterException e) {
            return redirect(controllers.routes.AdminPageController
                    .exportImportPage(ctx().messages().at(e.getMessage())));
        }

        return ok(file);
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
            String fileName = importData.getFilename();
            String contentType = importData.getContentType();
            File file = importData.getFile();
            importExport.Importer importer = new Importer();
            try {// TODO wenn wir wollen können wir hier das file übergeben
                 // (api änderung)
                importer.importProjects(file,
                        GeneralData.loadInstance().getCurrentSemester());
                return redirect(controllers.routes.AdminPageController
                        .exportImportPage(""));

            } catch (ImporterException e) {
                return redirect(controllers.routes.AdminPageController
                        .exportImportPage(ctx().messages().at(e.getMessage())));
            }
        }
        // TODO error message
        return redirect(controllers.routes.AdminPageController
                .exportImportPage("error"));
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
            importer.exportProjects(file,
                    GeneralData.loadInstance().getCurrentSemester());
        } catch (ImporterException e) {
            return redirect(controllers.routes.AdminPageController
                    .exportImportPage(ctx().messages().at(e.getMessage())));
        }
        return ok(file);
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
            String fileName = importData.getFilename();
            String contentType = importData.getContentType();
            File file = importData.getFile();
            importExport.Importer importer = new Importer();
            try {// TODO wenn wir wollen können wir hier das file übergeben
                 // (api änderung)
                importer.importCMSData(file.getAbsolutePath(),
                        GeneralData.loadInstance().getCurrentSemester());
                return redirect(controllers.routes.AdminPageController
                        .exportImportPage(""));
            } catch (ImporterException e) {
                return redirect(controllers.routes.AdminPageController
                        .exportImportPage(ctx().messages().at(e.getMessage())));
            }
        }
        // TODO error message
        return redirect(controllers.routes.AdminPageController
                .exportImportPage("error"));
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
            importer.exportCMSData(file.getAbsolutePath(),
                    GeneralData.loadInstance().getCurrentSemester());
        } catch (ImporterException e) {
            return redirect(controllers.routes.AdminPageController
                    .exportImportPage(ctx().messages().at(e.getMessage())));
        }

        return ok(file);
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
            String fileName = importData.getFilename();
            String contentType = importData.getContentType();
            File file = importData.getFile();
            importExport.Importer importer = new Importer();
            try {// TODO wenn wir wollen können wir hier das file übergeben
                 // (api änderung)
                importer.importStudents(file,
                        GeneralData.loadInstance().getCurrentSemester());
                return redirect(controllers.routes.AdminPageController
                        .exportImportPage(""));
            } catch (ImporterException e) {
                return redirect(controllers.routes.AdminPageController
                        .exportImportPage(ctx().messages().at(e.getMessage())));
            }
        }
        // TODO error message
        return redirect(controllers.routes.AdminPageController
                .exportImportPage("error"));
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
            importer.exportStudents(file,
                    GeneralData.loadInstance().getCurrentSemester());
        } catch (ImporterException e) {
            return redirect(controllers.routes.AdminPageController
                    .exportImportPage(ctx().messages().at(e.getMessage())));
        }

        return ok(file);
    }
}
