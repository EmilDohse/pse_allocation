package views;

import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Date;

import org.junit.Test;

import data.Allocation;
import data.GeneralData;
import data.Semester;
import deadline.StateStorage;
import play.test.WithBrowser;

public class IndexViewTest extends ViewTest {

    private static final String TEST_INFO_TEXT = "Test Info String.";

    @Test
    public void testIndexPage() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.setInfoText(TEST_INFO_TEXT);
        });
        browser.goTo("/");
        assertEquals(TEST_INFO_TEXT,
                browser.$("#generalInformation").getText());
    }

    @Test
    public void gotoResetPasswordPage() {
        browser.goTo("/");
        browser.$("#pwReset").click();
        assertEquals("/reset", browser.url());
    }

    @Test
    public void gotoGeneralInformationPage() {
        browser.goTo("/");
        browser.$(".nav-sidebar > li > a").first().click();
        assertEquals("/", browser.url());
    }

    @Test
    public void gotoRegisterPageBeforeRegStart() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Instant i = Instant.now();
        semester.doTransaction(() -> {
            semester.setRegistrationStart(Date.from(i.plusSeconds(30)));
            semester.setRegistrationEnd(Date.from(i.plusSeconds(40)));
        });
        StateStorage.getInstance().initStateChanging(
                semester.getRegistrationStart(), semester.getRegistrationEnd());
        try {
            Thread.sleep(100); // TODO: Besser??? Warten auf StateChange
        } catch (InterruptedException e) {
        }
        System.out.println(
                "Before:" + StateStorage.getInstance().getCurrentState());
        browser.goTo("/");
        browser.$(".nav-sidebar > li > a").get(1).click();
        assertEquals("/", browser.url());
    }

    @Test
    public void gotoRegisterPageDuringReg() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Instant i = Instant.now();
        semester.doTransaction(() -> {
            semester.setRegistrationStart(Date.from(i.minusSeconds(30)));
            semester.setRegistrationEnd(Date.from(i.plusSeconds(40)));
        });
        StateStorage.getInstance().initStateChanging(
                semester.getRegistrationStart(), semester.getRegistrationEnd());
        try {
            Thread.sleep(100); // TODO: Besser??? Warten auf StateChange
        } catch (InterruptedException e) {
        }
        System.out.println(
                "While:" + StateStorage.getInstance().getCurrentState());
        browser.goTo("/");
        browser.$(".nav-sidebar > li > a").get(1).click();
        assertEquals("/register", browser.url());
    }

    @Test
    public void gotoRegisterPageAfterRegEnd() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Instant i = Instant.now();
        semester.doTransaction(() -> {
            semester.setRegistrationStart(Date.from(i.minusSeconds(30)));
            semester.setRegistrationEnd(Date.from(i.minusSeconds(40)));
        });
        StateStorage.getInstance().initStateChanging(
                semester.getRegistrationStart(), semester.getRegistrationEnd());
        try {
            Thread.sleep(100); // TODO: Besser??? Warten auf StateChange
        } catch (InterruptedException e) {
        }
        System.out.println(
                "After:" + StateStorage.getInstance().getCurrentState());
        browser.goTo("/");
        browser.$(".nav-sidebar > li > a").get(1).click();
        assertEquals("/", browser.url());
    }
}
