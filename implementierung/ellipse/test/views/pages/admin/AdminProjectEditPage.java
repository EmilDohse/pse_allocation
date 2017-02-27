package views.pages.admin;

import static org.junit.Assert.assertTrue;

import play.test.TestBrowser;
import views.pages.Page;

public class AdminProjectEditPage extends Page {

    @Override
    public String getUrl() {
        return "/admin/projectEdit/";
    }

    public boolean isEditPageFromProject(TestBrowser browser, int id) {
        int editPageId = Integer.parseInt(browser.$("#id").first().getValue());
        return editPageId == id;
    }

    public void removeProject(TestBrowser browser) {
        browser.$("#removeProject").first().click();
    }
}