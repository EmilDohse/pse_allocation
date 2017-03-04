package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;

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
}