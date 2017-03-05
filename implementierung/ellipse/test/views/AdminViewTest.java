package views;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminAdvisersPage;
import views.pages.admin.AdminAllocationPage;
import views.pages.admin.AdminImExportPage;
import views.pages.admin.AdminProjectEditPage;
import views.pages.admin.AdminProjectsPage;
import views.pages.admin.AdminPropertiesPage;
import views.pages.admin.AdminResultsPage;
import views.pages.admin.AdminStudentEditPage;

/**
 * Diese Klasse enthält Tests für den AdminView.
 */
public class AdminViewTest extends ViewTest {

    private AdminAccountPage     accountPage;
    private AdminAdvisersPage    advisersPage;
    private AdminAllocationPage  allocationPage;
    private AdminImExportPage    imExportPage;
    private AdminProjectEditPage projectEditPage;
    private AdminProjectsPage    projectsPage;
    private AdminPropertiesPage  propertiesPage;
    private AdminResultsPage     resultsPage;
    private AdminStudentEditPage studentEditPage;

    /**
     * Initialisieren der Test-Seiten.
     */
    @Before
    @Override
    public void before() {
        super.before();
        accountPage = browser.createPage(AdminAccountPage.class);
        advisersPage = browser.createPage(AdminAdvisersPage.class);
        allocationPage = browser.createPage(AdminAllocationPage.class);
        imExportPage = browser.createPage(AdminImExportPage.class);
        projectEditPage = browser.createPage(AdminProjectEditPage.class);
        projectsPage = browser.createPage(AdminProjectsPage.class);
        propertiesPage = browser.createPage(AdminPropertiesPage.class);
        resultsPage = browser.createPage(AdminResultsPage.class);
        studentEditPage = browser.createPage(AdminStudentEditPage.class);
        TestHelpers.createAdmin();
        login("admin", "adminadmin", accountPage);
    }

    /**
     * Test für den Wechsel zur Betreuer-Übersicht des Admins.
     */
    @Test
    public void gotoAdminAdviser() {
        accountPage.gotoMenuEntry(browser, 0);
        assertEquals(advisersPage.getUrl(), browser.url());
    }

    /**
     * Test für den Wechsel zur Projekt-Ansicht des Admins.
     */
    @Test
    public void gotoAdminProjects() {
        accountPage.gotoMenuEntry(browser, 1);
        assertEquals(projectsPage.getUrl(), browser.url());
    }

    /**
     * Test für den Wechsel zur Einteilungs-Ansicht des Admins.
     */
    @Test
    public void gotoAdminAllocation() {
        accountPage.gotoMenuEntry(browser, 2);
        assertEquals(allocationPage.getUrl(), browser.url());
    }

    /**
     * Test für den Wechsel zur Einteilungsergebnis-Ansicht des Admins.
     */
    @Test
    public void gotoAdminResults() {
        accountPage.gotoMenuEntry(browser, 3);
        assertEquals(resultsPage.getUrl(), browser.url());
    }

    /**
     * Test für den Wechsel zur Import/Export-Ansicht des Admins.
     */
    @Test
    public void gotoAdminExportImport() {
        accountPage.gotoMenuEntry(browser, 4);
        assertEquals(imExportPage.getUrl(), browser.url());
    }

    /**
     * Test für den Wechsel zur Studenten-Bearbeitungs-Ansicht des Admins.
     */
    @Test
    public void gotoAdminStudentEdit() {
        accountPage.gotoMenuEntry(browser, 5);
        assertEquals(studentEditPage.getUrl(), browser.url());
    }

    /**
     * Test für den Wechsel zur Einstellungs-Ansicht des Admins.
     */
    @Test
    public void gotoAdminProperties() {
        accountPage.gotoMenuEntry(browser, 6);
        assertEquals(propertiesPage.getUrl(), browser.url());
    }

    /**
     * Test für den Wechsel zur Account-Ansicht des Admins.
     */
    @Test
    public void gotoAdminAccount() {
        accountPage.gotoMenuEntry(browser, 7);
        assertEquals(accountPage.getUrl(), browser.url());
    }
}
