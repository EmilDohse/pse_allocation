import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import data.GeneralData;
import data.LearningGroup;
import data.Semester;
import data.Student;
import data.Project;
import data.SPO;
import data.ElipseModel;
import views.ViewTest;
import views.TestHelpers;
import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminPropertiesPage;
import views.pages.adviser.AdviserProjectsPage;
import views.pages.index.IndexInformationPage;
import views.pages.index.IndexRegistrationPage;
import views.pages.student.StudentLearningGroupPage;
import views.pages.student.StudentRatingPage;

import static org.junit.Assert.*;

/**
 * Diese Klasse enthält die Globalen Testfälle aus dem Pflichtenheft.
 * 
 * @author daniel
 *
 */
public class GlobalTests extends ViewTest {

    private IndexRegistrationPage    registrationPage;
    private IndexInformationPage     infoPage;
    private StudentLearningGroupPage learningGroupPage;
    private StudentRatingPage        ratingPage;

    private static final String      firstName   = "TestFirstName";
    private static final String      lastName    = "TestLastName";
    private static final String      email       = "email@email.com";
    private static final String      password    = "TestPassowrd";
    private static final int         matrnr      = 1625432;
    private static final int         semester    = 3;
    private static final String      spoName     = "TestSPO";

    private AdviserProjectsPage      adviserPage;

    private static final String      advEmail    = "adviser@kit.edu";
    private static final String      advPassword = "adviserPassword";

    private static final String      name        = "TestProject";
    private static final String      url         = "http://www.google.de";
    private static final String      description = "TestDescription";
    private static final String      institute   = "Test Institute";
    private static final int         min         = 4;
    private static final int         max         = 8;
    private static final int         numOfTeams  = 2;

    private AdminAccountPage         accountPage;
    private AdminPropertiesPage      propertiesPage;

    /**
     * Iinitialisierung der Testdaten.
     */
    @Before
    @Override
    public void before() {
        super.before();
        registrationPage = browser.createPage(IndexRegistrationPage.class);
        infoPage = browser.createPage(IndexInformationPage.class);
        learningGroupPage = browser.createPage(StudentLearningGroupPage.class);
        ratingPage = browser.createPage(StudentRatingPage.class);
        adviserPage = browser.createPage(AdviserProjectsPage.class);
        accountPage = browser.createPage(AdminAccountPage.class);
        propertiesPage = browser.createPage(AdminPropertiesPage.class);
    }

    /**
     * Test für das Studenten-Interface
     */
    @Test
    public void studentRegisterCreateLearningAndRate() {
        TestHelpers.setStateToRegistration();
        int spoId = TestHelpers.createSpo(spoName);
        TestHelpers.createProjects(20);
        registrationPage.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(registrationPage)
                .isAt();
        registrationPage.fillAndSubmitRegistrtaionForm(browser, firstName,
                lastName, matrnr, email, password, semester, spoId);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(infoPage).isAt();
        login(Integer.toString(matrnr), password, learningGroupPage);
        assertTrue(learningGroupPage.isCreateLearningGroupFormPresent(browser));
        learningGroupPage.fillAndSubmitCreateLGForm(browser, "TestLerngName",
                "testPassword");
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(learningGroupPage)
                .isAt();
        assertTrue(
                !learningGroupPage.isCreateLearningGroupFormPresent(browser));
        ratingPage.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(ratingPage)
                .isAt();
        Semester sem = GeneralData.loadInstance().getCurrentSemester();
        ratingPage.fillRateForm(browser, sem.getProjects(), 2);
        Student s = Student.getStudent(matrnr);
        LearningGroup lg = sem.getLearningGroupOf(s);
        sem.getProjects().forEach((p) -> {
            assertEquals(2, lg.getRating(p));
        });
    }

    /**
     * Test für das Studenten-Interface
     */
    @Test
    public void studentRegisterJoinLearningAndRate() {
        TestHelpers.setStateToRegistration();
        int spoId = TestHelpers.createSpo(spoName);
        TestHelpers.createProjects(20);
        registrationPage.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(registrationPage)
                .isAt();
        registrationPage.fillAndSubmitRegistrtaionForm(browser, firstName,
                lastName, matrnr, email, password, semester, spoId);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(infoPage).isAt();
        login(Integer.toString(matrnr), password, learningGroupPage);
        assertTrue(learningGroupPage.isCreateLearningGroupFormPresent(browser));
        TestHelpers.createLearningGroup("TestLerngName", "testPassword");
        learningGroupPage.fillAndSubmitJoinLGForm(browser, "TestLerngName",
                "testPassword");
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(learningGroupPage)
                .isAt();
        assertTrue(
                !learningGroupPage.isCreateLearningGroupFormPresent(browser));
        ratingPage.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(ratingPage)
                .isAt();
        Semester sem = GeneralData.loadInstance().getCurrentSemester();
        ratingPage.fillRateForm(browser, sem.getProjects(), 2);
        Student s = Student.getStudent(matrnr);
        LearningGroup lg = sem.getLearningGroupOf(s);
        sem.getProjects().forEach((p) -> {
            assertEquals(2, lg.getRating(p));
        });
    }

    /**
     * Test um zu überprüfen ob Deadline beachtet wird.
     */
    @Test
    public void studentTryRegisterWhenNotAllowed() {
        TestHelpers.setStateToBeforeRegistration();
        registrationPage.go();
        infoPage.isAt();
    }

    /**
     * Betreuer erstellt und editiert 5 Projekte
     */
    @Test
    public void adviserCreateAndEditProjects() {
        TestHelpers.setStateToBeforeRegistration();
        int advId = TestHelpers.createAdviser(advEmail, advPassword);
        login(advEmail, advPassword, adviserPage);
        IntStream.rangeClosed(1, 5).forEach((num) -> {
            adviserPage.createProject(browser);
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage(adviserPage)
                    .isAt();
            int id = GeneralData.loadInstance().getCurrentSemester()
                    .getProjects().get(num - 1).getId();
            assertTrue(browser.url()
                    .equals(adviserPage.getUrl() + "/projects/" + id));
            assertTrue(adviserPage.isOnEditPage(browser));
            adviserPage.fillAndSubmitEditProjectForm(browser, name + " " + num,
                    url, institute, description, numOfTeams, min, max, advId);
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage(adviserPage)
                    .isAt();
            Project p = ElipseModel.getById(Project.class, id);
            assertEquals(name + " " + num, p.getName());
            assertEquals(url, p.getProjectURL());
            assertEquals(institute, p.getInstitute());
            assertEquals(description, p.getProjectInfo());
            assertEquals(numOfTeams, p.getNumberOfTeams());
            assertEquals(min, p.getMinTeamSize());
            assertEquals(max, p.getMaxTeamSize());
            assertEquals(advId, p.getAdvisers().get(0).getId());
        });
    }

    /**
     * Test für den Admin (Einstellungen ändern) Aufgrund eines Selenium-Bugs
     * nicht testbar jedoch
     */
    @Ignore
    @Test
    public void adminLoginAndEditSemesterAndSPOData() {
        browser.getDriver().maximize_window();
        TestHelpers.setStateToBeforeRegistration();
        TestHelpers.createAdmin();
        login("admin", "adminadmin", accountPage);
        propertiesPage.go();
        IntStream.rangeClosed(1, 5).forEach((num) -> {
            System.out.println(num);
            propertiesPage.addSpo(browser);
            browser.await().atMost(2, TimeUnit.SECONDS)
                    .untilPage(propertiesPage).isAt();
            int id = SPO.getSPOs().get(num - 1).getId();
            propertiesPage.changeSpo(browser, id, "SPO " + num);
            browser.await().atMost(2, TimeUnit.SECONDS)
                    .untilPage(propertiesPage).isAt();
            assertEquals("SPO " + num,
                    ElipseModel.getById(SPO.class, id).getName());
        });
        IntStream.rangeClosed(1, 5).forEach((num) -> {
            propertiesPage.addSemester(browser);
            browser.await().atMost(2, TimeUnit.SECONDS)
                    .untilPage(propertiesPage).isAt();
            int id = Semester.getSemesters().get(num).getId();
            propertiesPage.changeSemester(browser, id, "Semester " + num,
                    "InfoText", 4, 2, 3, 1, 4);
            browser.await().atMost(2, TimeUnit.SECONDS)
                    .untilPage(propertiesPage).isAt();
            assertEquals("Semester " + num,
                    ElipseModel.getById(Semester.class, id).getName());
        });
    }
}
