package views.pages.index;

import static org.junit.Assert.assertTrue;

import org.fluentlenium.core.FluentPage;

import play.test.TestBrowser;
import views.pages.Page;

public class IndexPage extends Page {

    public void gotoPasswordResetPage(TestBrowser browser) {
        browser.$("#pwReset").click();
    }

    @Override
    public String getUrl() {
        return "/";
    }
}
