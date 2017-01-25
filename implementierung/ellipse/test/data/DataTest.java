package data;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import play.Application;
import play.test.Helpers;

public class DataTest {

    private static Application app;

    @BeforeClass
    public static void beforeClass() {
        app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
        Helpers.start(app);
    }

    @AfterClass
    public static void afterClass() {
        Helpers.stop(app);
    }
}
