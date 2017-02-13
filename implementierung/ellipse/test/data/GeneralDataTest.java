package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Test für die GeneralData Klasse.
 *
 */
public class GeneralDataTest extends DataTest {

    private GeneralData data;

    /**
     * Initialisierung GeneralData.
     */
    @Before
    public void before() {
        data = GeneralData.loadInstance();
    }

    /**
     * Testet die getInstance Methode auf Korrektheit.
     */
    @Test
    public void testGetInstance() {
        assertEquals(data, GeneralData.loadInstance());
    }

    /**
     * Testet setter und getter für das aktuelle Semester.
     */
    @Test
    public void testSetSemester() {
        Semester semester = new Semester();
        data.setCurrentSemester(semester);
        assertEquals(semester, data.getCurrentSemester());
    }

    /**
     * Testet Speichern und Laten aus der Datenbank.
     */
    @Test
    public void testSetSaveAndReload() {
        Semester semester = new Semester();
        GeneralData testData = GeneralData.loadInstance();
        testData.doTransaction(() -> {
            testData.setCurrentSemester(semester);
        });
        assertEquals(semester, GeneralData.loadInstance().getCurrentSemester());
    }

    /**
     * Testet Speichern und Laden mit einer finalen Einteilung.
     */
    @Test
    public void testWithAllocation() {
        Semester s = new Semester();
        Allocation a = new Allocation();
        s.doTransaction(() -> {
            s.setFinalAllocation(a);
        });
        GeneralData testData = GeneralData.loadInstance();
        testData.doTransaction(() -> {
            testData.setCurrentSemester(s);
        });
        assertEquals(s, GeneralData.loadInstance().getCurrentSemester());
        assertEquals(a, s.getFinalAllocation());
    }

    /**
     * Testet Speichern und Laden mit finaler Einteilung und Teams.
     */
    @Test
    public void testWithTeams() {
        Semester s = new Semester();
        Allocation a = new Allocation();
        Team t = new Team();
        List<Team> teams = new ArrayList<Team>();
        teams.add(t);
        a.doTransaction(() -> {
            a.setTeams(teams);
        });
        s.doTransaction(() -> {
            s.setFinalAllocation(a);
        });
        GeneralData testData = GeneralData.loadInstance();
        testData.doTransaction(() -> {
            testData.setCurrentSemester(s);
        });
        assertEquals(s, GeneralData.loadInstance().getCurrentSemester());
        assertEquals(a, s.getFinalAllocation());
        assertNotNull(a.getTeams());
        assertFalse(a.getTeams().isEmpty());
        assertTrue(teams.contains(t));
    }

    /**
     * Testet Speichern und Laden mit finaler Einteilung, Teams und Studenten.
     */
    @Test
    public void testWithStudent() {
        Student student = new Student();
        Semester s = new Semester();
        Allocation a = new Allocation();
        Team t = new Team();
        Project p = new Project();
        p.doTransaction(() -> {
            p.setMinTeamSize(0);
            p.setMaxTeamSize(20);
        });
        List<Student> students = new ArrayList<Student>();
        students.add(student);
        t.doTransaction(() -> {
            t.setProject(p);
            t.setMembers(students);
        });
        List<Team> teams = new ArrayList<Team>();
        teams.add(t);
        a.doTransaction(() -> {
            a.setTeams(teams);
        });
        s.doTransaction(() -> {
            s.setFinalAllocation(a);
        });
        GeneralData testData = GeneralData.loadInstance();
        testData.doTransaction(() -> {
            testData.setCurrentSemester(s);
        });
        assertEquals(s, GeneralData.loadInstance().getCurrentSemester());
        assertEquals(a, s.getFinalAllocation());
        assertNotNull(a.getTeams());
        assertFalse(a.getTeams().isEmpty());
        assertTrue(teams.contains(t));
        assertFalse(t.getMembers().isEmpty());
        assertTrue(t.getMembers().contains(student));
    }

}
