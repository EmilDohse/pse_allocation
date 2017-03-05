package views;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import data.Student;
import views.pages.student.StudentAccountPage;
import views.pages.student.StudentLearningGroupPage;

import static org.junit.Assert.*;

/**
 * Diese Klasse beinhaltet Tests für die Account-Ansicht des Studenten.
 */
public class StudentAccountViewTest extends ViewTest {

    private StudentAccountPage       accountPage;
    private StudentLearningGroupPage learningGroupPage;

    private final static String      newPassword = "asdasdasdas";

    /**
     * Initialisierung der Testdaten.
     */
    @Before
    @Override
    public void before() {
        super.before();
        TestHelpers.setStateToRegistration();
        TestHelpers.createStudent(9123129, "asdasdasdkl");
        accountPage = browser.createPage(StudentAccountPage.class);
        learningGroupPage = browser.createPage(StudentLearningGroupPage.class);
        login("9123129", "asdasdasdkl", learningGroupPage);
        accountPage.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage).isAt();
    }

    /**
     * Test für das Ändern des Passworts.
     */
    @Test
    public void changePassword() {
        accountPage.fillAndSubmitChangePwForm(browser, "asdasdasdkl", newPassword);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage).isAt();
        logout();
        assertTrue(login("9123129", newPassword, learningGroupPage));
    }

    /**
     * Test für das Ändern der E-Mail-Adresse.
     */
    @Test
    public void changeEmail() {
        accountPage.fillAndSubmitChangeEmailForm(browser, "betreuer@kit.edu");
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage).isAt();
        assertEquals("betreuer@kit.edu", Student.getStudent(9123129).getEmailAddress());
    }

    /**
     * Test für das erneute verifizieren der E-Mail.
     */
    @Test
    public void reverifyEmail() {
        accountPage.clickReverify(browser);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(accountPage).isAt();
        accountPage.isAt();
    }
}
