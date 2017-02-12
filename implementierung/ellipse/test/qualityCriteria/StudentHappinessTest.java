package qualityCriteria;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import data.Allocation;
import data.LearningGroup;
import data.Project;
import data.Rating;
import data.Semester;
import data.Student;
import data.Team;
import exception.DataException;
import play.test.WithApplication;

public class StudentHappinessTest extends WithApplication {

    Semester            semester;
    Allocation          allocation;
    Student             s1;
    Student             s2;
    Student             s3;
    Student             s4;
    Student             s5;
    Student             s6;
    Student             s7;
    Student             s8;
    Student             s9;
    Student             s10;
    Project             p1;
    Project             p2;
    Project             p3;
    List<LearningGroup> learningGroups;
    List<Team>          teams;

    @Before
    public void setup() throws DataException {
        semester = new Semester();
        semester.setMaxGroupSize(7);
        allocation = new Allocation();

        learningGroups = new ArrayList<LearningGroup>();
        teams = new ArrayList<Team>();

        s1 = new Student();
        semester.addStudent(s1);
        s1.save();
        s2 = new Student();
        semester.addStudent(s2);
        s2.save();
        s3 = new Student();
        semester.addStudent(s3);
        s3.save();
        s4 = new Student();
        semester.addStudent(s4);
        s4.save();
        s5 = new Student();
        semester.addStudent(s5);
        s5.save();
        s6 = new Student();
        semester.addStudent(s6);
        s6.save();
        s7 = new Student();
        semester.addStudent(s7);
        s7.save();
        s8 = new Student();
        semester.addStudent(s8);

        p1 = new Project("PSE", "test", "Informatik", "www.test.de");
        p1.save();
        p2 = new Project("PSE1", "test", "Informatik", "www.test.de");
        p2.save();
        p3 = new Project("PSE2", "test", "Informatik", "www.test.de");
        p3.save();

        allocation.save();
        semester.save();
    }

    @Test
    public void firstCalculateTest() throws DataException {
        LearningGroup l1 = new LearningGroup();
        l1.setSemester(semester);
        List<Student> members = new ArrayList<Student>();
        members.add(s1);
        members.add(s2);
        members.add(s3);
        members.add(s4);
        l1.setMembers(members);

        Rating r1 = new Rating(5, p1);
        List<Rating> ratings = new ArrayList<Rating>();
        ratings.add(r1);
        l1.setRatings(ratings);

        l1.save();

        LearningGroup l2 = new LearningGroup();
        l2.setSemester(semester);
        List<Student> members2 = new ArrayList<Student>();
        members2.add(s5);
        members2.add(s6);
        members2.add(s7);
        members2.add(s8);
        l2.setMembers(members2);
        l2.setRatings(ratings);
        l2.save();

        Team t1 = new Team();
        t1.addMember(s1);
        t1.addMember(s2);
        t1.addMember(s3);
        t1.addMember(s4);
        t1.setProject(p1);

        Team t2 = new Team();
        t2.addMember(s5);
        t2.addMember(s6);
        t2.addMember(s7);
        t2.addMember(s8);
        t2.setProject(p1);

        teams.add(t1);
        teams.add(t2);

        learningGroups.add(l1);
        learningGroups.add(l2);

        allocation.setTeams(teams);
        allocation.setSemester(semester);
        semester.setLearningGroups(learningGroups);
        allocation.save();
        semester.save();

        StudentHappiness sh = new StudentHappiness();
        String[] result = sh.calculate(allocation).split("%");
        assertTrue(100.0 - Double.parseDouble(result[0]) < 0.01);
    }

    @Test
    public void secondCalculateTest() throws DataException {
        Rating r1 = new Rating(5, p1);
        Rating r2 = new Rating(0, p1);
        List<Rating> ratings = new ArrayList<Rating>();
        ratings.add(r1);
        List<Rating> ratings2 = new ArrayList<Rating>();
        ratings2.add(r2);

        LearningGroup l1 = new LearningGroup();
        l1.setSemester(semester);
        List<Student> members = new ArrayList<Student>();
        members.add(s1);
        members.add(s2);
        members.add(s3);
        members.add(s4);
        l1.setMembers(members);

        l1.setRatings(ratings);

        l1.save();

        LearningGroup l2 = new LearningGroup();
        l2.setSemester(semester);
        List<Student> members2 = new ArrayList<Student>();
        members2.add(s5);
        members2.add(s6);
        members2.add(s7);
        members2.add(s8);
        l2.setMembers(members2);
        l2.setRatings(ratings2);
        l2.save();

        Team t1 = new Team();
        t1.addMember(s1);
        t1.addMember(s2);
        t1.addMember(s3);
        t1.addMember(s4);
        t1.setProject(p1);

        Team t2 = new Team();
        t2.addMember(s5);
        t2.addMember(s6);
        t2.addMember(s7);
        t2.addMember(s8);
        t2.setProject(p1);

        teams.add(t1);
        teams.add(t2);

        learningGroups.add(l1);
        learningGroups.add(l2);

        allocation.setTeams(teams);
        allocation.setSemester(semester);
        semester.setLearningGroups(learningGroups);
        allocation.save();
        semester.save();

        StudentHappiness sh = new StudentHappiness();
        String[] result = sh.calculate(allocation).split("%");
        assertTrue(50.0 - Double.parseDouble(result[0]) < 0.01);
    }
}
