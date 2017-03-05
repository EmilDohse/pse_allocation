package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Tests für die Klasse Student.
 */
public class StudentTest extends DataTest {

    private Student student;

    /**
     * Initialisiereung des Studenten.
     */
    @Before
    public void beforeTest() {
        student = new Student();
    }

    /**
     * Diese Methode testet getter und setter für die Matrikelnummer des
     * Studenten.
     */
    @Test
    public void testMatriculationNumber() {
        int m = 1234567;
        student.setMatriculationNumber(m);
        assertEquals(m, student.getMatriculationNumber());
    }

    /**
     * Diese Methode testet getter und setter für die SPO des Studenten.
     */
    @Test
    public void testSPO() {
        SPO spo = new SPO();
        student.setSPO(spo);
        assertEquals(spo, student.getSPO());
    }

    /**
     * Diese Methode testet das Hinzufügen und Entfernen von bestandenen
     * Teilleistungen.
     */
    @Test
    public void testCompletedAchievements() {
        List<Achievement> achievements = new ArrayList<Achievement>();
        Achievement a = new Achievement();
        achievements.add(a);
        student.setCompletedAchievements(achievements);
        assertEquals(1, student.getCompletedAchievements().size());
        assertTrue(student.getCompletedAchievements().contains(a));
    }

    /**
     * Diese Methode testet das Hinzufügen und Entfernen von noch ausstehenden
     * Teilleistungen.
     */
    @Test
    public void testOralTestAchievements() {
        List<Achievement> achievements = new ArrayList<Achievement>();
        Achievement a = new Achievement();
        achievements.add(a);
        student.setOralTestAchievements(achievements);
        assertEquals(1, student.getOralTestAchievements().size());
        assertTrue(student.getOralTestAchievements().contains(a));
    }

    /**
     * Diese Methode testet getter und setter für den Status 'Bei PSE
     * registrert' auf Korrektheit.
     */
    @Test
    public void testRegisteredPSE() {
        boolean pse = true;
        student.setRegisteredPSE(pse);
        assertEquals(pse, student.isRegisteredPSE());
    }

    /**
     * Diese Methode testet getter und setter für den Status 'Bei TSE
     * registrert' auf Korrektheit.
     */
    @Test
    public void testRegisteredTSE() {
        boolean tse = true;
        student.setRegisteredTSE(tse);
        assertEquals(tse, student.isRegisteredTSE());
    }

    /**
     * Diese Methode testet getter und setter für die PSE-note auf Korrektheit.
     */
    @Test
    public void testGradePSE() {
        Grade pse = Grade.THREE_ZERO;
        student.setGradePSE(pse);
        assertEquals(pse, student.getGradePSE());
    }

    /**
     * Diese Methode testet getter und setter für die TSE-note auf Korrektheit.
     */
    @Test
    public void testGradeTSE() {
        Grade tse = Grade.THREE_ZERO;
        student.setGradeTSE(tse);
        assertEquals(tse, student.getGradeTSE());
    }

    /**
     * Diese Methode testet getter und setter für das Semseter des Studenten.
     */
    @Test
    public void testSemester() {
        int s = 3;
        student.setSemester(s);
        assertEquals(s, student.getSemester());
    }

    /**
     * Diese Methode testet getter und setter für den Status 'EMail verifiziert'
     * auf Korrektheit.
     */
    @Test
    public void testEmailVerified() {
        boolean e = true;
        student.setIsEmailVerified(e);
        assertEquals(e, student.isEmailVerified());
    }

    /**
     * Diese Methode testet ob die Methode 'registeredMoreThanOnce', welche
     * prüft, ob ein Student sich schonmal für das PSE angemeldet hatte.
     */
    @Test
    public void testRegisteredMoreThanOnce() {
        Semester firstS = new Semester();
        Semester secondS = new Semester();
        List<Student> empty = new ArrayList<Student>();
        List<Student> students = new ArrayList<Student>();
        students.add(student);
        firstS.setStudents(students);
        firstS.save();
        secondS.setStudents(empty);
        secondS.save();
        assertEquals(false, student.registeredMoreThanOnce());

        secondS.setStudents(students);
        secondS.save();

        assertEquals(true, student.registeredMoreThanOnce());
    }

    /**
     * Diese Methode testet die statischen Methoden, welche entweder eine Liste
     * aller Studenten zurückgeben, oder einen spezifischen Studenten aus der
     * Datenbank.
     */
    @Test
    public void testStudents() {
        Student.getStudents().forEach(s -> s.delete());
        Student one = new Student();
        Student two = new Student();
        one.doTransaction(() -> one.setMatriculationNumber(1));
        two.doTransaction(() -> two.setMatriculationNumber(2));

        assertEquals(2, Student.getStudents().size());
        assertEquals(one, Student.getStudent(1));
    }

    /**
     * Test für die toStringForNotification Methode.
     */
    @Test
    public void testToStringForNotification() {
        Student s = new Student();
        s.setFirstName("f");
        s.setLastName("l");
        s.setSemester(1);
        assertEquals(s.toStringForNotification(), "f l, 1");
    }

    /**
     * Test für die compareTo Methode.
     */
    @Test
    public void compareToTest() {

        Student s1 = new Student();
        s1.setFirstName("abc");
        s1.setLastName("abc");
        Student s2 = new Student();
        s2.setFirstName("abc");
        s2.setLastName("test");
        Student s3 = new Student();
        s3.setFirstName("test");
        s3.setLastName("abc");

        assertEquals(s1.compareTo(s1), 0);
        assertTrue(s1.compareTo(s2) < 0);
        assertTrue(s3.compareTo(s1) > 0);
    }
}
