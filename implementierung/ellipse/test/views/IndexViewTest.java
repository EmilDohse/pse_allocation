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

/**
 * Diese Klasse beinhaltet Tests für die IndexSeite.
 */
public class IndexViewTest extends ViewTest {

    private static final String  TEST_INFO_TEXT = "Test Info String.";
    private IndexInformationPage infoPage;

    /**
     * Initialisierung der Testseite.
     */
    @Before
    @Override
    public void before() {
        super.before();
        infoPage = browser.createPage(IndexInformationPage.class);
    }

    /**
     * Test für den Inhalt der Index-Seite.
     */
    @Test
    public void testIndexPage() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.setInfoText(TEST_INFO_TEXT);
        });
        browser.goTo(infoPage);
        assertEquals(TEST_INFO_TEXT, infoPage.getInfoText(browser));
    }

    /**
     * Test für den Wechsel zur Passwort-zurücksetzen Seite.
     */
    @Test
    public void gotoResetPasswordPage() {
        browser.goTo(infoPage);
        infoPage.gotoPasswordResetPage(browser);
        assertEquals("/reset", browser.url());
    }

    /**
     * Test für den Wechsel zur Infoseite.
     */
    @Test
    public void gotoGeneralInformationPage() {
        browser.goTo(infoPage);
        infoPage.gotoMenuEntry(browser, 0);
        assertEquals(infoPage.getUrl(), browser.url());
    }

    /**
     * Test für den Seitenwechsel vor dem Start der Registrierung.
     */
    @Test
    public void gotoRegisterPageBeforeRegStart() {
        TestHelpers.setStateToBeforeRegistration();
        browser.goTo(infoPage);
        infoPage.gotoMenuEntry(browser, 1);
        assertEquals(infoPage.getUrl(), browser.url());
    }

    /**
     * Test für den Seitenwechsel während der Registrierung.
     */
    @Test
    public void gotoRegisterPageDuringReg() {
        TestHelpers.setStateToRegistration();
        browser.goTo(infoPage);
        infoPage.gotoMenuEntry(browser, 1);
        assertEquals("/register", browser.url());
    }

    /**
     * Test für den Seitenwechsel nach der Registrierung.
     */
    @Test
    public void gotoRegisterPageAfterRegEnd() {
        TestHelpers.setStateToAfterRegistration();
        browser.goTo(infoPage);
        infoPage.gotoMenuEntry(browser, 1);
        assertEquals(infoPage.getUrl(), browser.url());
    }
}
