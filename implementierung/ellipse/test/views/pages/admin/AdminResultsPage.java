package views.pages.admin;

import play.test.TestBrowser;
import views.pages.Page;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NoSuchElementException;

public class AdminResultsPage extends Page {

    @Override
    public String getUrl() {
        return "/admin/results";
    }

    public boolean isAllocationPresent(TestBrowser browser, int id) {
        try {
            return !browser.$("#allocation-" + id).isEmpty();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void waitUntilAllocationIsPresent(TestBrowser browser) {
        while (!browser.$("#noAllocationYet").isEmpty()) {
            this.go();
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage(this).isAt();
        }
        this.go();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(this).isAt();
    }

    public void duplicateAllocation(TestBrowser browser, int id) {
        System.out.println(browser.$("#allocation-tabs").first().html());
        browser.$("#allocation-tab-" + id).first().click();
        browser.$("#duplicate_submit_" + id).first().click();
    }
}