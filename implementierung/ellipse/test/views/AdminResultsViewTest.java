package views;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import data.GeneralData;
import data.Adviser;
import data.ElipseModel;
import data.Project;
import play.test.TestBrowser;
import data.Allocation;

import java.util.List;
import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminResultsPage;
import views.pages.admin.AdminAllocationPage;

import static org.junit.Assert.*;

public class AdminResultsViewTest extends ViewTest {

    private AdminAllocationPage allocationPage;
    private AdminAccountPage    accountPage;
    private AdminResultsPage    resultsPage;

    private static final String name = "TestAllocation";

    @Before
    @Override
    public void before() {
        super.before();
        allocationPage = browser.createPage(AdminAllocationPage.class);
        accountPage = browser.createPage(AdminAccountPage.class);
        resultsPage = browser.createPage(AdminResultsPage.class);
        TestHelpers.createAdmin();
        login("admin", "adminadmin", accountPage);
        accountPage.gotoMenuEntry(browser, 3);
    }

    @Ignore
    @Test
    public void duplicateAllocation() {
        TestHelpers.setStateToAfterRegistration();
        TestHelpers.createDataSetForAllocation(2, 10, 1, 5);
        allocationPage.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(allocationPage)
                .isAt();
        allocationPage.fillAndSubmitAddAllocationForm(browser, name, 1, 5, 5,
                50);
        int id = 0;
        for (Allocation a : Allocation.getAllocations()) {
            if (a.getName().equals(name)) {
                id = a.getId();
                break;
            }
        }
        /*
         * while (!resultsPage.isAllocationPresent(browser, id)) {
         * resultsPage.go(); browser.await().atMost(2,
         * TimeUnit.SECONDS).untilPage(resultsPage) .isAt(); }
         */
        resultsPage.go();
        resultsPage.duplicateAllocation(browser, id);
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(resultsPage)
                .isAt();
        int duplicateId = 0;
        for (Allocation a : Allocation.getAllocations()) {
            if (a.getName().equals("cloned" + name)) {
                duplicateId = a.getId();
                break;
            }
        }
        assertTrue(resultsPage.isAllocationPresent(browser, duplicateId));
    }

    private void createAllocation() {

    }
}
