package views;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RatingDisplayTest {

    @Test
    public void getNumberTest() {
        assertEquals(1, RatingDisplay.ONE.getNumber());
        assertEquals(2, RatingDisplay.TWO.getNumber());
        assertEquals(3, RatingDisplay.THREE.getNumber());
        assertEquals(4, RatingDisplay.FOUR.getNumber());
        assertEquals(5, RatingDisplay.FIVE.getNumber());
    }

    @Test
    public void getRepresentationTest() {
        assertEquals("--", RatingDisplay.ONE.getRepresentation());
        assertEquals("-", RatingDisplay.TWO.getRepresentation());
        assertEquals("0", RatingDisplay.THREE.getRepresentation());
        assertEquals("+", RatingDisplay.FOUR.getRepresentation());
        assertEquals("++", RatingDisplay.FIVE.getRepresentation());
    }

    @Test
    public void getRepresentationByNumberTest() {
        assertEquals("--", RatingDisplay.getRepresentationByNumber(1));
        assertEquals("-", RatingDisplay.getRepresentationByNumber(2));
        assertEquals("0", RatingDisplay.getRepresentationByNumber(3));
        assertEquals("+", RatingDisplay.getRepresentationByNumber(4));
        assertEquals("++", RatingDisplay.getRepresentationByNumber(5));
    }
}
