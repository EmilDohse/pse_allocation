package views.pages.admin;

import allocation.AllocationQueue;
import allocation.Criterion;
import play.test.TestBrowser;
import views.pages.Page;

public class AdminAllocationPage extends Page {

    @Override
    public String getUrl() {
        return "/admin/allocation";
    }

    public void fillAndSubmitAddAllocationForm(TestBrowser browser, String name,
            int min, int prefered, int max, int... criterionParams) {
        browser.$("#name").first().fill().with(name);
        browser.$("#minTeamSize").first().fill().with(Integer.toString(min));
        browser.$("#preferedTeamSize").first().fill()
                .with(Integer.toString(prefered));
        browser.$("#maxTeamSize").first().fill().with(Integer.toString(max));
        int i = 0;
        for (Criterion c : AllocationQueue.getInstance().getAllocator()
                .getAllCriteria()) {
            browser.$("#" + c.getName()).first().fill()
                    .with(Integer.toString(criterionParams[i]));
            if (i < criterionParams.length - 1) {
                i++;
            }
        }
        browser.$("#add_submit").first().click();
    }

    public boolean isAllocationNotEmpty(TestBrowser browser) {
        return !browser.$("#queue").isEmpty();
    }
}