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

public class CriterionPreferHigherSemesterTest {

    private static EbeanServer                   server;
    private static CriterionPreferHigherSemester cphs;

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
        cphs = new CriterionPreferHigherSemester();
    }

    @Test
    public void testGetName() {
        assertEquals("PreferHigherSemester", cphs.getName());
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("Bevorzuge Studenten höheren Semesters",
                cphs.getDisplayName("de"));
        assertEquals("Bonus students of higher semesters",
                cphs.getDisplayName("en"));
    }

    @Test
    public void testUseCriterion() {
        // TODO füllen
    }

    @After
    public void after() {
        cphs = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
