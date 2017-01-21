package data;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class RatingTest extends DataTest {
    
    private Rating rating;
    
    @Before
    public void beforeTest() {
        rating = new Rating();
    }
    
    @Test
    public void testRating() {
        int r = 11;
        rating.setRating(r);
        assertEquals(r, rating.getRating());
    }
    
    @Test
    public void testProject() {
        Project p = new Project();
        rating.setProject(p);
        assertEquals(p, rating.getProject());
    }
}
