package views;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import data.GeneralData;
import data.Student;

import java.util.List;
import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminStudentEditPage;

import static org.junit.Assert.*;

public class AdminStudentEditViewTest extends ViewTest {

    private AdminAccountPage     accountPage;
    private AdminStudentEditPage studentEditPage;

    private static final String  firstName = "TestFirstName";
    private static final String  lastName  = "TestLastName";
    private static final String  email     = "email@email.com";
    private static final String  password  = "TestPassowrd";
    private static final int     matrnr    = 1625432;
    private static final int     semester  = 3;
    private static final String  spoName   = "TestSPO";

    @Before
    @Override
    public void before() {
        super.before();
        accountPage = browser.createPage(AdminAccountPage.class);
        studentEditPage = browser.createPage(AdminStudentEditPage.class);
        TestHelpers.createAdmin();
        login("admin", "adminadmin", accountPage);
        accountPage.gotoMenuEntry(browser, 5);
    }

    @Test
    public void createStudent() {
        TestHelpers.setStateToRegistration();
        int spoId = TestHelpers.createSpo(spoName);
        studentEditPage.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(studentEditPage)
                .isAt();
        studentEditPage.fillAndSubmitAddStudentForm(browser, firstName,
                lastName, matrnr, email, password, semester, spoId);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(studentEditPage)
                .isAt();
        assertTrue(Student.getStudent(matrnr).getEmailAddress().equals(email));
    }

    @Ignore
    @Test
    public void removeStudent() {
        createStudent();
        studentEditPage.fillAndSubmitRemoveStudentForm(browser, matrnr);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(studentEditPage)
                .isAt();
        assertTrue(Student.getStudent(matrnr) == null);
    }
}
