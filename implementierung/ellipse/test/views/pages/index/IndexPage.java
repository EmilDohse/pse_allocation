package views.pages.index;

import static org.junit.Assert.assertTrue;

import org.fluentlenium.core.FluentPage;

import play.test.TestBrowser;
import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden für den Inhalt der Indexseite.
 */
public class IndexPage extends Page {

    /**
     * Diese Methode vollzieht den Wechsel zu Passwort.Zurücksetzen Seite.
     * 
     * @param browser
     *            Der TestBrowser.
     */
    public void gotoPasswordResetPage(TestBrowser browser) {
        browser.$("#pwReset").click();
    }

    /**
     * Diese Methode gibt die Url der Seite zurück.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/";
    }
}
