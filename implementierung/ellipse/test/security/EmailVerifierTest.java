package security;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import data.Student;

/**
 * Diese Klasse beinhaltet Unit-Test für die Klasse EmailVerifier.
 */
public class EmailVerifierTest {

    Student student;
    Student student2;

    /**
     * Setup code.
     */
    @Before
    public void setup() {
        student = new Student();
        student2 = new Student();
    }

    /**
     * Diese Methode testet, ob der Verifikationscode eines Studenten
     * funktioniert.
     */
    @Test
    public void testVerify() {
        String code = EmailVerifier.getInstance().getVerificationCode(student);
        assertEquals(true, EmailVerifier.getInstance().verify(code));

        assertEquals(false, EmailVerifier.getInstance().verify(code));

        assertEquals(false, EmailVerifier.getInstance().verify(null));
    }
}
