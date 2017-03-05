package views.pages.index;

import org.fluentlenium.core.FluentPage;

import play.test.TestBrowser;

/**
 * Diese Klasse beinhaltet Methoden für den Inhalt der IndexInformationsseite.
 */
public class IndexInformationPage extends IndexPage {

    /**
     * Diese Methode gibt den Infotext zurück.
     * 
     * @param browser
     *            Der TestBrowser.
     * @return Der Infotext.
     */
    public String getInfoText(TestBrowser browser) {
        return browser.$("#generalInformation").getText();
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

    /**
     * Diese Methode gibt zurück, ob der Browser eine Fehlermeldung anzeigt.
     * 
     * @param browser
     *            Der TestBrowser.
     * @return Wahr, wenn Fehlermeldung angezeigt wird, falsch sonst.
     */
    public boolean showsError(TestBrowser browser) {
        return !browser.$(".alert-danger").isEmpty();
    }
}
