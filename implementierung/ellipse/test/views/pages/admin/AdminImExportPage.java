package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;
import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

public class AdminImExportPage extends Page {

    @Override
    public String getUrl() {
        return "/admin/exportImport";
    }

    public boolean showsError(TestBrowser browser) {
        return !browser.$(".alert-danger").isEmpty();
    }

    public void importSpo(TestBrowser browser, String filePath) {
        browser.$("#file").first().fill().with(filePath);
        browser.$("#submit_spo_import").first().click();
    }

    public void importAllocation(TestBrowser browser, String filePath) {
        browser.$("#file").first().fill().with(filePath);
        browser.$("#submit_allocation_import").first().click();
    }

    public void importStudents(TestBrowser browser, String filePath) {
        browser.$("#file").first().fill().with(filePath);
        browser.$("#submit_students_import").first().click();
    }

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