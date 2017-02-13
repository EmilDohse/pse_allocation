package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AdviserTest extends UserTest {

    @Before
    public void before() {
    }

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
        String name = "Name@elipse.de";
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

    @Test
    public void testGetAdvisers() {
        for (Project p : Project.getProjects()) {
            Iterator<Adviser> iter = p.getAdvisers().iterator();
            while (iter.hasNext()) {
                iter.next();
                iter.remove();
            }
            p.save();
            p.delete();
        }
        Adviser.getAdvisers().forEach(a -> a.delete());
        Adviser one = new Adviser();
        Adviser two = new Adviser();
        one.save();
        two.save();
        assertEquals(2, Adviser.getAdvisers().size());
    }
}
