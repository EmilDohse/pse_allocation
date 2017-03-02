package views.pages.adviser;

import static org.junit.Assert.assertTrue;

import play.test.TestBrowser;
import views.pages.Page;

import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

public class AdviserProjectsPage extends Page {

    @Override
    public String getUrl() {
        return "/adviser";
    }

    @Override
    public void isAt() {
        assertTrue(url().startsWith(getUrl()));
        assertTrue(!url().contains("/adviser/account"));
    }

    public void createProject(TestBrowser browser) {
        browser.$("#addProject_button").first().click();
    }

    public void fillAndSubmitEditProjectForm(TestBrowser browser, String name,
            String url, String institute, String description, int numOfTeams,
            int min, int max, int... advisers) {
        browser.$("#name").first().fill().with(name);
        browser.$("#url").first().fill().with(url);
        browser.$("#institute").first().fill().with(institute);
        browser.$("#description").first().fill().with(description);
        browser.$("#teamCount").first().fill()
                .with(Integer.toString(numOfTeams));
        browser.$("#minSize").first().fill().with(Integer.toString(min));
        browser.$("#maxSize").first().fill().with(Integer.toString(max));
        FillSelectConstructor select = new FillSelectConstructor(
                "#adviser-selection", FluentThread.get().getDriver());
        for (Integer adviser : advisers) {
            select.withValue(Integer.toString(adviser));
        }
        browser.$("#edit_submit").first().click();
    }

    public void join(TestBrowser browser) {
        browser.$("#joinProject").first().click();
    }

    public void leave(TestBrowser browser) {
        browser.$("#leaveProject").first().click();
    }

    public void remove(TestBrowser browser) {
        browser.$("#removeProject").first().click();
    }

    public boolean isOnEditPage(TestBrowser browser) {
        return browser.$("form").size() > 1;
    }
}
