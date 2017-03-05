package views;

import org.junit.Before;
import org.junit.Test;

import views.pages.student.StudentLearningGroupPage;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Diese Klasse beinhaltet Test für die Lerngruppenansicht des Studenten.
 */
public class StudentLearningGroupViewTest extends ViewTest {

    private StudentLearningGroupPage learningGroupPage;

    /**
     * Initialisieren der Testdaten.
     */
    @Before
    @Override
    public void before() {
        super.before();
        TestHelpers.setStateToRegistration();
        TestHelpers.createStudent(9123129, "asdasdasdkl");
        learningGroupPage = browser.createPage(StudentLearningGroupPage.class);
        login("9123129", "asdasdasdkl", learningGroupPage);
    }

    /**
     * ´Test ob die Form der Lerngruppe auf der Seite vorhanden ist.
     */
    @Test
    public void isLearningGroupFormPresent() {
        assertTrue(learningGroupPage.isCreateLearningGroupFormPresent(browser));

    }

    /**
     * Test für das Erstellen einer Lerngruppe.
     */
    @Test
    public void createLearningGroup() {
        assertTrue(learningGroupPage.isCreateLearningGroupFormPresent(browser));
        learningGroupPage.fillAndSubmitCreateLGForm(browser, "TestLerngName", "testPassword");
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(learningGroupPage).isAt();
        assertTrue(!learningGroupPage.isCreateLearningGroupFormPresent(browser));
    }

    /**
     * Test für das Beitreten einer Lerngruppe.
     */
    @Test
    public void joinLearningGroup() {
        assertTrue(learningGroupPage.isCreateLearningGroupFormPresent(browser));
        TestHelpers.createLearningGroup("TestLerngName", "testPassword");
        learningGroupPage.fillAndSubmitJoinLGForm(browser, "TestLerngName", "testPassword");
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(learningGroupPage).isAt();
        assertTrue(!learningGroupPage.isCreateLearningGroupFormPresent(browser));
    }

    /**
     * Test für das Verlassen einer Lerngruppe.
     */
    @Test
    public void leaveLearningGroup() {
        TestHelpers.createAndJoinLearningGroup(9123129);
        learningGroupPage.go();
        assertTrue(!learningGroupPage.isCreateLearningGroupFormPresent(browser));
        learningGroupPage.leaveLearningGroup(browser);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(learningGroupPage).isAt();
        assertTrue(learningGroupPage.isCreateLearningGroupFormPresent(browser));
    }
}
