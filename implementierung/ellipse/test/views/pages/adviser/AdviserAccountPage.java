package views.pages.adviser;

import static org.junit.Assert.*;

import play.test.TestBrowser;
import views.pages.Page;

public class AdviserAccountPage extends Page {

    @Override
    public String getUrl() {
        return "/adviser/account";
    }

    public void fillAndSubmitChangePwForm(TestBrowser browser, String oldPw,
            String newPw) {
        browser.$("#oldPassword").first().fill().with(oldPw);
        browser.$("#newPassword").first().fill().with(newPw);
        browser.$("#newPasswordRepeat").first().fill().with(newPw);
        browser.$("#submit_passwordChange").first().click();
    }

    public void fillAndSubmitChangeEmailForm(TestBrowser browser,
            String email) {
        browser.$("#newEmail").first().fill().with(email);
        browser.$("#submit_emailChange").first().click();
    }
}