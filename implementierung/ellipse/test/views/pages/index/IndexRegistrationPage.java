package views.pages.index;

import play.test.TestBrowser;
import views.pages.Page;
import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

/**
 * Diese Klasse beinhaltet Methoden für den Inhalt der
 * Index-Registrierungs-Seite.
 */
public class IndexRegistrationPage extends Page {

    /**
     * Diese Methode vollzieht den Wechsel zu Passwort.Zurücksetzen Seite.
     * 
     * @param browser
     *            Der TestBrowser.
     */
    @Override
    public String getUrl() {
        return "/register";
    }

    /**
     * Diese Methode befüllt die Form zum Registrieren und klickt den
     * Registrieren-Button.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param firstName
     *            Der Vorname.
     * @param lastName
     *            Der Nachname.
     * @param matNr
     *            Die Matrikelnummer.
     * @param email
     *            Die E-Mail-Adresse.
     * @param password
     *            Das PAsswort.
     * @param semester
     *            Das Semseter.
     * @param spoId
     *            Die SPO-ID.
     */
    public void fillAndSubmitRegistrtaionForm(TestBrowser browser, String firstName, String lastName, int matNr,
            String email, String password, int semester, int spoId) {
        browser.$("#firstName").first().fill().with(firstName);
        browser.$("#lastName").first().fill().with(lastName);
        browser.$("#matrnr").first().fill().with(Integer.toString(matNr));
        browser.$("#email").first().fill().with(email);
        browser.$("#pw").first().fill().with(password);
        browser.$("#rpw").first().fill().with(password);
        browser.$("#semester").first().fill().with(Integer.toString(semester));
        FillSelectConstructor select = new FillSelectConstructor("#spo", FluentThread.get().getDriver());
        select.withValue(Integer.toString(spoId));
        browser.$("#trueData").first().click();
        browser.$("#register_submit").first().click();
    }
}
