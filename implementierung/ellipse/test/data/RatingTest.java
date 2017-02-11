package data;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import exception.DataException;

public class RatingTest extends DataTest {

    private Rating rating;

    @Before
    public void beforeTest() throws DataException {
        rating = new Rating();
    }

    @Test
    public void testRating() throws DataException {
        int r = 1;
        rating.setRating(r);
        assertEquals(r, rating.getRating());
    }

    @Test
    public void testProject() throws DataException {
        Project p = new Project();
        rating.setProject(p);
        assertEquals(p, rating.getProject());
    }
}
