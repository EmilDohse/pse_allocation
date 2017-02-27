package views;

import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import data.Allocation;
import data.GeneralData;
import data.Semester;
import deadline.StateStorage;
import views.pages.admin.AdminAccountPage;
import views.pages.index.IndexInformationPage;

public class IndexViewTest extends ViewTest {

    private static final String  TEST_INFO_TEXT = "Test Info String.";
    private IndexInformationPage infoPage;

    @Before
    @Override
    public void before() {
        super.before();
        infoPage = browser.createPage(IndexInformationPage.class);
    }

    @Test
    public void testIndexPage() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.setInfoText(TEST_INFO_TEXT);
        });
        browser.goTo(infoPage);
        assertEquals(TEST_INFO_TEXT, infoPage.getInfoText(browser));
    }

    @Test
    public void gotoResetPasswordPage() {
        browser.goTo(infoPage);
        infoPage.gotoPasswordResetPage(browser);
        assertEquals("/reset", browser.url());
    }

    @Test
    public void gotoGeneralInformationPage() {
        browser.goTo(infoPage);
        infoPage.gotoMenuEntry(browser, 0);
        assertEquals(infoPage.getUrl(), browser.url());
    }

    @Test
    public void gotoRegisterPageBeforeRegStart() {
        TestHelpers.setStateToBeforeRegistration();
        browser.goTo(infoPage);
        infoPage.gotoMenuEntry(browser, 1);
        assertEquals(infoPage.getUrl(), browser.url());
    }

    @Test
    public void gotoRegisterPageDuringReg() {
        TestHelpers.setStateToRegistration();
        browser.goTo(infoPage);
        infoPage.gotoMenuEntry(browser, 1);
        assertEquals("/register", browser.url());
    }

    @Test
    public void gotoRegisterPageAfterRegEnd() {
        TestHelpers.setStateToAfterRegistration();
        browser.goTo(infoPage);
        infoPage.gotoMenuEntry(browser, 1);
        assertEquals(infoPage.getUrl(), browser.url());
    }
}
