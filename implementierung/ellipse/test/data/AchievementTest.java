package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Diese Klasse enthält Unit-Tests zur Klasse Achievement.
 */
public class AchievementTest extends DataTest {

    /**
     * Diese Methode testet den setter und getter für die ID des Achievements.
     */
    @Test
    public void testID() {
        int id = 42;
        Achievement a = new Achievement();
        a.doTransaction((() -> a.setId(id)));
        assertEquals(id, a.getId());
    }

    /**
     * Diese Methode testet den setter und getter für den Namen des
     * Achievements.
     */
    @Test
    public void testName() {
        String name = "Name";
        Achievement a = new Achievement();
        a.doTransaction(() -> {
            a.setName(name);
        });
        assertEquals(name, a.getName());
    }

    /**
     * Diese Methode testet die statische Methode, die ein spezifisches
     * Achievement aus der Datenbank zurückgibt. Außerdem wird die statisiche
     * Methode getestet, die alle Achievements aus der Datenbank zurückgibt.
     */
    @Test
    public void testGetAchievement() {
        // Leere die Datenbank
        Achievement.getAchievements().forEach(a -> a.delete());
        Achievement one = new Achievement();
        Achievement two = new Achievement();
        one.doTransaction(() -> {
            one.setName("one");
        });
        two.doTransaction(() -> {
            two.setName("two");
        });
        assertEquals(one, Achievement.getAchievement("one"));
        assertEquals(2, Achievement.getAchievements().size());
    }
}
