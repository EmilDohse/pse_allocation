package views.pages;

import static org.junit.Assert.assertTrue;

import org.fluentlenium.core.FluentPage;

import play.test.TestBrowser;

public class Page extends FluentPage {

    public void gotoMenuEntry(TestBrowser browser, int index) {
        browser.$(".nav-sidebar > li > a").get(index).click();
    }

    @Override
    public void isAt() {
        assertTrue(url().startsWith(getUrl()));
    }
}
