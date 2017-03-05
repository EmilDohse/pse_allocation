package views;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import data.GeneralData;
import data.ElipseModel;
import data.Project;
import views.pages.adviser.AdviserProjectsPage;

import static org.junit.Assert.*;

/**
 * Diese Klasse beinhaltet Tests für die Ansicht des Betreuers.
 */
public class AdviserViewTest extends ViewTest {

    private AdviserProjectsPage adviserPage;

    private static final String advEmail    = "adviser@kit.edu";
    private static final String advPassword = "adviserPassword";

    private static final String name        = "TestProject";
    private static final String url         = "http://www.google.de";
    private static final String description = "TestDescription";
    private static final String institute   = "Test Institute";
    private static final int    min         = 4;
    private static final int    max         = 8;
    private static final int    numOfTeams  = 2;

    private int                 advId;

    /**
     * Initialisierung der Testdaten.
     */
    @Before
    @Override
    public void before() {
        super.before();
        adviserPage = browser.createPage(AdviserProjectsPage.class);
        advId = TestHelpers.createAdviser(advEmail, advPassword);
        login(advEmail, advPassword, adviserPage);
    }

    /**
     * Test für das Erstellen eines Projektes.
     */
    @Test
    public void createProject() {
        TestHelpers.setStateToBeforeRegistration();
        adviserPage.createProject(browser);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(adviserPage).isAt();
        int id = GeneralData.loadInstance().getCurrentSemester().getProjects().get(0).getId();
        assertTrue(browser.url().equals(adviserPage.getUrl() + "/projects/" + id));
    }

    /**
     * Test für das Beitreten des Betreuers zu einem Projekt.
     */
    @Test
    public void joinProject() {
        TestHelpers.setStateToBeforeRegistration();
        int id = TestHelpers.createProject(name);
        browser.goTo(adviserPage.getUrl() + "/projects/" + id);
        adviserPage.join(browser);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(adviserPage).isAt();
        assertTrue(adviserPage.isOnEditPage(browser));
    }

    /**
     * Test für das Editieren eines Projektes.
     */
    @Test
    public void editProject() {
        TestHelpers.setStateToBeforeRegistration();
        int id = TestHelpers.createAndJoinProject(name, advId);
        browser.goTo(adviserPage.getUrl() + "/projects/" + id);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(adviserPage).isAt();
        assertTrue(adviserPage.isOnEditPage(browser));
        adviserPage.fillAndSubmitEditProjectForm(browser, name, url, institute, description, numOfTeams, min, max,
                advId);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(adviserPage).isAt();
        Project p = ElipseModel.getById(Project.class, id);
        assertEquals(name, p.getName());
        assertEquals(url, p.getProjectURL());
        assertEquals(institute, p.getInstitute());
        assertEquals(description, p.getProjectInfo());
        assertEquals(numOfTeams, p.getNumberOfTeams());
        assertEquals(min, p.getMinTeamSize());
        assertEquals(max, p.getMaxTeamSize());
        assertEquals(advId, p.getAdvisers().get(0).getId());

    }

    /**
     * Test für das Verlassen (nicht länger betreuen) eines Projektes.
     */
    @Test
    public void leaveProject() {
        TestHelpers.setStateToBeforeRegistration();
        int id = TestHelpers.createAndJoinProject(name, advId);
        browser.goTo(adviserPage.getUrl() + "/projects/" + id);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(adviserPage).isAt();
        assertTrue(adviserPage.isOnEditPage(browser));
        adviserPage.leave(browser);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(adviserPage).isAt();
        assertTrue(!adviserPage.isOnEditPage(browser));
    }

    /**
     * Test für das Entfernen eines Projektes.
     */
    @Test
    public void removeProject() {
        TestHelpers.setStateToBeforeRegistration();
        int id = TestHelpers.createAndJoinProject(name, advId);
        browser.goTo(adviserPage.getUrl() + "/projects/" + id);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(adviserPage).isAt();
        assertTrue(adviserPage.isOnEditPage(browser));
        adviserPage.remove(browser);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(adviserPage).isAt();
        assertNull(ElipseModel.getById(Project.class, id));
    }
}
