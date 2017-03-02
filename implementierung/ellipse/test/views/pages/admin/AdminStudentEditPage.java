package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;

import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

public class AdminStudentEditPage extends Page {

    @Override
    public String getUrl() {
        return "/admin/studentEdit";
    }

    public void fillAndSubmitAddStudentForm(TestBrowser browser,
            String firstName, String lastName, int matNr, String email,
            String password, int semester, int spoId) {
        browser.$("#firstName").first().fill().with(firstName);
        browser.$("#lastName").first().fill().with(lastName);
        browser.$("#matrnr").first().fill().with(Integer.toString(matNr));
        browser.$("#email").first().fill().with(email);
        browser.$("#password").first().fill().with(password);
        browser.$("#passwordRp").first().fill().with(password);
        browser.$("#semester").first().fill().with(Integer.toString(semester));
        FillSelectConstructor select = new FillSelectConstructor("#spo",
                FluentThread.get().getDriver());
        select.withValue(Integer.toString(spoId));
        browser.$("#add_submit").first().click();
    }

    public void fillAndSubmitRemoveStudentForm(TestBrowser browser, int matNr) {
        browser.$("#matrnr2").first().fill().with(Integer.toString(matNr));
        browser.$("#remove_submit").first().click();
    }
}