package views.pages.index;

import org.fluentlenium.core.FluentPage;

import play.test.TestBrowser;

public class IndexInformationPage extends IndexPage {

    public String getInfoText(TestBrowser browser) {
        return browser.$("#generalInformation").getText();
    }

    @Override
    public String getUrl() {
        return "/";
    }
}
