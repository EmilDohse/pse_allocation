package qualityCriteria;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.Allocation;
import data.GeneralData;
import data.Semester;
import data.Student;
import data.Team;
import exception.DataException;
import qualityCriteria.NotAllocatedStudents;

public class NotAllocatedStudentsTest {

    private Semester   s;
    private Allocation a;

    @Before
    public void setup() throws DataException {
        s = new Semester();
        a = new Allocation();
    }

    @Test
    public void calculateTest() throws DataException {

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
}
