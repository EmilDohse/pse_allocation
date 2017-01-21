package allocation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.AllocationTest;
import exception.AllocationException;
import jdk.nashorn.internal.ir.annotations.Ignore;

public class AbstractAllocatorTest {

    AbstractAllocator abstAllocator;

    @BeforeClass
    public void init() {
        abstAllocator = new AbstractAllocator() {

            @Override
            public void cancel() {
                // empty
            }

            @Override
            public void calculate(Configuration configuration) throws AllocationException {
                // empty
            }
        };
    }

    /**
     * test der überprüft ob der Serviceloader eine liste mit mehr als einem
     * element zurückgibt
     */
    @Test @Ignore
    public void testServiceLoader() {
        assert (abstAllocator.getAllCriteria().size() > 0);
    }

}
