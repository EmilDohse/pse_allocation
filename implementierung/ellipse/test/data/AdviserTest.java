package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse enthält Unit-Tests zur Klasse Adviser.
 */
public class AdviserTest extends DataTest {

    /**
     * Diese Methode testet sowohl den setter alsauch den getter für die ID des
     * Betreuers.
     */
    @Test
    public void testID() {
        int id = 42;
        Adviser a = new Adviser();
        a.setId(id);
        assertEquals(id, a.getId());
    }

    /**
     * Diese Methode testet sowohl den setter alsauch den getter für den
     * Benutzernamen des Betreuers.
     */
    @Test
    public void testUserName() {
        String name = "Name";
        Adviser a = new Adviser();
        a.setUserName(name);
        assertEquals(name, a.getUserName());
    }

    /**
     * Diese Methode testet sowohl den setter alsauch den getter für den
     * Vornamen des Betreuers.
     */
    @Test
    public void testFirstName() {
        String name = "Name";
        Adviser a = new Adviser();
        a.setFirstName(name);
        assertEquals(name, a.getFirstName());
    }

    /**
     * Diese Methode testet sowohl den setter alsauch den getter für den
     * Nachnamen des Betreuers.
     */
    @Test
    public void testLastName() {
        String name = "Name";
        Adviser a = new Adviser();
        a.setLastName(name);
        assertEquals(name, a.getLastName());
    }

    /**
     * Diese Methode testet sowohl den setter alsauch den getter für die
     * EMail-Adresse des Betreuers.
     */
    @Test
    public void testMailAdress() {
        String name = "Name@elipse.de";
        Adviser a = new Adviser();
        a.setEmailAddress(name);
        assertEquals(name, a.getEmailAddress());
    }

    /**
     * Diese Methode testet, ob man dem Betreuer erfolgreich Projekte zuordnen
     * kann.
     */
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

    /**
     * Diese Methode testet die statische Methode, die alle Betreuer aus der
     * Datenbank lädt.
     */
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

    /**
     * Test für die compareTo Methode.
     */
    @Test
    public void compareToTest() {

        Adviser a1 = new Adviser();
        a1.setFirstName("abc");
        a1.setLastName("abc");
        Adviser a2 = new Adviser();
        a2.setFirstName("abc");
        a2.setLastName("test");
        Adviser a3 = new Adviser();
        a3.setFirstName("test");
        a3.setLastName("abc");

        assertEquals(a1.compareTo(a1), 0);
        assertTrue(a1.compareTo(a2) < 0);
        assertTrue(a3.compareTo(a1) > 0);
    }
}
