package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LearningGroupTest extends DataTest {

    private LearningGroup learningGroup;

    @Before
    public void beforeTest() {
        learningGroup = new LearningGroup();
    }

    @Test
    public void testName() {
        String n = "testname";
        learningGroup.setName(n);
        assertEquals(n, learningGroup.getName());
    }

    @Test
    public void testPassword() {
        String p = "123456";
        learningGroup.setPassword(p);
        assertEquals(p, learningGroup.getPassword());
    }

    @Test
    public void testMembers() {
        Student firstS = new Student();
        Student secondS = new Student();
        List<Student> members = new ArrayList<Student>();
        members.add(firstS);
        learningGroup.setMembers(members);
        assertEquals(learningGroup.getMembers().size(), 1);
        assertTrue(learningGroup.getMembers().contains(firstS));

        learningGroup.addMember(secondS);
        assertEquals(learningGroup.getMembers().size(), 2);
        assertTrue(learningGroup.getMembers().contains(firstS));
        assertTrue(learningGroup.getMembers().contains(secondS));

        learningGroup.removeMember(firstS);
        assertEquals(learningGroup.getMembers().size(), 1);
        assertTrue(learningGroup.getMembers().contains(secondS));
    }

    @Test
    public void testRatings() {
        Rating rating = new Rating();
        Project firstP = new Project();
        rating.setProject(firstP);
        List<Rating> ratings = new ArrayList<Rating>();
        ratings.add(rating);
        learningGroup.setRatings(ratings);
        assertEquals(learningGroup.getRatings().size(), 1);
        assertTrue(learningGroup.getRatings().contains(rating));

        int firstR = 11;
        learningGroup.rate(firstP, firstR);
        assertEquals(learningGroup.getRating(firstP), firstR);

        int secondR = 42;
        Project secondP = new Project();
        learningGroup.rate(secondP, secondR);
        assertEquals(learningGroup.getRating(secondP), secondR);
        assertEquals(learningGroup.getRatings().size(), 2);
    }

    @Test
    public void testPrivate() {
        boolean p = true;
        learningGroup.setPrivate(p);
        assertEquals(learningGroup.isPrivate(), p);
    }
}
