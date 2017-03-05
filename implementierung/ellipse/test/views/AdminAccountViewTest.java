package views;

import org.junit.Before;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminProjectsPage;
import views.pages.index.IndexInformationPage;

import static org.junit.Assert.*;

/**
 * Diese Klasse beinhaltet Tests für die Account-Ansicht des Admins.
 */
public class AdminAccountViewTest extends ViewTest {

    private AdminAccountPage    accountPage;
    private AdminProjectsPage   projectPage;

    private final static String newPassword = "asdasdasdas";

    /**
     * Initialisierung der Testdaten.
     */
    @Before
    @Override
    public void before() {
        super.before();
        accountPage = browser.createPage(AdminAccountPage.class);
        indexPage = browser.createPage(IndexInformationPage.class);
        projectPage = browser.createPage(AdminProjectsPage.class);
        TestHelpers.createAdmin();
        login("admin", "adminadmin", accountPage);
        accountPage.gotoMenuEntry(browser, 7);
    }

    /**
     * Test für das Ändern des Passworts.
     */
    @Test
    public void changePassword() {
        accountPage.fillAndSubmitChangePwForm(browser, "adminadmin", newPassword);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage).isAt();
        logout();
        assertTrue(login("admin", newPassword, projectPage));
    }
}
