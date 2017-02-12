package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SemesterTest extends DataTest {

    private Semester semester;

    @Before
    public void beforeTest() {
        semester = new Semester();
        semester.save();
    }

    @Test
    public void testWintersemester() {
        boolean w = true;
        semester.setWintersemester(w);
        assertEquals(w, semester.isWintersemester());
    }

    @Test
    public void testYear() {
        int y = 2016;
        semester.setYear(y);
        assertEquals(y, semester.getYear());
    }

    @Test
    public void testName() {
        String n = "testname";
        semester.setName(n);
        assertEquals(n, semester.getName());
    }

    @Test
    public void testSPOs() {
        SPO firstSPO = new SPO();
        SPO secondSPO = new SPO();
        List<SPO> spos = new ArrayList<SPO>();
        spos.add(firstSPO);
        semester.setSpos(spos);
        assertEquals(semester.getSpos().size(), 1);
        assertTrue(semester.getSpos().contains(firstSPO));

        semester.addSPO(secondSPO);
        assertEquals(semester.getSpos().size(), 2);
        assertTrue(semester.getSpos().contains(firstSPO));
        assertTrue(semester.getSpos().contains(secondSPO));

        semester.removeSPO(firstSPO);
        assertEquals(semester.getSpos().size(), 1);
        assertTrue(semester.getSpos().contains(secondSPO));
    }

    @Test
    public void testInfoText() {
        String t = "testtext";
        semester.setInfoText(t);
        assertEquals(t, semester.getInfoText());
    }

    @Test
    public void testFinalAllocation() {
        Allocation a = new Allocation();
        semester.setFinalAllocation(a);
        assertEquals(a, semester.getFinalAllocation());
    }

    @Test
    public void testRegistrationStart() {
        Date d = new Date();
        semester.setRegistrationStart(d);
        assertEquals(d, semester.getRegistrationStart());
    }

    @Test
    public void testRegistrationEnd() {
        Date d = new Date();
        semester.setRegistrationEnd(d);
        assertEquals(d, semester.getRegistrationEnd());
    }

    @Test
    public void testLearningGroups() {
        LearningGroup firstL = new LearningGroup();
        LearningGroup secondL = new LearningGroup();
        List<LearningGroup> learningGroups = new ArrayList<LearningGroup>();
        learningGroups.add(firstL);
        semester.setLearningGroups(learningGroups);
        assertEquals(semester.getLearningGroups().size(), 1);
        assertTrue(semester.getLearningGroups().contains(firstL));

        semester.addLearningGroup(secondL);
        assertEquals(semester.getLearningGroups().size(), 2);
        assertTrue(semester.getLearningGroups().contains(firstL));
        assertTrue(semester.getLearningGroups().contains(secondL));

        semester.removeLearningGroup(firstL);
        assertEquals(semester.getLearningGroups().size(), 1);
        assertTrue(semester.getLearningGroups().contains(secondL));
    }

    @Test
    public void testStudents() {
        Student firstS = new Student();
        Student secondS = new Student();
        List<Student> students = new ArrayList<Student>();
        students.add(firstS);
        semester.setStudents(students);
        assertEquals(semester.getStudents().size(), 1);
        assertTrue(semester.getStudents().contains(firstS));

        semester.addStudent(secondS);
        assertEquals(semester.getStudents().size(), 2);
        assertTrue(semester.getStudents().contains(firstS));
        assertTrue(semester.getStudents().contains(secondS));

        semester.removeStudent(firstS);
        assertEquals(semester.getStudents().size(), 1);
        assertTrue(semester.getStudents().contains(secondS));
    }

    @Test
    public void testProjects() {
        Project firstP = new Project();
        Project secondP = new Project();
        List<Project> projects = new ArrayList<Project>();
        projects.add(firstP);
        semester.setProjects(projects);
        assertEquals(semester.getProjects().size(), 1);
        assertTrue(semester.getProjects().contains(firstP));

        semester.addProject(secondP);
        assertEquals(semester.getProjects().size(), 2);
        assertTrue(semester.getProjects().contains(firstP));
        assertTrue(semester.getProjects().contains(secondP));

        semester.removeProject(firstP);
        assertEquals(semester.getProjects().size(), 1);
        assertTrue(semester.getProjects().contains(secondP));
    }

    @Test
    public void testAllocations() {
        Allocation firstA = new Allocation();
        Allocation secondA = new Allocation();
        List<Allocation> allocations = new ArrayList<Allocation>();
        allocations.add(firstA);
        semester.setAllocations(allocations);
        assertEquals(semester.getAllocations().size(), 1);
        assertTrue(semester.getAllocations().contains(firstA));

        semester.addAllocation(secondA);
        assertEquals(semester.getAllocations().size(), 2);
        assertTrue(semester.getAllocations().contains(firstA));
        assertTrue(semester.getAllocations().contains(secondA));

        semester.removeAllocation(firstA);
        assertEquals(semester.getAllocations().size(), 1);
        assertTrue(semester.getAllocations().contains(secondA));
    }

    @Test
    public void testGetAdvisers() {
        Adviser a = new Adviser();
        List<Adviser> advisers = new ArrayList<Adviser>();
        advisers.add(a);
        Project p = new Project();
        p.setAdvisers(advisers);
        List<Project> projects = new ArrayList<Project>();
        projects.add(p);
        semester.setProjects(projects);
        assertEquals(semester.getAdvisers().size(), 1);
        assertTrue(semester.getAdvisers().contains(a));
    }
}
