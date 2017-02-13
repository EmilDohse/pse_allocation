package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AchievementTest extends DataTest {

    @Test
    public void testID() {
        int id = 42;
        Achievement a = new Achievement();
        a.doTransaction((() -> a.setId(id)));
        a.setId(id);
        assertEquals(id, a.getId());
    }

    @Test
    public void testName() {
        String name = "Name";
        Achievement a = new Achievement();
        a.doTransaction(() -> {
            a.setName(name);
        });
        assertEquals(name, a.getName());
    }

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
