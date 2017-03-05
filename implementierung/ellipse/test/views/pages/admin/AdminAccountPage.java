package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Account-Seite des Admins.
 */
public class AdminAccountPage extends Page {

    /**
     * Diese Methode gibt die Url der Seite zurück.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin/account";
    }

    /**
     * Diese Methode befüllt die Passwort-Ändern Form.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param oldPw
     *            Das alte Passwort.
     * @param newPw
     *            Das neue Passwort.
     */
    public void fillAndSubmitChangePwForm(TestBrowser browser, String oldPw, String newPw) {
        browser.$("#oldPassword").first().fill().with(oldPw);
        browser.$("#newPassword").first().fill().with(newPw);
        browser.$("#newPasswordRepeat").first().fill().with(newPw);
        browser.$("#submit_passwordChange").first().click();
    }
}
