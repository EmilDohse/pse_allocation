package views.pages.student;

import play.test.TestBrowser;
import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Lerngruppe-Seite eines
 * Studenten.
 */
public class StudentLearningGroupPage extends Page {

    /**
     * Diese Methode vollzieht den Wechsel zu Passwort.Zurücksetzen Seite.
     * 
     * @param browser
     *            Der TestBrowser.
     */
    @Override
    public String getUrl() {
        return "/student";
    }

    /**
     * Diese Methode gibt zurück, ob die Form zum Erstellen einer Lerngruppe
     * vorhanden ist.
     * 
     * @param browser
     *            Der TestBrowser.
     * @return Wahr, wenn die Form zum Erstellen einer Lerngruppe vorhanden ist,
     *         falsch sonst.
     */
    public boolean isCreateLearningGroupFormPresent(TestBrowser browser) {
        return !browser.$("#create_submit").isEmpty();
    }

    /**
     * Diese Methode befüllt die Form zum erstellen einer Lerngruppe und klickt
     * den Erstellen-Button.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param name
     *            Der Name der Lerngruppe.
     * @param password
     *            Das Passwort der Lerngruppe.
     */
    public void fillAndSubmitCreateLGForm(TestBrowser browser, String name, String password) {
        browser.$("#learningGroupname").first().fill().with(name);
        browser.$("#learningGroupPassword").first().fill().with(password);
        browser.$("#create_submit").first().click();
    }

    /**
     * Diese Methode befüllt die Form zum Beitreten einer Lerngruppe und klickt
     * den Beitreten-Button.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param name
     *            Der Name der Lerngruppe.
     * @param password
     *            Das Passwort der Lerngruppe.
     */
    public void fillAndSubmitJoinLGForm(TestBrowser browser, String name, String password) {
        browser.$("#learningGroupname").first().fill().with(name);
        browser.$("#learningGroupPassword").first().fill().with(password);
        browser.$("#join_submit").first().click();
    }

    /**
     * Diese Methode klickt den Verlassen-Button.
     * 
     * @param browser
     *            Der TestBrowser.
     */
    public void leaveLearningGroup(TestBrowser browser) {
        browser.$("#leave_submit").first().click();
    }
}
