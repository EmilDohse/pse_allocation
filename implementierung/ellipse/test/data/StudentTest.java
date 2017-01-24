package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StudentTest extends UserTest {

    private Student student;

    @Before
    public void beforeTest() {
        student = new Student();
        user = student;
    }

    @Test
    public void testMatriculationNumber() {
        int m = 1234567;
        student.setMatriculationNumber(m);
        assertEquals(m, student.getMatriculationNumber());
    }

    @Test
    public void testSPO() {
        SPO spo = new SPO();
        student.setSPO(spo);
        assertEquals(spo, student.getSPO());
    }

    @Test
    public void testCompletedAchievements() {
        List<Achievement> achievements = new ArrayList<Achievement>();
        Achievement a = new Achievement();
        achievements.add(a);
        student.setCompletedAchievements(achievements);
        assertEquals(student.getCompletedAchievements().size(), 1);
        assertTrue(student.getCompletedAchievements().contains(a));
    }

    @Test
    public void testOralTestAchievements() {
        List<Achievement> achievements = new ArrayList<Achievement>();
        Achievement a = new Achievement();
        achievements.add(a);
        student.setOralTestAchievements(achievements);
        assertEquals(student.getOralTestAchievements().size(), 1);
        assertTrue(student.getOralTestAchievements().contains(a));
    }

    @Test
    public void testRegisteredPSE() {
        boolean pse = true;
        student.setRegisteredPSE(pse);
        assertEquals(pse, student.isRegisteredPSE());
    }

    @Test
    public void testRegisteredTSE() {
        boolean tse = true;
        student.setRegisteredTSE(tse);
        assertEquals(tse, student.isRegisteredTSE());
    }

    @Test
    public void testGradePSE() {
        Grade pse = Grade.THREE_ZERO;
        student.setGradePSE(pse);
        assertEquals(pse, student.getGradePSE());
    }

    @Test
    public void testGradeTSE() {
        Grade tse = Grade.THREE_ZERO;
        student.setGradeTSE(tse);
        assertEquals(tse, student.getGradeTSE());
    }

    @Test
    public void testSemester() {
        int s = 3;
        student.setSemester(s);
        assertEquals(s, student.getSemester());
    }

    @Test
    public void testEmailVerified() {
        boolean e = true;
        student.setIsEmailVerified(e);
        assertEquals(e, student.isEmailVerified());
    }

    @Test
    public void testGetRating() {
        int r = 11;
        Project p = new Project();
        List<Rating> ratings = new ArrayList<Rating>();
        Rating rating = new Rating();
        rating.setRating(r);
        rating.setProject(p);
        ratings.add(rating);
        LearningGroup l = new LearningGroup();
        l.setRatings(ratings);
        List<Student> students = new ArrayList<Student>();
        students.add(student);
        l.setMembers(students);
        assertEquals(r, student.getRating(p));
    }

    @Test
    public void testGetCurrentProject() {
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
        GeneralData.setCurrentSemester(s);
        assertEquals(p, student.getCurrentProject());
    }

    @Test
    public void testGetCurrentTeam() {
        Semester s = new Semester();
        Allocation a = new Allocation();
        Team t = new Team();
        List<Student> students = new ArrayList<Student>();
        students.add(student);
        t.setMembers(students);
        List<Team> teams = new ArrayList<Team>();
        teams.add(t);
        a.setTeams(teams);
        s.setFinalAllocation(a);
        GeneralData.setCurrentSemester(s);
        assertEquals(t, student.getCurrentTeam());
    }

    @Test
    public void testGetCurrentLearningGroup() {
        Semester s = new Semester();
        LearningGroup l = new LearningGroup();
        List<Student> students = new ArrayList<Student>();
        students.add(student);
        l.setMembers(students);
        List<LearningGroup> learningGroups = new ArrayList<LearningGroup>();
        learningGroups.add(l);
        s.setLearningGroups(learningGroups);
        GeneralData.setCurrentSemester(s);
        assertEquals(l, student.getCurrentLearningGroup());
    }

    @Test
    public void testGetLearningGroup() {
        Semester s = new Semester();
        LearningGroup l = new LearningGroup();
        List<Student> students = new ArrayList<Student>();
        students.add(student);
        l.setMembers(students);
        List<LearningGroup> learningGroups = new ArrayList<LearningGroup>();
        learningGroups.add(l);
        s.setLearningGroups(learningGroups);
        assertEquals(l, student.getLearningGroup(s));
    }

    @Test
    public void testRegisteredMoreThanOnce() {
        Semester firstS = new Semester();
        Semester secondS = new Semester();
        List<Student> empty = new ArrayList<Student>();
        List<Student> students = new ArrayList<Student>();
        students.add(student);
        firstS.setStudents(students);
        secondS.setStudents(empty);
        assertEquals(student.registeredMoreThanOnce(), false);

        secondS.setStudents(students);
        assertEquals(student.registeredMoreThanOnce(), true);
    }
}
