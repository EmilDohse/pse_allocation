package views;

import org.junit.After;
import org.junit.Before;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import data.Allocation;
import data.GeneralData;
import data.Semester;
import play.test.WithBrowser;

public class ViewTest extends WithBrowser {

    protected static EbeanServer server;

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
    }

    @After
    public void after() {
        server.shutdown(false, false);
    }
}
