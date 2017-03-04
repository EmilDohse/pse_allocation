package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import data.Achievement;
import data.DataTest;
import play.data.DynamicForm;

public class MultiselectListTest extends DataTest {

    private DynamicForm form;

    @Before
    public void before() {
        super.before();

        form = Mockito.mock(DynamicForm.class);
    }

    @Test
    public void getValueArrayTest() {

        Map<String, String> map = new HashMap<>();
        map.put("test1", "a");
        map.put("test2", "b");
        map.put("abc", "c");

        Mockito.when(form.data()).thenReturn(map);

        String[] list = MultiselectList.getValueArray(form, "test");

        assertEquals(list.length, 2);
        assertTrue(list[0].equals("a") || list[1].equals("a"));
        assertTrue(list[0].equals("b") || list[1].equals("b"));
    }

    @Test
    public void createAchievementListTest() {

        Achievement a1 = new Achievement();
        a1.save();
        Achievement a2 = new Achievement();
        a2.save();

        Map<String, String> map = new HashMap<>();
        map.put("test1", String.valueOf(a1.getId()));
        map.put("test2", String.valueOf(a2.getId()));

        Mockito.when(form.data()).thenReturn(map);

        List<Achievement> list = MultiselectList.createAchievementList(form,
                "test");
        assertEquals(list.size(), 2);
        assertTrue(list.contains(a1));
        assertTrue(list.contains(a2));
    }
}
