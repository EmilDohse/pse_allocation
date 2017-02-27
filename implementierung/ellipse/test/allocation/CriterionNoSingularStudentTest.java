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

public class CriterionNoSingularStudentTest {

    private static EbeanServer                server;
    private static CriterionNoSingularStudent cnss;

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
        cnss = new CriterionNoSingularStudent();
    }

    @Test
    public void testGetName() {
        assertEquals("NoSingularStudent", cnss.getName());
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("Kein einzelner Student zu Lerngruppe",
                cnss.getDisplayName("de"));
        assertEquals("No singular student plus learning group in a team",
                cnss.getDisplayName("en"));
    }

    @Test
    public void testUseCriterion() {
        // TODO füllen
    }

    @After
    public void after() {
        cnss = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
