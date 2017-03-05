package views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import data.Allocation;
import data.GeneralData;
import data.SPO;
import data.Semester;
import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminImExportPage;

import java.util.concurrent.TimeUnit;

public class AdminImportExportTest extends ViewTest {

    private AdminAccountPage    accountPage;
    private AdminImExportPage   imexportPage;

    private static final String name = "TestAllocation";

    @Before
    @Override
    public void before() {
        super.before();
        accountPage = browser.createPage(AdminAccountPage.class);
        imexportPage = browser.createPage(AdminImExportPage.class);
        TestHelpers.createAdmin();
        login("admin", "adminadmin", accountPage);
        accountPage.gotoMenuEntry(browser, 4);
    }

    @Test
    public void importSpo() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importSpo(browser, "importSpo.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertNotNull(SPO.getSPO("2008"));
        assertFalse(SPO.getSPO("2008").getAdditionalAchievements().isEmpty());
        assertFalse(SPO.getSPO("2008").getNecessaryAchievements().isEmpty());
    }

    @Test
    public void importSpoNoFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importSpo(browser, "");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    @Test
    public void importSpoWrongFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importSpo(browser, "importProjects.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    @Test
    public void importProjects() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importProjects(browser, "importProjects.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        Semester s = GeneralData.loadInstance().getCurrentSemester();
        assertFalse(s.getProjects().isEmpty());
        assertEquals(23, s.getProjects().size());
    }

    @Test
    public void importProjectsNoFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importProjects(browser, "");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    @Test
    public void importProjectsWrongFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importProjects(browser, "importSpo.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    @Test
    public void importStudents() {
        TestHelpers.setStateToBeforeRegistration();
        // SPO und Projekte für Studentenimport benötigt
        imexportPage.importSpo(browser, "importSpo.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        imexportPage.importProjects(browser, "importProjects.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        imexportPage.importStudents(browser, "importStudents.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        Semester s = GeneralData.loadInstance().getCurrentSemester();
        assertFalse(s.getStudents().isEmpty());
        assertFalse(s.getLearningGroups().isEmpty());
    }

    @Test
    public void importStudentsNoFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importStudents(browser, "");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    @Test
    public void importStudentsWrongFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importStudents(browser, "importSpo.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    @Test
    public void importAllocation() {
        TestHelpers.setStateToBeforeRegistration();
        // SPO und Projekte für Studentenimport benötigt
        imexportPage.importSpo(browser, "importSpo.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        imexportPage.importProjects(browser, "importProjects.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        imexportPage.importStudents(browser, "importStudents.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        imexportPage.importAllocation(browser, "importAllocation.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(Allocation.getAllocations().size() > 0);
    }

    @Test
    public void importAllocationNoFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importAllocation(browser, "");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    @Test
    public void importAllocationWrongFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importAllocation(browser, "importSpo.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

}
