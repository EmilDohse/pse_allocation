package qualityCriteria;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import data.Allocation;
import data.GeneralData;
import data.LearningGroup;
import data.Project;
import data.Rating;
import data.Semester;
import data.Student;
import data.Team;
import exception.DataException;

public class StudentHappinessTest {

    private Semester            s;
    private Allocation          a;
    private List<Team>          teams;
    private List<LearningGroup> learningGroups;
    private GeneralData         data;
    private Project             project;

    @Before
    public void setup() throws DataException {
        s = new Semester();
        a = new Allocation();
        project = new Project();
        s.addProject(project);
        a.setSemester(s);
        learningGroups = new ArrayList<LearningGroup>();
        teams = new ArrayList<Team>();

        data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(s);
        });

        LearningGroup lg1 = new LearningGroup();
        Student firstS = new Student();
        Student secondS = new Student();
        List<Student> members = new ArrayList<Student>();
        members.add(firstS);
        members.add(secondS);
        lg1.setMembers(members);
        Rating r1 = new Rating(0, project);
        r1.setLearningGroup(lg1);

        LearningGroup lg2 = new LearningGroup();
        Student thirdS = new Student();
        Student fourthS = new Student();
        List<Student> members2 = new ArrayList<Student>();
        members2.add(thirdS);
        members2.add(fourthS);
        lg2.setMembers(members2);
        Rating r2 = new Rating(5, project);
        r2.setLearningGroup(lg2);

        learningGroups.add(lg1);
        learningGroups.add(lg2);

        Team t1 = new Team();
        t1.addMember(firstS);
        t1.addMember(secondS);

        Team t2 = new Team();
        t2.addMember(thirdS);
        t2.addMember(fourthS);

        teams.add(t1);
        teams.add(t2);

        a.setTeams(teams);
        s.setLearningGroups(learningGroups);
    }

    @Ignore
    @Test
    public void calculationTest() {
        StudentHappiness sh = new StudentHappiness();
        assertTrue(50.0 - Double.parseDouble(sh.calculate(a)) < 0.1);
    }
}
