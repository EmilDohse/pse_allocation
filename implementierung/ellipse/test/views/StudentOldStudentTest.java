package views;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import views.pages.student.OldStudentPage;
import views.pages.student.StudentAccountPage;

/**
 * Diese Klasse beinhaltet Tests für den Fall, dass ein Student eines älteren
 * Semesters sich erneut anmeldet.
 */
public class StudentOldStudentTest extends ViewTest {

    private OldStudentPage studentPage;

    /**
     * Initialisierung der Testdaten.
     */
    @Before
    @Override
    public void before() {
        super.before();
        studentPage = browser.createPage(OldStudentPage.class);
        TestHelpers.setStateToRegistration();
    }

    /**
     * Test ob die Form zum Aktualisieren der Daten gezeigt wird.
     */
    @Test
    public void isDataChangeFormShown() {
        TestHelpers.createOldStudent(9123129, "asdasdasdkl");
        login("9123129", "asdasdasdkl", studentPage);
    }
}
