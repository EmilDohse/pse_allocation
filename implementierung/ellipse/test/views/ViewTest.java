package views;

import org.junit.After;

import org.junit.Before;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import java.util.concurrent.TimeUnit;

import data.GeneralData;
import data.Semester;
import play.test.Helpers;
import play.test.WithBrowser;
import views.pages.index.IndexInformationPage;

import org.fluentlenium.core.FluentPage;

/**
 * Diese Klasse beinhaltet grundlegende Initialisierung der Testdaten und die
 * login/logout mehtode. Außerdem ist sie Oberklasse für alles anderen
 * ViewTests.
 *
 */
public class ViewTest extends WithBrowser {

    protected static EbeanServer server;

    private IndexInformationPage indexPage;

    /**
     * Diese Klasse stellt den TestBrowser zur Verfügung.
     */
    @Override
    protected play.test.TestBrowser provideBrowser(int port) {
        return Helpers.testBrowser(new NoJsErrorHtmlDriver(), port);
    }

    /**
     * Initialisierung der Testdaten und Konfiguration des Servers.
     */
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

    /**
     * Shutdown des Servers.
     */
    @After
    public void after() {
        server.shutdown(false, false);
    }

    /**
     * Diese Methode logt einen Benutzer ein
     * 
     * @param username
     *            Der Benutzername.
     * @param password
     *            Das Passwort.
     * @param page
     *            Die Seite, die nach dem Login resultieren soll.
     * @return True, wenn Login in erfolgreich, false sonst.
     */
    public boolean login(String username, String password, FluentPage page) {
        indexPage.go();
        browser.$("#login_username").first().fill().with(username);
        browser.$("#login_password").first().fill().with(password);
        browser.$("#login_submit").first().click();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(page).isAt();
        return !browser.url().equals(indexPage.getUrl());
    }

    /**
     * Logout des momentan eingeloggten Benutzers.
     */
    public void logout() {
        browser.$("#logout_button").first().click();
        browser.await().atMost(2, TimeUnit.SECONDS).untilPage(indexPage).isAt();
    }
}
