package data;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

public class DataTest {

    private static EbeanServer server;

    @BeforeClass
    public static void beforeClass() {
        ServerConfig config = new ServerConfig();
        config.setName("db");
        config.loadTestProperties();
        config.setDefaultServer(true);
        config.setRegister(true);

        server = EbeanServerFactory.create(config);
    }

    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
