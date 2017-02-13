package security;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import data.Adviser;
import data.DataTest;
import data.Student;

/**
 * Diese Klasse beinhaltet Unit-Tests für die Klasse PasswordResetter.
 */
public class PasswordResetterTest extends DataTest {

    Student student;
    Adviser adviser;

    /**
     * Setup code.
     */
    @Before
    public void setup() {
        student = new Student();
        adviser = new Adviser();
    }

    /**
     * Diese Methode testet, ob das resetten eines Studentenpassworts
     * funktioniert.
     */
    @Test
    public void testStudentReset() {
        String code = PasswordResetter.getInstance().initializeReset(student, "testtest");
        assertEquals(true, PasswordResetter.getInstance().finalizeReset(code));

        assertEquals(false, PasswordResetter.getInstance().finalizeReset(code));
    }

    /**
     * Diese Methode testet, ob das resetten eines Betreuerpassworts
     * funktioniert.
     */
    @Test
    public void testAdviserReset() {
        String code = PasswordResetter.getInstance().initializeReset(adviser, "testtest");
        assertEquals(true, PasswordResetter.getInstance().finalizeReset(code));

        assertEquals(false, PasswordResetter.getInstance().finalizeReset(code));
    }

}
