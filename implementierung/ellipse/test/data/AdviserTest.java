package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import exception.DataException;

public class AdviserTest extends UserTest {

    @Before
    public void before() {
    }

    @Test
    public void testID() throws DataException {
        int id = 42;
        Adviser a = new Adviser();
        a.setId(id);
        assertEquals(id, a.getId());
    }

    @Test
    public void testUserName() throws DataException {
        String name = "Name";
        Adviser a = new Adviser();
        a.setUserName(name);
        assertEquals(name, a.getUserName());
    }

    @Test
    public void testFirstName() throws DataException {
        String name = "Name";
        Adviser a = new Adviser();
        a.setFirstName(name);
        assertEquals(name, a.getFirstName());
    }

    @Test
    public void testLastName() throws DataException {
        String name = "Name";
        Adviser a = new Adviser();
        a.setLastName(name);
        assertEquals(name, a.getLastName());
    }

    @Test
    public void testMailAdress() throws DataException {
        String name = "Name@elipse.de";
        Adviser a = new Adviser();
        a.setEmailAddress(name);
        assertEquals(name, a.getEmailAddress());
    }

    @Test
    public void testGetTeams() throws DataException {
        Project project = new Project();
        Project projectTwo = new Project();
        List<Adviser> advisers = new ArrayList<Adviser>();
        Adviser a = new Adviser();
        a.save();
        advisers.add(a);
        project.setAdvisers(advisers);
        project.save();
        projectTwo.setAdvisers(advisers);
        projectTwo.save();
        assertTrue(a.getProjects().contains(project));
        assertTrue(a.getProjects().contains(projectTwo));
        assertTrue(a.getProjects().size() == 2);
    }
}
