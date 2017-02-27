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

public class CriterionPreferredTeamSizeTest {

    private static EbeanServer                server;
    private static CriterionPreferredTeamSize cpts;

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
        cpts = new CriterionPreferredTeamSize();
    }

    @Test
    public void testGetName() {
        assertEquals("PreferredTeamSize", cpts.getName());
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("Beachte gewünschte Teamgröße", cpts.getDisplayName("de"));
        assertEquals("Respect preferred team size", cpts.getDisplayName("en"));
    }

    @Test
    public void testUseCriterion() {
        // TODO füllen
    }

    @After
    public void after() {
        cpts = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
