package data;

import org.junit.After;
import org.junit.Before;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

/**
 * Oberklasse für alle Daten Tests, welche Serverdaten und allgemeine Daten
 * initialisiert.
 */
public class DataTest {

    private static EbeanServer server;

    /**
     * Initialisiert Serverdaten und allgemeine Daten.
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

    }

    /**
     * Server shutdown.
     */
    @After
    public void after() {
        server.shutdown(false, false);
    }
}
