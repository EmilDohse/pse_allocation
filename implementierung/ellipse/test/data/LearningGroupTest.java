package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import exception.DataException;

public class LearningGroupTest extends DataTest {

    private LearningGroup learningGroup;

    @Before
    public void beforeTest() throws DataException {
        learningGroup = new LearningGroup();
    }

    @Test
    public void testName() throws DataException {
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
    public void testMembers() throws DataException {
        Semester semester = new Semester();
        GeneralData data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(semester);
        });
        semester.setMaxGroupSize(7);
        learningGroup.setSemester(semester);
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
    public void testRatings() throws DataException {
        // TODO Warum Reihenfolge beim save relevant?
        Rating rating = new Rating();
        Project firstP = new Project();
        firstP.save();
        rating.setProject(firstP);
        List<Rating> ratings = new ArrayList<Rating>();
        ratings.add(rating);
        learningGroup.setRatings(ratings);
        learningGroup.save();
        assertEquals(1, learningGroup.getRatings().size());
        assertTrue(learningGroup.getRatings().contains(rating));

        int firstR = 4;
        learningGroup.rate(firstP, firstR);
        learningGroup.save();
        assertEquals(firstR, learningGroup.getRating(firstP));

        int secondR = 5;
        Project secondP = new Project();
        secondP.save();
        learningGroup.rate(secondP, secondR);
        learningGroup.save();
        assertEquals(secondR, learningGroup.getRating(secondP));
        assertEquals(2, learningGroup.getRatings().size());
    }

    @Test
    public void testPrivate() {
        boolean p = true;
        learningGroup.setPrivate(p);
        assertEquals(learningGroup.isPrivate(), p);
    }
}
