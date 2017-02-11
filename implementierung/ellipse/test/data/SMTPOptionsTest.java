package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SMTPOptionsTest extends DataTest {

    @Test
    public void testPassword() {
        SMTPOptions options = SMTPOptions.getInstance();
        String password = "secret";
        options.savePassword(password);
        assertEquals(password, options.getPassword());
    }

}
