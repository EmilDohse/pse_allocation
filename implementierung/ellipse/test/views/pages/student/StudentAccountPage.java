package views.pages.student;

import play.test.TestBrowser;
import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Account-Seite eines
 * Studenten.
 */
public class StudentAccountPage extends Page {

    /**
     * Diese Methode gibt die Url der Seite zurück.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/student/account";
    }

    /**
     * Diese Methode befüllt die Form zum Ändern des Passworts und klickt den
     * Ändern-Button.
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

    /**
     * Diese Methode befüllt die Form zum Ändern der E-Mail-Adresse und klickt
     * den Ändern-Button.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param email
     *            Die neue E-Mail-Adresse.
     */
    public void fillAndSubmitChangeEmailForm(TestBrowser browser, String email) {
        browser.$("#newEmail").first().fill().with(email);
        browser.$("#submit_emailChange").first().click();
    }

    /**
     * Diese Methode kickt den Reverify-Button.
     * 
     * @param browser
     *            Der TestBrowser.
     */
    public void clickReverify(TestBrowser browser) {
        browser.$("#submit_reverify").first().click();
    }
}