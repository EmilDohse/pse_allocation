package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;
import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Import/Export-Seite des
 * Admins.
 */
public class AdminImExportPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin/exportImport";
    }

    /**
     * Diese Methode gibt zurück, ob der Browser eine Fehlermeldung anzeigt.
     * 
     * @param browser
     *            Der TestBrowser.
     * @return Wahr, wenn Fehlermeldung angezeigt wird, falsch sonst.
     */
    public boolean showsError(TestBrowser browser) {
        return !browser.$(".alert-danger").isEmpty();
    }

    /**
     * Diese Methode befüllt die Import-Form mit dem file path der Datei und
     * importiert diese.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param filePath
     *            Der Pfad der zu importierenden Datei.
     */
    public void importSpo(TestBrowser browser, String filePath) {
        browser.$("#file").first().fill().with(filePath);
        browser.$("#submit_spo_import").first().click();
    }

    /**
     * Diese Methode befüllt die Import-Form mit dem file path der Datei und
     * importiert diese.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param filePath
     *            Der Pfad der zu importierenden Datei.
     */
    public void importAllocation(TestBrowser browser, String filePath) {
        browser.$("#file").first().fill().with(filePath);
        browser.$("#submit_allocation_import").first().click();
    }

    /**
     * Diese Methode befüllt die Import-Form mit dem file path der Datei und
     * importiert diese.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param filePath
     *            Der Pfad der zu importierenden Datei.
     */
    public void importStudents(TestBrowser browser, String filePath) {
        browser.$("#file").first().fill().with(filePath);
        browser.$("#submit_students_import").first().click();
    }

    /**
     * Diese Methode befüllt die Import-Form mit dem file path der Datei und
     * importiert diese.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param filePath
     *            Der Pfad der zu importierenden Datei.
     */
    public void importProjects(TestBrowser browser, String filePath) {
        browser.$("#file").first().fill().with(filePath);
        browser.$("#submit_projects_import").first().click();
    }

    public void exportSpo(TestBrowser browser, int spoId) {
        FillSelectConstructor select = new FillSelectConstructor(
                "#spo-selection", FluentThread.get().getDriver());
        select.withValue(Integer.toString(spoId));
        browser.$("#submit_spo_export").first().click();
    }

    public void exportProjects(TestBrowser browser) {
        browser.$("#submit_projects_export").first().click();
    }

    public void exportStudents(TestBrowser browser) {
        browser.$("#submit_students_export").first().click();
    }

    public void exportAllocation(TestBrowser browser, int id) {
        FillSelectConstructor select = new FillSelectConstructor(
                "#allocation-selection", FluentThread.get().getDriver());
        select.withValue(Integer.toString(id));
        browser.$("#submit_allocation_export").first().click();
    }
}