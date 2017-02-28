package views.pages.admin;

import static org.junit.Assert.assertTrue;

import data.Adviser;
import play.test.TestBrowser;
import views.pages.Page;

import org.fluentlenium.core.action.FillSelectConstructor;
import org.fluentlenium.core.FluentThread;

public class AdminProjectEditPage extends Page {

    @Override
    public String getUrl() {
        return "/admin/projectEdit/";
    }

    public boolean isEditPageFromProject(TestBrowser browser, int id) {
        int editPageId = Integer.parseInt(browser.$("#id").first().getValue());
        return editPageId == id;
    }

    public void removeProject(TestBrowser browser) {
        browser.$("#removeProject").first().click();
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
}