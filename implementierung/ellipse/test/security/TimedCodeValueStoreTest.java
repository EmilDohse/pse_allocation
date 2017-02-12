package security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TimedCodeValueStoreTest {

    private TimedCodeValueStore<Object> tcvs;

    @Test
    public void testValidPop() {
        tcvs = new TimedCodeValueStore<>(1, 42);
        Object object = new Object();
        String code = tcvs.store(object);
        Object result = tcvs.pop(code);
        assertEquals(object, result);
    }

    @Test
    public void testInvalidPop() {
        tcvs = new TimedCodeValueStore<>(0, 42);
        Object object = new Object();
        String code = tcvs.store(object);
        Object result = tcvs.pop(code);
        assertNull(result);
    }
}
