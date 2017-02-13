package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Tests für die Klasse SMTPOption.
 */
public class SMTPOptionsTest extends DataTest {

    /**
     * Diese Methode testet sowohl den getter alsauch den setter des
     * SMTP-passworts auf Korrektheit.
     */
    @Test
    public void testPassword() {
        SMTPOptions options = SMTPOptions.getInstance();
        String password = "secret";
        options.savePassword(password);
        assertEquals(password, options.getPassword());
    }

}
