package data;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Tests für die Klasse Rating.
 *
 */
public class RatingTest extends DataTest {

    private Rating rating;

    /**
     * Initialisierung des Ratings.
     */
    @Before
    public void beforeTest() {
        rating = new Rating();
    }

    /**
     * Diese Methode testet sowohl getter als auch setter für den Wert des
     * Ratings auf Korrektheit.
     */
    @Test
    public void testRating() {
        int r = 1;
        rating.setRating(r);
        assertEquals(r, rating.getRating());
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter für das Projekt des
     * Ratings auf Korrektheit.
     */
    @Test
    public void testProject() {
        Project p = new Project();
        rating.setProject(p);
        assertEquals(p, rating.getProject());
    }
}
