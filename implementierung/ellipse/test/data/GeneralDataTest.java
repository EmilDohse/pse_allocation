package data;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class GeneralDataTest extends DataTest {

    private GeneralData data;

    @Before
    public void before() {
        data = GeneralData.getInstance();
    }

    @Test
    public void testGetInstance() {
        assertEquals(data, GeneralData.getInstance());
    }

    @Test
    public void testSetSemester() {
        Semester semester = new Semester();
        data.setCurrentSemester(semester);
        assertEquals(semester, data.getCurrentSemester());
    }

    @Test
    public void testSetSaveAndReload() {
        Semester semester = new Semester();
        data.setCurrentSemester(semester);
        data.save();
        assertEquals(semester, GeneralData.getInstance().getCurrentSemester());
    }

}
