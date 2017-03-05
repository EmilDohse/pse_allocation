package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;

import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Studenten-Editieren-Seite
 * des Admins.
 */
public class AdminStudentEditPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin/studentEdit";
    }

    /**
     * Diese Methode befüllt die Form zum Erstellen eines Studenten und erstellt
     * diesen.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param firstName
     *            Der Vorname des Studenten.
     * @param lastName
     *            Der Nachname der Studenten.
     * @param matNr
     *            Die Matrikelnummer der Studenten.
     * @param email
     *            Die Email-Adresse der Studenten.
     * @param password
     *            Das Passwort des Studenten.
     * @param semester
     *            Das Semester des Studenten.
     * @param spoId
     *            Die SPO ID des Studenten.
     */
    public void fillAndSubmitAddStudentForm(TestBrowser browser, String firstName, String lastName, int matNr,
            String email, String password, int semester, int spoId) {
        browser.$("#firstName").first().fill().with(firstName);
        browser.$("#lastName").first().fill().with(lastName);
        browser.$("#matrnr").first().fill().with(Integer.toString(matNr));
        browser.$("#email").first().fill().with(email);
        browser.$("#password").first().fill().with(password);
        browser.$("#passwordRp").first().fill().with(password);
        browser.$("#semester").first().fill().with(Integer.toString(semester));
        FillSelectConstructor select = new FillSelectConstructor("#spo", FluentThread.get().getDriver());
        select.withValue(Integer.toString(spoId));
        browser.$("#add_submit").first().click();
    }

    /**
     * Diese Methode befüllt die Form zum Entfernen eines Studenten und entfernt
     * diesen.
     * 
     * @param browser
     *            Der Testbrowser.
     * @param matNr
     *            Die Matrikelnummer des Studenten.
     */
    public void fillAndSubmitRemoveStudentForm(TestBrowser browser, int matNr) {
        browser.$("#matrnr2").first().fill().with(Integer.toString(matNr));
        browser.$("#remove_submit").first().click();
    }
}