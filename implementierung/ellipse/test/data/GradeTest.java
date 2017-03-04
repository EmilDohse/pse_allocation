package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GradeTest {

    @Test
    public void getNameTest() {
        assertEquals(Grade.ONE_ZERO.getName(), "1.0");
    }

    @Test
    public void getNumberTest() {
        assertEquals(Grade.ONE_ZERO.getNumber(), 100);
    }

    @Test
    public void getGradeByNumberTest() {
        assertEquals(Grade.getGradeByNumber(100), Grade.ONE_ZERO);
    }
}
