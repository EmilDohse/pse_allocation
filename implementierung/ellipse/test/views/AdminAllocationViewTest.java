package views;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import data.GeneralData;
import data.Adviser;
import data.ElipseModel;
import data.Project;
import data.Allocation;

import java.util.List;
import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminAllocationPage;
import views.pages.admin.AdminResultsPage;

import static org.junit.Assert.*;

public class AdminAllocationViewTest extends ViewTest {

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
        accountPage.gotoMenuEntry(browser, 2);
    }

    @Test
    public void createAllocation() {
        TestHelpers.setStateToAfterRegistration();
        TestHelpers.createDataSetForAllocation();
        allocationPage.fillAndSubmitAddAllocationForm(browser, name, 1, 2, 2,
                50);
        boolean allocationExists = false;
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(allocationPage)
                .isAt();
        allocationExists = allocationPage.isAllocationNotEmpty(browser);
        if (!allocationExists) {
            resultsPage.go();
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage(resultsPage)
                    .isAt();
            int id = 0;
            for (Allocation a : Allocation.getAllocations()) {
                if (a.getName().equals(name)) {
                    id = a.getId();
                    break;
                }
            }
            allocationExists = resultsPage.isAllocationPresent(browser, id);
        }
        assertTrue(allocationExists);
    }

}
