package views.pages.student;

import play.test.TestBrowser;
import views.pages.Page;
import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

/**
 * Diese Klasse beinhaltet Methoden zum befüllen der Seite eines Studeierenden,
 * der sich zum zweiten mal anmeldet.
 * 
 * @author samue
 *
 */
public class OldStudentPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/studentOld/changeForm";
    }

    /**
     * Diese Methode befüllt die Form zum Registrieren und klickt den
     * Registrieren-Button.
     *
     * @param semester
     *            Das Semseter.
     * @param spoId
     *            Die SPO-ID.
     */
    public void fillAndSubmitOldDataForm(TestBrowser browser, int semester,
            int spoId) {
        browser.$("#semester").first().fill().with(Integer.toString(semester));
        FillSelectConstructor select = new FillSelectConstructor("#spo",
                FluentThread.get().getDriver());
        select.withValue(Integer.toString(spoId));
        browser.$("#trueData").first().click();
        browser.$("#submit").first().click();
    }
}
