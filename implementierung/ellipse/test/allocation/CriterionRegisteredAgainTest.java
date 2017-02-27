package allocation;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

public class CriterionRegisteredAgainTest {

    private static EbeanServer              server;
    private static CriterionRegisteredAgain cra;

    /**
     * Setup Server.
     */
    @BeforeClass
    public static void beforeClass() {
        ServerConfig config = new ServerConfig();
        config.setName("db");
        config.loadTestProperties();
        config.setDefaultServer(true);
        config.setRegister(true);

        server = EbeanServerFactory.create(config);
    }

    @Before
    public void before() {
        cra = new CriterionRegisteredAgain();
    }

    @Test
    public void testGetName() {
        assertEquals("RegisteredAgain", cra.getName());
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("Bonus für Zweitanmeldung", cra.getDisplayName("de"));
        assertEquals("Bonus second registration", cra.getDisplayName("en"));
    }

    @Test
    public void testUseCriterion() {
        // TODO füllen
    }

    @After
    public void after() {
        cra = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
