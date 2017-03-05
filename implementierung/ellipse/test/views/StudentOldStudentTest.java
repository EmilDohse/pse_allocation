package views;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import views.pages.student.OldStudentPage;
import views.pages.student.StudentAccountPage;

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

    @Test
    public void isDataChangeFormShown() {
        TestHelpers.createOldStudent(9123129, "asdasdasdkl");
        login("9123129", "asdasdasdkl", studentPage);
    }
}
