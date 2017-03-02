package views.pages.admin;

import static org.junit.Assert.*;

import play.test.TestBrowser;
import views.pages.Page;

public class AdminAccountPage extends Page {

    @Override
    public String getUrl() {
        return "/admin/account";
    }

    public void fillAndSubmitChangePwForm(TestBrowser browser, String oldPw,
            String newPw) {
        browser.$("#oldPassword").first().fill().with(oldPw);
        browser.$("#newPassword").first().fill().with(newPw);
        browser.$("#newPasswordRepeat").first().fill().with(newPw);
        browser.$("#submit_passwordChange").first().click();
    }
}
