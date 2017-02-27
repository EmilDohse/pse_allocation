package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;

public class AdminProjectsPage extends Page {

    @Override
    public String getUrl() {
        return "/admin";
    }

    public void fillAndSubmitAddProjectForm(TestBrowser browser, String name) {
        browser.$("#name").first().fill().with(name);
        browser.$("#addProject").first().click();
    }

    public void gotoEditProjectPage(TestBrowser browser, int id) {
        browser.$("#edit-" + id).first().click();
    }

    public boolean isProjectShown(TestBrowser browser, int id) {
        return !browser.$("#edit-" + id).isEmpty();
    }
}