package views.pages.admin;

import allocation.AllocationQueue;
import allocation.Criterion;
import play.test.TestBrowser;
import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Einteilungs-Seite des
 * Admins.
 */
public class AdminAllocationPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin/allocation";
    }

    /**
     * Diese Methode befüllt die Form zum Hinzufügen einer Einteilung und fügt
     * diese hinzu.
     * 
     * @param browser
     *            Der TestBrowser
     * @param name
     *            Der Name der Einteilung.
     * @param min
     *            Die minimale Teamgröße.
     * @param prefered
     *            Die bevorzugte Teamgröße.
     * @param max
     *            Die maximale Teamgröße.
     * @param criterionParams
     *            Die Kriterienparameter.
     */
    public void fillAndSubmitAddAllocationForm(TestBrowser browser, String name, int min, int prefered, int max,
            int... criterionParams) {
        browser.$("#name").first().fill().with(name);
        browser.$("#minTeamSize").first().fill().with(Integer.toString(min));
        browser.$("#preferedTeamSize").first().fill().with(Integer.toString(prefered));
        browser.$("#maxTeamSize").first().fill().with(Integer.toString(max));
        int i = 0;
        for (Criterion c : AllocationQueue.getInstance().getAllocator().getAllCriteria()) {
            browser.$("#" + c.getName()).first().fill().with(Integer.toString(criterionParams[i]));
            if (i < criterionParams.length - 1) {
                i++;
            }
        }
        browser.$("#add_submit").first().click();
    }

    /**
     * Diese Methode gibt zurück, ob die Einteilungsliste nicht leer ist.
     * 
     * @param browser
     *            Der TestBrowser.
     * @return Wahr, wenn die Liste nicht leer ist, false sonst.
     */
    public boolean isAllocationNotEmpty(TestBrowser browser) {
        return !browser.$("#queue").isEmpty();
    }
}