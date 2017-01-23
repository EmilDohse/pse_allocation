package data;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AllocationParameterTest extends DataTest {

    private AllocationParameter allocationParameter;

    @Before
    public void beforeTest() {
        allocationParameter = new AllocationParameter();
    }

    @Test
    public void testName() {
        String n = "testname";
        allocationParameter.setName(n);
        assertEquals(n, allocationParameter.getName());
    }

    @Test
    public void testValue() {
        int v = 11;
        allocationParameter.setValue(v);
        assertEquals(v, allocationParameter.getValue());
    }
}
