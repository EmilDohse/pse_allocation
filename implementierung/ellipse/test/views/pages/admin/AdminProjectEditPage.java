package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;

import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der ProjektEditieren-Seite des
 * Admins.
 */
public class AdminProjectEditPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin/projectEdit/";
    }

    /**
     * Diese Methode testet, ob es sich um die Editieren Seite eines bestimmten
     * Projektes handelt.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param id
     *            Die Projekt id.
     * @return Wahr, wenn es die Editieren-Seite des Projektes ist, falsch
     *         sonst.
     */
    public boolean isEditPageFromProject(TestBrowser browser, int id) {
        int editPageId = Integer.parseInt(browser.$("#id").first().getValue());
        return editPageId == id;
    }

    /**
     * Entfernen des Projekts.
     * 
     * @param browser
     *            Der TestBrowser.
     */
    public void removeProject(TestBrowser browser) {
        browser.$("#removeProject").first().click();
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
     *            Die Projektbeschreibung.
     * @param numOfTeams
     *            Die Anzahl der angebotenen Teams.
     * @param min
     *            Die minimale Teamgröße.
     * @param max
     *            Die maximale Teamgröße.
     * 
     * @param advisers
     *            Die Betreuer des Projektes.
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
}