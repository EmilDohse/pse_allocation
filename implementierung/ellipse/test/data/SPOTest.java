package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Tests für die Klasse SPO.
 */
public class SPOTest extends DataTest {

    private SPO spo;

    /**
     * Initialisierung der SPO.
     */
    @Before
    public void beforeTest() {
        spo = new SPO();
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter des Namens der SPO.
     */
    @Test
    public void testName() {
        String n = "testname";
        spo.setName(n);
        assertEquals(n, spo.getName());
    }

    /**
     * Diese Methode testet das Hinzufügen und Entfernen von benötigten
     * Teilleistungen.
     */
    @Test
    public void testNecessaryAchievements() {
        Achievement firstA = new Achievement();
        Achievement secondA = new Achievement();
        List<Achievement> a = new ArrayList<Achievement>();
        a.add(firstA);
        spo.setNecessaryAchievements(a);
        assertEquals(spo.getNecessaryAchievements().size(), 1);
        assertTrue(spo.getNecessaryAchievements().contains(firstA));

        spo.addNecessaryAchievement(secondA);
        assertEquals(spo.getNecessaryAchievements().size(), 2);
        assertTrue(spo.getNecessaryAchievements().contains(firstA));
        assertTrue(spo.getNecessaryAchievements().contains(secondA));

        spo.removeNecessaryAchievement(firstA);
        assertEquals(spo.getNecessaryAchievements().size(), 1);
        assertTrue(spo.getNecessaryAchievements().contains(secondA));
    }

    /**
     * Diese Methode testet das Hinzufügen und Entfernen von zusätzlichen
     * Teilleistungen.
     */
    @Test
    public void testAdditionalAchievements() {
        Achievement firstA = new Achievement();
        Achievement secondA = new Achievement();
        List<Achievement> a = new ArrayList<Achievement>();
        a.add(firstA);
        spo.setAdditionalAchievements(a);
        assertEquals(spo.getAdditionalAchievements().size(), 1);
        assertTrue(spo.getAdditionalAchievements().contains(firstA));

        spo.addAdditionalAchievement(secondA);
        assertEquals(spo.getAdditionalAchievements().size(), 2);
        assertTrue(spo.getAdditionalAchievements().contains(firstA));
        assertTrue(spo.getAdditionalAchievements().contains(secondA));

        spo.removeAdditionalAchievement(firstA);
        assertEquals(spo.getAdditionalAchievements().size(), 1);
        assertTrue(spo.getAdditionalAchievements().contains(secondA));
    }

    /**
     * Diese Methode testet die statischen Methoden, mit denen man entweder eine
     * Liste aller SPOs aus der Datenbank bekommt, oder eine Spezifische SPO.
     */
    @Test
    public void testGetSPO() {
        SPO one = new SPO("one");
        SPO two = new SPO("two");
        one.save();
        two.save();
        assertEquals(2, SPO.getSPOs().size());
        assertEquals(one, SPO.getSPO("one"));
    }

}
