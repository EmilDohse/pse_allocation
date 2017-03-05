package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Diese Klasse beinhaltet Tests für die Enum Grade.
 */
public class GradeTest {

    /**
     * Test für die Methode getName.
     */
    @Test
    public void getNameTest() {
        assertEquals(Grade.ONE_ZERO.getName(), "1.0");
    }

    /**
     * Test für die Methode getNumber.
     */
    @Test
    public void getNumberTest() {
        assertEquals(Grade.ONE_ZERO.getNumber(), 100);
    }

    /**
     * Test für die Methode getGradeByNumber.
     */
    @Test
    public void getGradeByNumberTest() {
        assertEquals(Grade.getGradeByNumber(100), Grade.ONE_ZERO);
    }
}
