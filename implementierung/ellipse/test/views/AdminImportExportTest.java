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

/**
 * Diese Klasse beinhaltet Tests für das Importieren und Exportieren durch den
 * Admin.
 */
public class AdminImportExportTest extends ViewTest {

    private AdminAccountPage    accountPage;
    private AdminImExportPage   imexportPage;

    private static final String name = "TestAllocation";

    /**
     * Initialisieren der Testdaten.
     */
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

    /**
     * Test für das Importieren einer SPO.
     */
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

    /**
     * Test für das Importieren einer SPO aus einer leeren Datei.
     */
    @Test
    public void importSpoNoFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importSpo(browser, "");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    /**
     * Test für das Importieren einer SPO aus einer falschen Datei.
     */
    @Test
    public void importSpoWrongFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importSpo(browser, "importProjects.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    /**
     * Test für das Importieren von Projekten.
     */
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

    /**
     * Test für das Importieren von Projekten aus einer leeren Datei.
     */
    @Test
    public void importProjectsNoFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importProjects(browser, "");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    /**
     * Test für das Importieren von Projekten aus einer falsch formatierten
     * Datei.
     */
    @Test
    public void importProjectsWrongFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importProjects(browser, "importSpo.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    /**
     * Test für das Importieren von Studenten.
     */
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

    /**
     * Test für das Importieren einer Studentenliste aus einer leeren Datei.
     */
    @Test
    public void importStudentsNoFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importStudents(browser, "");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    /**
     * Test für das Importieren einer Studentenliste aus einer falsch
     * formatierten Datei.
     */
    @Test
    public void importStudentsWrongFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importStudents(browser, "importSpo.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    /**
     * Test für das Importieren einer Einteilung.
     */
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

    /**
     * Test für das Importieren einer Einteilung aus einer leeren Datei.
     */
    @Test
    public void importAllocationNoFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importAllocation(browser, "");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    /**
     * Test für das Importieren einer Einteilung aus einer falsch formatierten
     * Datei.
     */
    @Test
    public void importAllocationWrongFile() {
        TestHelpers.setStateToBeforeRegistration();
        imexportPage.importAllocation(browser, "importSpo.csv");
        browser.await().atMost(10, TimeUnit.SECONDS).untilPage(imexportPage)
                .isAt();
        assertTrue(imexportPage.showsError(browser));
    }

    /**
     * Test für das Exportieren einer SPO.
     */
    @Test
    public void exportSpo() {
        TestHelpers.setStateToBeforeRegistration();
        int id = TestHelpers.createSpo("SPO 2008");
        imexportPage.go();
        imexportPage.exportSpo(browser, id);
        assertTrue(browser.pageSource().contains("SPO 2008"));
    }

    /**
     * Test für das Exportieren eines Projectes.
     */
    @Test
    public void exportProjects() {
        TestHelpers.setStateToBeforeRegistration();
        TestHelpers.createProject("TestProject");
        imexportPage.go();
        imexportPage.exportProjects(browser);
        assertTrue(browser.pageSource().contains("TestProject"));
    }

    /**
     * Test für das Exportieren von Studenten.
     */
    @Test
    public void exportStudents() {
        TestHelpers.setStateToBeforeRegistration();
        TestHelpers.createStudent(123121, "asdasdas");
        imexportPage.go();
        imexportPage.exportStudents(browser);
        assertTrue(browser.pageSource().contains("123121"));
    }

    /**
     * Test für das Exportieren einer Einteilung.
     */
    @Test
    public void exportAllocation() {
        TestHelpers.setStateToBeforeRegistration();
        int id = TestHelpers.createAllocation("Allocation 2008");
        imexportPage.go();
        imexportPage.exportAllocation(browser, id);
        assertTrue(browser.pageSource().contains("Mitglieder"));
    }

    /**
     * Test für das Exportieren der Noten.
     */
    @Test
    public void exportGrades() {
        TestHelpers.createStudentWithGrades(1231233);
        imexportPage.go();
        imexportPage.exportGrades(browser);
        assertTrue(browser.pageSource().contains("1231233"));
    }
}
