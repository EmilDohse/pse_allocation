package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;

public class AdminResultsPage extends Page {

    @Override
    public String getUrl() {
        return "/admin/results";
    }

    public boolean isAllocationPresent(TestBrowser browser, int id) {
        return !browser.$("#allocation-" + id).isEmpty();
    }
}