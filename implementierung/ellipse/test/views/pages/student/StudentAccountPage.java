package views.pages.student;

import play.test.TestBrowser;
import views.pages.Page;

public class StudentAccountPage extends Page {

    @Override
    public String getUrl() {
        return "/student/account";
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

    public void clickReverify(TestBrowser browser) {
        browser.$("#submit_reverify").first().click();
    }
}