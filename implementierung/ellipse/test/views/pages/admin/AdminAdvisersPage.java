package views.pages.admin;

import static org.junit.Assert.*;

import play.test.TestBrowser;
import views.pages.Page;

public class AdminAdvisersPage extends Page {

    @Override
    public String getUrl() {
        return "/admin/advisers";
    }

    public void fillAndSubmitAddAdviserForm(TestBrowser browser,
            String firstName, String lastName, String email, String password) {
        browser.$("#firstName").first().fill().with(firstName);
        browser.$("#lastName").first().fill().with(lastName);
        browser.$("#email").first().fill().with(email);
        browser.$("#password").first().fill().with(password);
        browser.$("#addAdviser_submit").first().click();
    }

    public void removeAdviser(TestBrowser browser, int id) {
        browser.$("#removeAdviser-" + id).click();
    }

    public boolean isAdviserShown(TestBrowser browser, int id) {
        return !browser.$("#removeAdviser-" + id).isEmpty();
    }
}
