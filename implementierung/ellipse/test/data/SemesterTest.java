package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Tests für die Klasse Semester.
 */
public class SemesterTest extends DataTest {

    private Semester semester;

    /**
     * Initialisierung des Semesters.
     */
    @Before
    public void beforeTest() {
        semester = new Semester();
        semester.save();
    }

    /**
     * Diese Methode testet sowohl getter als auch setter für den Status
     * 'Wintersemester' des Semesters.
     */
    @Test
    public void testWintersemester() {
        boolean w = true;
        semester.setWintersemester(w);
        assertEquals(w, semester.isWintersemester());
    }

    /**
     * Diese Methode testet sowohl getter als auch setter für den Namen des
     * Semesters.
     */
    @Test
    public void testName() {
        String n = "testname";
        semester.setName(n);
        assertEquals(n, semester.getName());
    }

    /**
     * Diese Methode testet das Entfernen und Hinzufügen von SPOs zum Semester.
     * Außerdem testet sie die getter und setter der SPOs auf Korrektheit.
     */
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

    /**
     * Diese Methode testet sowohl getter als auch setter für den InfoText des
     * Semesters.
     */
    @Test
    public void testInfoText() {
        String t = "testtext";
        semester.setInfoText(t);
        assertEquals(t, semester.getInfoText());
    }

    /**
     * Diese Methode testet sowohl getter als auch setter für die finale
     * Einteilung des Semesters.
     */
    @Test
    public void testFinalAllocation() {
        Allocation a = new Allocation();
        semester.setFinalAllocation(a);
        assertEquals(a, semester.getFinalAllocation());
    }

    /**
     * Diese Methode testet sowohl getter als auch setter für den Start der
     * Registrierungsphase des Semesters.
     */
    @Test
    public void testRegistrationStart() {
        Date d = new Date();
        semester.setRegistrationStart(d);
        assertEquals(d, semester.getRegistrationStart());
    }

    /**
     * Diese Methode testet sowohl getter als auch setter für das Ende der
     * Registrierungsphase des Semesters.
     */
    @Test
    public void testRegistrationEnd() {
        Date d = new Date();
        semester.setRegistrationEnd(d);
        assertEquals(d, semester.getRegistrationEnd());
    }

    /**
     * Diese Methode testet das Entfernen und Hinzufügen von Lerngruppen zum
     * Semester. Außerdem testet sie die getter und setter der Lerngruppenliste
     * auf Korrektheit.
     */
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

    /**
     * Diese Methode testet das Entfernen und Hinzufügen von Studenten zum
     * Semester. Außerdem testet sie die getter und setter der Studentenliste
     * auf Korrektheit.
     */
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

    /**
     * Diese Methode testet das Entfernen und Hinzufügen von Projekten zum
     * Semester. Außerdem testet sie die getter und setter der Projektliste auf
     * Korrektheit.
     */
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

    /**
     * Diese Methode testet das Entfernen und Hinzufügen von Einteilungen zum
     * Semester. Außerdem testet sie die getter und setter der Einteilungsliste
     * auf Korrektheit.
     */
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

    /**
     * Diese Methode testet das Hinzufügen und Entfernen von Betreuern zum
     * Semester, sowie den getter der Betreuerliste.
     */
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

    /**
     * Diese Methode testet die statische Methode, die alle Semester aus der
     * Datenbank zurückgibt, sowie die Methode, die ein spezifisches Semester
     * aus der Datenbank zurückgibt.
     */
    @Test
    public void testGetSemesters() {
        // clear Database
        Semester.getSemesters().forEach(s -> {
            if (!GeneralData.loadInstance().getCurrentSemester().equals(s)) {
                s.delete();
            }
        });
        Semester one = new Semester("one", true);
        Semester two = new Semester("two", true);
        one.save();

        // clear standard current Semester
        GeneralData data = GeneralData.loadInstance();
        Semester old = data.getCurrentSemester();
        data.doTransaction(() -> data.setCurrentSemester(one));
        old.delete();
        two.save();
        assertEquals(2, Semester.getSemesters().size());
        assertEquals(one, Semester.getSemester("one"));
    }

    @Test
    public void testCompareTo() {

        Semester s1 = new Semester();
        s1.setName("abc");
        Semester s2 = new Semester();
        s2.setName("test");
        assertEquals(s1.compareTo(s1), 0);
        assertTrue(s1.compareTo(s2) < 0);
        assertTrue(s2.compareTo(s1) > 0);
    }

    @Test
    public void testEquals() {

        Semester s = new Semester();
        s.save();

        Project p = new Project();
        p.save();

        assertTrue(s.equals(s));
        assertFalse(s.equals(p));
        assertFalse(s.equals(null));
    }
}
