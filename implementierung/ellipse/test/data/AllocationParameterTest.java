package data;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Tests für die Klasse AllocationParameter.
 */
public class AllocationParameterTest extends DataTest {

    private AllocationParameter allocationParameter;

    /**
     * Initialisierung des allocationParameters.
     */
    @Before
    public void beforeTest() {
        allocationParameter = new AllocationParameter();
    }

    /**
     * Diese Methode testet sowohl den getter alsauch den setter für den Namen
     * des Parameters.
     */
    @Test
    public void testName() {
        String n = "testname";
        allocationParameter.setName(n);
        assertEquals(n, allocationParameter.getName());
    }

    /**
     * Diese Methode testet sowohl den getter alsauch den setter für den Wert
     * des Parameters.
     */
    @Test
    public void testValue() {
        int v = 11;
        allocationParameter.setValue(v);
        assertEquals(v, allocationParameter.getValue());
    }
}
