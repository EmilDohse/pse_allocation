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

public class CriterionSameSemesterTest {

    private static EbeanServer           server;
    private static CriterionSameSemester css;

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
        css = new CriterionSameSemester();
    }

    @Test
    public void testGetName() {
        assertEquals("SameSemester", css.getName());
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("Studenten des selben Semesters in einem Team",
                css.getDisplayName("de"));
        assertEquals("Students in a team belong to same semester",
                css.getDisplayName("en"));
    }

    @Test
    public void testUseCriterion() {
        // TODO füllen
    }

    @After
    public void after() {
        css = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
