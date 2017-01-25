package data;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class DataTest {

    protected void testMethodWithDatabase(Runnable r) {
        running(fakeApplication(inMemoryDatabase()), r);
    }
}
