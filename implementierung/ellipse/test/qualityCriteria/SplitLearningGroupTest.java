package qualityCriteria;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import data.Allocation;
import data.LearningGroup;
import data.Semester;
import data.Student;
import data.Team;
import exception.DataException;

public class SplitLearningGroupTest {

    Semester   semester;
    Allocation allocation;

    @Before
    public void setup() throws DataException {
        semester = new Semester();
        allocation = new Allocation();

        LearningGroup lg1 = new LearningGroup();
        List<Student> members1 = new ArrayList<Student>();
        Student s1 = new Student();
        Student s2 = new Student();
        members1.add(s1);
        members1.add(s2);
        lg1.setMembers(members1);

        LearningGroup lg2 = new LearningGroup();
        List<Student> members2 = new ArrayList<Student>();
        Student s3 = new Student();
        Student s4 = new Student();
        members2.add(s3);
        members2.add(s4);
        lg2.setMembers(members2);

        Team t1 = new Team();
        t1.addMember(s1);
        t1.addMember(s3);

        Team t2 = new Team();
        t2.addMember(s2);
        t2.addMember(s4);

        List<Team> teams = new ArrayList<Team>();
        teams.add(t1);
        teams.add(t2);

        List<LearningGroup> lgs = new ArrayList<LearningGroup>();
        lgs.add(lg1);
        lgs.add(lg2);

        semester.setLearningGroups(lgs);
        allocation.setTeams(teams);
        allocation.setSemester(semester);
        semester.addAllocation(allocation);
    }

    @Test
    public void calculateTest() {
        SplitLearningGroups slg = new SplitLearningGroups();
        assertEquals(2, Integer.parseInt(slg.calculate(allocation)));
    }

}
