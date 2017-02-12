package security;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import data.Adviser;
import data.Student;
import exception.DataException;
import play.test.WithApplication;

public class PasswordResetterTest extends WithApplication {

    Student student;
    Adviser adviser;

    @Before
    public void setup() throws DataException {
        student = new Student();
        adviser = new Adviser();
    }

    @Test
    public void testStudentReset() {
        String code = PasswordResetter.getInstance().initializeReset(student, "testtest");
        assertEquals(true, PasswordResetter.getInstance().finalizeReset(code));

        String code1 = PasswordResetter.getInstance().initializeReset(student, "testtest");
        assertEquals(false, PasswordResetter.getInstance().finalizeReset(code));
    }

    @Test
    public void testAdviserReset() {
        String code = PasswordResetter.getInstance().initializeReset(adviser, "testtest");
        assertEquals(true, PasswordResetter.getInstance().finalizeReset(code));

        String code1 = PasswordResetter.getInstance().initializeReset(adviser, "testtest");
        assertEquals(false, PasswordResetter.getInstance().finalizeReset(code));
    }

}
