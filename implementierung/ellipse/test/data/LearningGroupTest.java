package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Kllasse beinhaltet Unit-Tests für die Klasse LearningGroup.
 *
 */
public class LearningGroupTest extends DataTest {

    private LearningGroup learningGroup;

    /**
     * Initialisierung der Lerngruppe.
     */
    @Before
    public void beforeTest() {
        learningGroup = new LearningGroup();
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter für den Namen der
     * Lerngruppe.
     */
    @Test
    public void testName() {
        String n = "testname";
        learningGroup.setName(n);
        assertEquals(n, learningGroup.getName());
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter für das Passwort der
     * Lerngruppe.
     */
    @Test
    public void testPassword() {
        String p = "123456";
        learningGroup.setPassword(p);
        assertEquals(p, learningGroup.getPassword());
    }

    /**
     * Diese Methode testet, ob das Hinzufügen und Entfernen von Studenten
     * einwandfrei funktioniert.
     */
    @Test
    public void testMembers() {
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

    /**
     * Diese Methode testet, ob das Setzen von Bewertungen für ein Projekt
     * einwandfrei funktioniert.
     */
    @Test
    public void testRatings() {
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

    /**
     * Diese Methode testet sowohl getter alsauch setter für den Status der
     * Lerngruppe.
     */
    @Test
    public void testPrivate() {
        boolean p = true;
        learningGroup.setPrivate(p);
        assertEquals(learningGroup.isPrivate(), p);
    }

    /**
     * Diese Methode testet die statische Methode, mit der man eine spezifische
     * Lerngruppe aus der Datenbank erhält.
     */
    @Test
    public void testGetLearningGroup() {
        Semester one = new Semester();
        Semester two = new Semester();

        LearningGroup oneGroup = new LearningGroup("test", "test123");
        LearningGroup twoGroup = new LearningGroup("test", "test1234");
        one.doTransaction(() -> {
            one.addLearningGroup(oneGroup);
        });
        two.doTransaction(() -> {
            two.addLearningGroup(twoGroup);
        });
        assertEquals(oneGroup, LearningGroup.getLearningGroup("test", one));

    }
}
