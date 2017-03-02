package views;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import data.GeneralData;
import data.Student;

import java.util.List;
import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminProjectsPage;
import views.pages.index.IndexInformationPage;

import static org.junit.Assert.*;

public class AdminAccountViewTest extends ViewTest {

    private AdminAccountPage     accountPage;
    private IndexInformationPage indexPage;
    private AdminProjectsPage    projectPage;

    private final static String  newPassword = "asdasdasdas";

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

    @Test
    public void changePassword() {
        accountPage.fillAndSubmitChangePwForm(browser, "adminadmin",
                newPassword);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage)
                .isAt();
        logout();
        assertTrue(login("admin", newPassword, projectPage));
    }
}
