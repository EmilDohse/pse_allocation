package views;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import data.Student;
import views.pages.student.StudentLearningGroupPage;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class StudentLearningGroupViewTest extends ViewTest {

    private StudentLearningGroupPage learningGroupPage;

    @Before
    @Override
    public void before() {
        super.before();
        TestHelpers.setStateToRegistration();
        TestHelpers.createStudent(9123129, "asdasdasdkl");
        learningGroupPage = browser.createPage(StudentLearningGroupPage.class);
        login("9123129", "asdasdasdkl", learningGroupPage);
    }

    @Test
    public void isLearningGroupFormPresent() {
        assertTrue(learningGroupPage.isCreateLearningGroupFormPresent(browser));

    }

    @Test
    public void createLearningGroup() {
        assertTrue(learningGroupPage.isCreateLearningGroupFormPresent(browser));
        learningGroupPage.fillAndSubmitCreateLGForm(browser, "TestLerngName",
                "testPassword");
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(learningGroupPage)
                .isAt();
        assertTrue(
                !learningGroupPage.isCreateLearningGroupFormPresent(browser));
    }

    @Test
    public void joinLearningGroup() {
        assertTrue(learningGroupPage.isCreateLearningGroupFormPresent(browser));
        TestHelpers.createLearningGroup("TestLerngName", "testPassword");
        learningGroupPage.fillAndSubmitJoinLGForm(browser, "TestLerngName",
                "testPassword");
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(learningGroupPage)
                .isAt();
        assertTrue(
                !learningGroupPage.isCreateLearningGroupFormPresent(browser));
    }

    @Test
    public void leaveLearningGroup() {
        TestHelpers.createAndJoinLearningGroup(9123129);
        learningGroupPage.go();
        assertTrue(
                !learningGroupPage.isCreateLearningGroupFormPresent(browser));
        learningGroupPage.leaveLearningGroup(browser);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(learningGroupPage)
                .isAt();
        assertTrue(learningGroupPage.isCreateLearningGroupFormPresent(browser));
    }
}
