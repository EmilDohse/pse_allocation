package views.pages.student;

import play.test.TestBrowser;
import views.pages.Page;

public class StudentLearningGroupPage extends Page {

    @Override
    public String getUrl() {
        return "/student";
    }

    public boolean isCreateLearningGroupFormPresent(TestBrowser browser) {
        return !browser.$("#create_submit").isEmpty();
    }

    public void fillAndSubmitCreateLGForm(TestBrowser browser, String name,
            String password) {
        browser.$("#learningGroupname").first().fill().with(name);
        browser.$("#learningGroupPassword").first().fill().with(password);
        browser.$("#create_submit").first().click();
    }

    public void fillAndSubmitJoinLGForm(TestBrowser browser, String name,
            String password) {
        browser.$("#learningGroupname").first().fill().with(name);
        browser.$("#learningGroupPassword").first().fill().with(password);
        browser.$("#join_submit").first().click();
    }

    public void leaveLearningGroup(TestBrowser browser) {
        browser.$("#leave_submit").first().click();
    }
}
