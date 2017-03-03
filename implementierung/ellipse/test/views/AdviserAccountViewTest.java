package views;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import data.Adviser;
import data.ElipseModel;
import data.GeneralData;
import data.Student;

import java.util.List;
import views.pages.adviser.AdviserAccountPage;
import views.pages.adviser.AdviserProjectsPage;

import static org.junit.Assert.*;

public class AdviserAccountViewTest extends ViewTest {

    private AdviserAccountPage  accountPage;
    private AdviserProjectsPage projectPage;

    private final static String newPassword = "asdasdasdas";

    private int                 id;

    @Before
    @Override
    public void before() {
        super.before();
        accountPage = browser.createPage(AdviserAccountPage.class);
        projectPage = browser.createPage(AdviserProjectsPage.class);
        id = TestHelpers.createAdviser("betruer@kit.edu", "asdasdasdkl");
        login("betruer@kit.edu", "asdasdasdkl", projectPage);
        accountPage.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage)
                .isAt();
    }

    @Test
    public void changePassword() {
        accountPage.fillAndSubmitChangePwForm(browser, "asdasdasdkl",
                newPassword);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage)
                .isAt();
        logout();
        assertTrue(login("betruer@kit.edu", newPassword, projectPage));
    }

    @Test
    public void changeEmail() {
        accountPage.fillAndSubmitChangeEmailForm(browser, "betreuer@kit.edu");
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage)
                .isAt();
        assertEquals("betreuer@kit.edu", ElipseModel
                .<Adviser>getById(Adviser.class, id).getEmailAddress());
    }
}
