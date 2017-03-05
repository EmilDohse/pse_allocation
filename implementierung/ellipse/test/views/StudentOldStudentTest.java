package views;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import views.pages.student.OldStudentPage;
import views.pages.student.StudentAccountPage;
import views.pages.student.StudentLearningGroupPage;

import java.util.concurrent.TimeUnit;

/**
 * Diese Klasse beinhaltet Tests für den Fall, dass ein Student eines älteren
 * Semesters sich erneut anmeldet.
 */
public class StudentOldStudentTest extends ViewTest {

    private OldStudentPage           studentPage;
    private StudentLearningGroupPage learningGroupPage;

    private static final String      password = "TestPassowrd";
    private static final int         matrnr   = 1625432;
    private static final int         semester = 3;
    private static final String      spoName  = "TestSPO";

    /**
     * Initialisierung der Testdaten.
     */
    @Before
    @Override
    public void before() {
        super.before();
        studentPage = browser.createPage(OldStudentPage.class);
        learningGroupPage = browser.createPage(StudentLearningGroupPage.class);
        TestHelpers.setStateToRegistration();
    }

    /**
     * Test ob die Form zum Aktualisieren der Daten gezeigt wird.
     */
    @Test
    public void isDataChangeFormShown() {
        TestHelpers.createOldStudent(matrnr, "asdasdasdkl");
        login(Integer.toString(matrnr), "asdasdasdkl", studentPage);
    }

    @Test
    public void submitDataChangeForm() {
        TestHelpers.createOldStudent(matrnr, "asdasdasdkl");
        int spoId = TestHelpers.createSpo(spoName);
        login(Integer.toString(matrnr), "asdasdasdkl", studentPage);
        studentPage.fillAndSubmitOldDataForm(browser, semester, spoId);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(learningGroupPage)
                .isAt();
    }
}
