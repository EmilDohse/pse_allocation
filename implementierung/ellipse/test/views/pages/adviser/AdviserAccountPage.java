package views.pages.adviser;

import play.test.TestBrowser;
import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Account-Seite eines
 * Betreuers.
 */
public class AdviserAccountPage extends Page {

    /**
     * Diese Methode gibt die Url der Seite zurück.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/adviser/account";
    }

    /**
     * Diese Methode befüllt die Form zum Ändern des Passworts und ändert
     * dieses.
     * 
     * @param browser
     *            Der Testbrowser.
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

    /**
     * Diese Methode befüllt die Form zum Ändern der Email und ändert diese.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param email
     *            Die neue Email.
     */
    public void fillAndSubmitChangeEmailForm(TestBrowser browser, String email) {
        browser.$("#newEmail").first().fill().with(email);
        browser.$("#submit_emailChange").first().click();
    }
}