package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Adviser-Seite des Admins.
 */
public class AdminAdvisersPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin/advisers";
    }

    /**
     * Diese Methode befüllt die Form zum Hinzufügen eines Betreuers und fügt
     * ihn hinzu.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param firstName
     *            Der Vorname der Betreuers.
     * @param lastName
     *            Der Nachname des Betreuers.
     * @param email
     *            Die Email-Adresse des Betreuers.
     * @param password
     *            Das Passwort des Betreuers.
     */
    public void fillAndSubmitAddAdviserForm(TestBrowser browser, String firstName, String lastName, String email,
            String password) {
        browser.$("#firstName").first().fill().with(firstName);
        browser.$("#lastName").first().fill().with(lastName);
        browser.$("#email").first().fill().with(email);
        browser.$("#password").first().fill().with(password);
        browser.$("#addAdviser_submit").first().click();
    }

    /**
     * Diese Methode entfernt den Betreuer.
     * 
     * @param browser
     *            Der Testbrowser.
     * @param id
     *            Die Id des Betreuers.
     */
    public void removeAdviser(TestBrowser browser, int id) {
        browser.$("#removeAdviser-" + id).click();
    }

    /**
     * Diese Methode gibt zurück, ob ein Betreuer angezeigt wird.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param id
     *            Die Id des Betreuers.
     * @return Wahr, wenn der Betreuer angezeigt wird, falsch sonst.
     */
    public boolean isAdviserShown(TestBrowser browser, int id) {
        return !browser.$("#removeAdviser-" + id).isEmpty();
    }
}
