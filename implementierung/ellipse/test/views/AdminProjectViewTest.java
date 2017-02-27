package views;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import data.GeneralData;
import data.Adviser;
import java.util.List;
import security.BlowfishPasswordEncoder;
import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminProjectEditPage;
import views.pages.admin.AdminProjectsPage;

import static org.junit.Assert.*;

public class AdminProjectViewTest extends ViewTest {

    private AdminProjectsPage    projectPage;
    private AdminAccountPage     accountPage;
    private AdminProjectEditPage projectEditPage;

    private static final String  name = "TestProject";

    @Before
    @Override
    public void before() {
        super.before();
        projectPage = browser.createPage(AdminProjectsPage.class);
        accountPage = browser.createPage(AdminAccountPage.class);
        projectEditPage = browser.createPage(AdminProjectEditPage.class);
        TestHelpers.createAdmin();
        login("admin", "adminadmin", accountPage);
        accountPage.gotoMenuEntry(browser, 1);
    }

    @Test
    public void createProject() {
        TestHelpers.setStateToBeforeRegistration();
        projectPage.fillAndSubmitAddProjectForm(browser, name);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(projectEditPage)
                .isAt();
        int id = GeneralData.loadInstance().getCurrentSemester().getProjects()
                .get(0).getId();
        assertTrue(projectEditPage.isEditPageFromProject(browser, id));
    }

    @Test
    public void gotoEditPage() {
        TestHelpers.setStateToBeforeRegistration();
        int id = TestHelpers.createProject(name);
        projectPage.go();
        projectPage.gotoEditProjectPage(browser, id);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(projectEditPage)
                .isAt();
        assertTrue(projectEditPage.isEditPageFromProject(browser, id));
    }

    @Test
    public void removeProject() {
        TestHelpers.setStateToBeforeRegistration();
        int id = TestHelpers.createProject(name);
        browser.goTo(projectEditPage.getUrl() + id);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(projectEditPage)
                .isAt();
        projectEditPage.removeProject(browser);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(projectPage)
                .isAt();
        assertTrue(!projectPage.isProjectShown(browser, id));
    }
}