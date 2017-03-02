package views;

import org.junit.After;
import org.junit.Before;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import java.util.concurrent.TimeUnit;

import data.Allocation;
import data.GeneralData;
import data.Semester;
import play.test.Helpers;
import play.test.WithBrowser;
import views.pages.admin.AdminResultsPage;
import views.pages.index.IndexInformationPage;

import org.fluentlenium.core.FluentPage;

public class ViewTest extends WithBrowser {

    protected static EbeanServer server;

    private IndexInformationPage indexPage;

    @Override
    protected play.test.TestBrowser provideBrowser(int port) {
        return Helpers.testBrowser(new NoJsErrorHtmlDriver(), port);
    }

    @Before
    public void before() {
        ServerConfig config = new ServerConfig();
        config.setName("db");
        config.loadTestProperties();
        config.setDefaultServer(true);
        config.setRegister(true);

        server = EbeanServerFactory.create(config);
        // Init General Data. Evolutions wollen nicht funktionieren
        GeneralData data = new GeneralData();
        data.save();
        Semester semester = new Semester();
        semester.save();
        data.setCurrentSemester(semester);
        data.save();

        indexPage = browser.createPage(IndexInformationPage.class);
    }

    @After
    public void after() {
        server.shutdown(false, false);
    }

    public boolean login(String username, String password, FluentPage page) {
        indexPage.go();
        browser.$("#login_username").first().fill().with(username);
        browser.$("#login_password").first().fill().with(password);
        browser.$("#login_submit").first().click();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(page).isAt();
        return !browser.url().equals(indexPage.getUrl());
    }

    public void logout() {
        browser.$("#logout_button").first().click();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(indexPage).isAt();
    }
}
