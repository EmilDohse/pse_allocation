package views;

import org.junit.Before;
import org.junit.Test;
import data.Adviser;
import security.BlowfishPasswordEncoder;
import java.util.List;
import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminAdvisersPage;

import static org.junit.Assert.*;

/**
 * Diese Klasse beinhaltet Test für die Betreuer-Ansicht des Admins.
 */
public class AdminAdviserViewTest extends ViewTest {

    private AdminAdvisersPage   advisersPage;
    private AdminAccountPage    accountPage;

    private static final String adviserFirstName = "TestFirstName";
    private static final String adviserLastName  = "TestLastName";
    private static final String adviserEmail     = "email@avbncv.de";
    private static final String adviserPassword  = "asdass4654";

    /**
     * Initialisierung der Testdaten.
     */
    @Before
    @Override
    public void before() {
        super.before();
        advisersPage = browser.createPage(AdminAdvisersPage.class);
        accountPage = browser.createPage(AdminAccountPage.class);
        TestHelpers.createAdmin();
        login("admin", "adminadmin", accountPage);
        accountPage.gotoMenuEntry(browser, 0);
    }

    /**
     * Test für das Erstellen eines Betreuers.
     */
    @Test
    public void createAdviser() {
        advisersPage.fillAndSubmitAddAdviserForm(browser, adviserFirstName, adviserLastName, adviserEmail,
                adviserPassword);
        List<Adviser> advisers = Adviser.getAdvisers();
        assertEquals(1, advisers.size());
        Adviser adviser = advisers.get(0);
        assertEquals(adviserFirstName, adviser.getFirstName());
        assertEquals(adviserLastName, adviser.getLastName());
        assertEquals(adviserEmail, adviser.getEmailAddress());
        assertTrue(new BlowfishPasswordEncoder().matches(adviserPassword, adviser.getPassword()));
        assertTrue(advisersPage.isAdviserShown(browser, adviser.getId()));
    }

    /**
     * Test für das Entfernen eines Betreuers.
     */
    @Test
    public void removeAdviser() {
        int id = TestHelpers.createAdviser(adviserFirstName, adviserLastName, adviserEmail, adviserPassword);
        System.out.println(id);
        advisersPage.go();
        advisersPage.removeAdviser(browser, id);
        List<Adviser> advisers = Adviser.getAdvisers();
        assertEquals(0, advisers.size());
        assertTrue(!advisersPage.isAdviserShown(browser, id));
    }
}
