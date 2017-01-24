package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UserTest extends DataTest {

    protected User user;

    @Test
    public void testUsername() {
        String username = "testname";
        user.setUserName(username);
        assertEquals(username, user.getUserName());
    }

    @Test
    public void testPassword() {
        String password = "123456";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testEmailAddress() {
        String email = "student@email.de";
        user.setEmailAddress(email);
        assertEquals(email, user.getEmailAddress());
    }

    @Test
    public void testFirstName() {
        String name = "Testname";
        user.setFirstName(name);
        assertEquals(name, user.getFirstName());
    }

    @Test
    public void testLastName() {
        String name = "Testname";
        user.setLastName(name);
        assertEquals(name, user.getLastName());
    }
}
