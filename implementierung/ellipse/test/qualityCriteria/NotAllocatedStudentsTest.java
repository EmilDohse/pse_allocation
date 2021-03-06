package qualityCriteria;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import data.Allocation;
import data.Semester;
import data.Student;
import data.Team;

/**
 * Diese Klasse beinhaltet Unit-Test für das Gütekriterium, welches die Anzhal
 * nicht zugeteilter Studenten berechnet.
 */
public class NotAllocatedStudentsTest {

    private Semester   s;
    private Allocation a;

    /**
     * Setup code.
     */
    @Before
    public void setup() {
        s = new Semester();
        a = new Allocation();
    }

    /**
     * Diese Methode testet, ob das Gütekriterium die richtige Anzahl nicht
     * zugeteilter Studenten berechnet.
     */
    @Test
    public void calculateTest() {

        List<Student> allStudents = new ArrayList<Student>();

        List<Team> teams = new ArrayList<Team>();

        for (int i = 1; i <= 150; i++) {
            Student st = new Student();
            s.addStudent(st);
            if (i % 5 == 0) {
                allStudents.add(st);
                Team t = new Team();
                for (int j = i - 4; j < i; j++) {
                    t.addMember(allStudents.get(j));
                }
                teams.add(t);
            } else {
                allStudents.add(st);
            }
        }

        a.setTeams(teams);
        a.setSemester(s);
        NotAllocatedStudents nas = new NotAllocatedStudents();
        assertEquals(allStudents.size() / 5, Integer.parseInt(nas.calculate(a)));
    }

    /**
     * Test ob der Name richtig angezeigt wird.
     */
    @Test
    public void testDisplayName() {
        assertEquals("Anzahl nicht zugeteilter Studenten", new NotAllocatedStudents().getName("de"));
        assertEquals("Number of not assigned students", new NotAllocatedStudents().getName("en"));
    }
}
