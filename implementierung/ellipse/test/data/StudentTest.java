package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import exception.DataException;

public class StudentTest extends UserTest {

    private Student student;

    @Before
    public void beforeTest() throws DataException {
        student = new Student();
    }

    @Test
    public void testMatriculationNumber() throws DataException {
        int m = 1234567;
        student.setMatriculationNumber(m);
        assertEquals(m, student.getMatriculationNumber());
    }

    @Test
    public void testSPO() throws DataException {
        SPO spo = new SPO();
        student.setSPO(spo);
        assertEquals(spo, student.getSPO());
    }

    @Test
    public void testCompletedAchievements() throws DataException {
        List<Achievement> achievements = new ArrayList<Achievement>();
        Achievement a = new Achievement();
        achievements.add(a);
        student.setCompletedAchievements(achievements);
        assertEquals(1, student.getCompletedAchievements().size());
        assertTrue(student.getCompletedAchievements().contains(a));
    }

    @Test
    public void testOralTestAchievements() throws DataException {
        List<Achievement> achievements = new ArrayList<Achievement>();
        Achievement a = new Achievement();
        achievements.add(a);
        student.setOralTestAchievements(achievements);
        assertEquals(1, student.getOralTestAchievements().size());
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
    public void testGradePSE() throws DataException {
        Grade pse = Grade.THREE_ZERO;
        student.setGradePSE(pse);
        assertEquals(pse, student.getGradePSE());
    }

    @Test
    public void testGradeTSE() throws DataException {
        Grade tse = Grade.THREE_ZERO;
        student.setGradeTSE(tse);
        assertEquals(tse, student.getGradeTSE());
    }

    @Test
    public void testSemester() throws DataException {
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
    public void testRegisteredMoreThanOnce() throws DataException {
        Semester firstS = new Semester();
        Semester secondS = new Semester();
        List<Student> empty = new ArrayList<Student>();
        List<Student> students = new ArrayList<Student>();
        students.add(student);
        firstS.setStudents(students);
        firstS.save();
        try {
            secondS.setStudents(empty);
        } catch (DataException e) {
            // Mit absicht leer, da die Exception durch die leere Liste geworfen
            // wird. Hier ist das aber nützlich
        }
        secondS.save();
        assertEquals(false, student.registeredMoreThanOnce());

        secondS.setStudents(students);
        secondS.save();

        assertEquals(true, student.registeredMoreThanOnce());
    }
}
