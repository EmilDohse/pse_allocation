package views;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import data.Adviser;
import data.ElipseModel;
import views.pages.adviser.AdviserAccountPage;
import views.pages.adviser.AdviserProjectsPage;

import static org.junit.Assert.*;

/**
 * Diese Klasse enthält Tests für den Account-View eines Betreuers.
 */
public class AdviserAccountViewTest extends ViewTest {

    private AdviserAccountPage  accountPage;
    private AdviserProjectsPage projectPage;

    private final static String newPassword = "asdasdasdas";

    private int                 id;

    /**
     * Initialisieren der Test-Seiten und Test-Daten.
     */
    @Before
    @Override
    public void before() {
        super.before();
        accountPage = browser.createPage(AdviserAccountPage.class);
        projectPage = browser.createPage(AdviserProjectsPage.class);
        id = TestHelpers.createAdviser("betruer@kit.edu", "asdasdasdkl");
        login("betruer@kit.edu", "asdasdasdkl", projectPage);
        accountPage.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage).isAt();
    }

    /**
     * Test für das Passwortändern des Betreuers.
     */
    @Test
    public void changePassword() {
        accountPage.fillAndSubmitChangePwForm(browser, "asdasdasdkl", newPassword);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage).isAt();
        logout();
        assertTrue(login("betruer@kit.edu", newPassword, projectPage));
    }

    /**
     * Test für das Ändern der E-Mail-Adresse des Betreuers.
     */
    @Test
    public void changeEmail() {
        accountPage.fillAndSubmitChangeEmailForm(browser, "betreuer@kit.edu");
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage).isAt();
        assertEquals("betreuer@kit.edu", ElipseModel.<Adviser>getById(Adviser.class, id).getEmailAddress());
    }
}
