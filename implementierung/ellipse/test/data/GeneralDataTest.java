package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GeneralDataTest extends DataTest {

    private GeneralData data;

    @Before
    public void before() {
        data = GeneralData.loadInstance();
    }

    @Test
    public void testGetInstance() {
        assertEquals(data, GeneralData.loadInstance());
    }

    @Test
    public void testSetSemester() {
        Semester semester = new Semester();
        data.setCurrentSemester(semester);
        assertEquals(semester, data.getCurrentSemester());
    }

    @Test
    public void testSetSaveAndReload() {
        Semester semester = new Semester();
        GeneralData.loadInstance().setCurrentSemester(semester);
        GeneralData.loadInstance().save();
        assertEquals(semester, GeneralData.loadInstance().getCurrentSemester());
    }

    @Test
    public void testWithSemester() {
        Semester s = new Semester();
        GeneralData.loadInstance().setCurrentSemester(s);
        GeneralData.loadInstance().save();
        assertEquals(s, GeneralData.loadInstance().getCurrentSemester());
    }

    @Test
    public void testWithAllocation() {
        Semester s = new Semester();
        Allocation a = new Allocation();
        s.setFinalAllocation(a);
        GeneralData.loadInstance().setCurrentSemester(s);
        GeneralData.loadInstance().save();
        assertEquals(s, GeneralData.loadInstance().getCurrentSemester());
        assertEquals(a, s.getFinalAllocation());
    }

    @Test
    public void testWithTeams() {
        Semester s = new Semester();
        Allocation a = new Allocation();
        Team t = new Team();
        List<Team> teams = new ArrayList<Team>();
        teams.add(t);
        a.setTeams(teams);
        s.setFinalAllocation(a);
        GeneralData.loadInstance().setCurrentSemester(s);
        GeneralData.loadInstance().save();
        assertEquals(s, GeneralData.loadInstance().getCurrentSemester());
        assertEquals(a, s.getFinalAllocation());
        assertNotNull(a.getTeams());
        assertFalse(a.getTeams().isEmpty());
        assertTrue(teams.contains(t));
    }

    @Test
    public void testWithStudent() {
        Student student = new Student();
        Semester s = new Semester();
        Allocation a = new Allocation();
        Team t = new Team();
        Project p = new Project();
        List<Student> students = new ArrayList<Student>();
        students.add(student);
        t.setMembers(students);
        t.setProject(p);
        List<Team> teams = new ArrayList<Team>();
        teams.add(t);
        a.setTeams(teams);
        s.setFinalAllocation(a);
        GeneralData.loadInstance().setCurrentSemester(s);
        GeneralData.loadInstance().save();
        assertEquals(s, GeneralData.loadInstance().getCurrentSemester());
        assertEquals(a, s.getFinalAllocation());
        assertNotNull(a.getTeams());
        assertFalse(a.getTeams().isEmpty());
        assertTrue(teams.contains(t));
        assertFalse(t.getMembers().isEmpty());
        assertTrue(t.getMembers().contains(student));
    }

}
