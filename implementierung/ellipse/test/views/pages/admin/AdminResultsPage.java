package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NoSuchElementException;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Einteilungsergebnis-Seite
 * des Admins.
 */
public class AdminResultsPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin/results";
    }

    /**
     * Diese Methode gibt zurück, ob eine betimmte Einteilung vorhanden ist.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param id
     *            Die id der Einteilung.
     * @return Wahr, wenn die Einteilung vorhanden ist, falsch sonst.
     */
    public boolean isAllocationPresent(TestBrowser browser, int id) {
        try {
            return !browser.$("#allocation-" + id).isEmpty();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Diese Methode wartet solange bis eine Einteilung vorhanden ist.
     * 
     * @param browser
     *            Der TestBrowser.
     */
    public void waitUntilAllocationIsPresent(TestBrowser browser) {
        while (!browser.$("#noAllocationYet").isEmpty()) {
            this.go();
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage(this).isAt();
        }
        this.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(this).isAt();
    }

    /**
     * Diese Methode dulpiziert eine bestimmte Einteilung.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param id
     *            Die ID der Einteilung.
     */
    public void duplicateAllocation(TestBrowser browser, int id) {
        System.out.println(browser.$("#allocation-tabs").first().html());
        browser.$("#allocation-tab-" + id).first().click();
        browser.$("#duplicate_submit_" + id).first().click();
    }
}