package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AdviserTest extends DataTest {

    @Test
    public void testID() {
        int id = 42;
        Adviser a = new Adviser();
        a.setId(id);
        assertEquals(id, a.getId());
    }

    @Test
    public void testUserName() {
        String name = "Name";
        Adviser a = new Adviser();
        a.setUserName(name);
        assertEquals(name, a.getUserName());
    }

    @Test
    public void testFirstName() {
        String name = "Name";
        Adviser a = new Adviser();
        a.setFirstName(name);
        assertEquals(name, a.getFirstName());
    }

    @Test
    public void testLastName() {
        String name = "Name";
        Adviser a = new Adviser();
        a.setLastName(name);
        assertEquals(name, a.getLastName());
    }

    @Test
    public void testMailAdress() {
        String name = "Name";
        Adviser a = new Adviser();
        a.setEmailAddress(name);
        assertEquals(name, a.getEmailAddress());
    }

    @Test
    public void testGetTeams() {
        Project project = new Project();
        Project projectTwo = new Project();
        List<Adviser> advisers = new ArrayList<Adviser>();
        Adviser a = new Adviser();
        advisers.add(a);
        project.setAdvisers(advisers);
        projectTwo.setAdvisers(advisers);
        assertTrue(a.getProjects().contains(project));
        assertTrue(a.getProjects().contains(projectTwo));
        assertTrue(a.getProjects().size() == 2);
    }
}
