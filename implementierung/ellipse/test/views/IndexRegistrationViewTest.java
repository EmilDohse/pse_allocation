package views;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import data.GeneralData;
import data.Student;
import views.pages.index.IndexInformationPage;
import views.pages.index.IndexRegistrationPage;

import java.util.List;

import static org.junit.Assert.*;

public class IndexRegistrationViewTest extends ViewTest {

    private IndexRegistrationPage registrationPage;
    private IndexInformationPage  infoPage;

    private static final String   firstName = "TestFirstName";
    private static final String   lastName  = "TestLastName";
    private static final String   email     = "email@email.com";
    private static final String   password  = "TestPassowrd";
    private static final int      matrnr    = 1625432;
    private static final int      semester  = 3;
    private static final String   spoName   = "TestSPO";

    @Before
    @Override
    public void before() {
        super.before();
        registrationPage = browser.createPage(IndexRegistrationPage.class);
        infoPage = browser.createPage(IndexInformationPage.class);
    }

    @Test
    public void registerStudent() {
        TestHelpers.setStateToRegistration();
        int spoId = TestHelpers.createSpo(spoName);
        registrationPage.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(registrationPage)
                .isAt();
        registrationPage.fillAndSubmitRegistrtaionForm(browser, firstName,
                lastName, matrnr, email, password, semester, spoId);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(infoPage).isAt();
        assertTrue(Student.getStudent(matrnr).getEmailAddress().equals(email));
    }
}
