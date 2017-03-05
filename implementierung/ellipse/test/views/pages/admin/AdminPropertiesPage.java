package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;

import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

import java.util.concurrent.TimeUnit;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Einstellungs-Seite des
 * Admins.
 */
public class AdminPropertiesPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin/properties";
    }

    /**
     * Methode zum hinzufügen einer SPO
     * 
     * @param browser
     *            der Browser
     */
    public void addSpo(TestBrowser browser) {
        browser.$("#add_spo_link").first().click();
    }

    /**
     * Methode zum ändern einer Spo
     * 
     * @param id
     *            die spo id
     * @param name
     *            der neue Name
     */
    public void changeSpo(TestBrowser browser, int id, String name) {
        browser.$("#spo-tab-" + id).first().click();
        browser.$("#nameSPO-" + id).first().fill().with(name);
        browser.$("#change_spo_submit-" + id).first().click();
    }

    /**
     * Methode zum hinzufügen eines Semesters
     * 
     * @param browser
     *            der Browser
     */
    public void addSemester(TestBrowser browser) {
        browser.$("#add_semester_link").first().click();
    }

    /**
     * Methode zum ändern eines Semesters
     * 
     * @param id
     *            die semester id
     * @param name
     *            der neue Name
     */
    public void changeSemester(TestBrowser browser, int id, String name,
            String info, int maxGroupSize, int... spoIds) {
        browser.$("#semester-tab-" + id).first().click();
        browser.$("#name2-" + id).first().fill().with(name);
        browser.$("#info-" + id).first().fill().with(info);
        browser.$("#maxGroupSize-" + id).first().fill()
                .with(Integer.toString(maxGroupSize));
        FillSelectConstructor select = new FillSelectConstructor(
                "#availabe-spo-" + id, FluentThread.get().getDriver());
        for (int spo : spoIds) {
            select.withValue(Integer.toString(spo));
        }
        browser.$("#change_semester_submit-" + id).first().click();
    }
}