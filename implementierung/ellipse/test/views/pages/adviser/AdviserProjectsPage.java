package views.pages.adviser;

import static org.junit.Assert.assertTrue;

import play.test.TestBrowser;
import views.pages.Page;

import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Projekt-Seite eines
 * Betreuers.
 */
public class AdviserProjectsPage extends Page {

    /**
     * Diese Methode gibt die Url der Seite zurück.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/adviser";
    }

    /**
     * Diese Seite gitb zurück, ob man sich auf der Projektseite befindet.
     */
    @Override
    public void isAt() {
        assertTrue(url().startsWith(getUrl()));
        assertTrue(!url().contains("/adviser/account"));
    }

    /**
     * Diese Methode klickt den Button zum Erstellen eines Projektes.
     * 
     * @param browser
     */
    public void createProject(TestBrowser browser) {
        browser.$("#addProject_button").first().click();
    }

    /**
     * Diese Methode befüllt die Form zum Editieren eines Projekts und ändert
     * dieses.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param name
     *            Der Name des Projekts.
     * @param url
     *            Die Url des Projekts.
     * @param institute
     *            Das Institut des Projekts.
     * @param description
     *            Die Beschreibung des Projekts.
     * @param numOfTeams
     *            Die Anzahl der angebotenen Teams.
     * @param min
     *            Die minimale Teamgröße.
     * @param max
     *            Die maximale Teamgröße.
     * @param advisers
     *            Die Betreuer.
     */
    public void fillAndSubmitEditProjectForm(TestBrowser browser, String name, String url, String institute,
            String description, int numOfTeams, int min, int max, int... advisers) {
        browser.$("#name").first().fill().with(name);
        browser.$("#url").first().fill().with(url);
        browser.$("#institute").first().fill().with(institute);
        browser.$("#description").first().fill().with(description);
        browser.$("#teamCount").first().fill().with(Integer.toString(numOfTeams));
        browser.$("#minSize").first().fill().with(Integer.toString(min));
        browser.$("#maxSize").first().fill().with(Integer.toString(max));
        FillSelectConstructor select = new FillSelectConstructor("#adviser-selection", FluentThread.get().getDriver());
        for (Integer adviser : advisers) {
            select.withValue(Integer.toString(adviser));
        }
        browser.$("#edit_submit").first().click();
    }

    /**
     * Diese Methode klickt den Button zum Beitreten eines Projekts.
     * 
     * @param browser
     *            Der TestBrowser.
     */
    public void join(TestBrowser browser) {
        browser.$("#joinProject").first().click();
    }

    /**
     * Diese Methode klickt den Button zum Verlassen eines Projekts.
     * 
     * @param browser
     *            Der TestBrowser.
     */
    public void leave(TestBrowser browser) {
        browser.$("#leaveProject").first().click();
    }

    /**
     * Diese Methode klickt den Button zum Entfernen eines Projekts.
     * 
     * @param browser
     *            Der TestBrowser.
     */
    public void remove(TestBrowser browser) {
        browser.$("#removeProject").first().click();
    }

    /**
     * Diese Methode gibt zurück, ob man sich auf der Projekt-Editieren-Seite
     * befindet.
     * 
     * @param browser
     *            Der TestBrowser.
     * @return Wahr, wenn man sich auf der ProjektEditieren Seite ist, falsch
     *         sonst.
     */
    public boolean isOnEditPage(TestBrowser browser) {
        return browser.$("form").size() > 1;
    }
}
