package security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Test für die Klasse TimedCodeValueStore.
 */
public class TimedCodeValueStoreTest {

    private TimedCodeValueStore<Object> tcvs;

    /**
     * Diese Methode testet, ob der TimedCodeValueStore korrekt speichert und
     * mit passendem code herausgibt.
     */
    @Test
    public void testValidPop() {
        tcvs = new TimedCodeValueStore<>(1, 42);
        Object object = new Object();
        String code = tcvs.store(object);
        Object result = tcvs.pop(code);
        assertEquals(object, result);
    }

    /**
     * Diese Methode testet, ob der TimedCodeValueStore nicht korrekt speichert
     * und nicht korrekt herausgibt, mit nicht korekten Anfragen.
     */
    @Test
    public void testInvalidPop() {
        tcvs = new TimedCodeValueStore<>(0, 42);
        Object object = new Object();
        String code = tcvs.store(object);
        Object result = tcvs.pop(code);
        assertNull(result);
    }
}
