package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Account-Seite des Admins.
 */
public class AdminAccountPage extends Page {

    /**
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin/account";
    }

    public void fillAndSubmitChangePwForm(TestBrowser browser, String oldPw, String newPw) {
        browser.$("#oldPassword").first().fill().with(oldPw);
        browser.$("#newPassword").first().fill().with(newPw);
        browser.$("#newPasswordRepeat").first().fill().with(newPw);
        browser.$("#submit_passwordChange").first().click();
    }
}
