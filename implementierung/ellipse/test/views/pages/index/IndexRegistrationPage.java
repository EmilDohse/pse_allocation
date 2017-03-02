package views.pages.index;

import play.test.TestBrowser;
import views.pages.Page;
import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

public class IndexRegistrationPage extends Page {

    @Override
    public String getUrl() {
        return "/register";
    }

    public void fillAndSubmitRegistrtaionForm(TestBrowser browser,
            String firstName, String lastName, int matNr, String email,
            String password, int semester, int spoId) {
        browser.$("#firstName").first().fill().with(firstName);
        browser.$("#lastName").first().fill().with(lastName);
        browser.$("#matrnr").first().fill().with(Integer.toString(matNr));
        browser.$("#email").first().fill().with(email);
        browser.$("#pw").first().fill().with(password);
        browser.$("#rpw").first().fill().with(password);
        browser.$("#semester").first().fill().with(Integer.toString(semester));
        FillSelectConstructor select = new FillSelectConstructor("#spo",
                FluentThread.get().getDriver());
        select.withValue(Integer.toString(spoId));
        browser.$("#trueData").first().click();
        browser.$("#register_submit").first().click();
    }
}
