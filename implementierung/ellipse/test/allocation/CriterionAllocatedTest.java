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

public class CriterionAllocatedTest {

    private static EbeanServer        server;
    private static CriterionAllocated ca;

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
        ca = new CriterionAllocated();
    }

    @Test
    public void testGetName() {
        assertEquals("Allocated", ca.getName());
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("Teile möglichst viele Studenten ein",
                ca.getDisplayName("de"));
        assertEquals("Assign as many students as possible",
                ca.getDisplayName("en"));
    }

    @Test
    public void testUseCriterion() {
        // TODO füllen
    }

    @After
    public void after() {
        ca = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
