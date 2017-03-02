package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import data.Achievement;
import data.DataTest;
import data.GeneralData;
import data.SPO;
import data.Semester;
import play.data.DynamicForm;
import play.data.FormFactory;

@RunWith(MockitoJUnitRunner.class)
public class AdminPropertiesControllerTest extends DataTest {

    @Mock
    FormFactory               formFactory;

    @InjectMocks
    AdminPropertiesController controller;

    private DynamicForm       form;

    private Semester          semester;
    private SPO               firstSpo;
    private SPO               secondSpo;
    private Achievement       firstAchievement;
    private Achievement       secondAchievement;
    private Achievement       thirdAchievement;

    @Override
    @Before
    public void before() {
        super.before();

        semester = GeneralData.loadInstance().getCurrentSemester();

        firstAchievement = new Achievement();
        firstAchievement.save();

        firstAchievement.doTransaction(() -> {
            firstAchievement.setName("testName");
        });

        secondAchievement = new Achievement();
        secondAchievement.save();

        thirdAchievement = new Achievement();
        thirdAchievement.save();

        firstSpo = new SPO();
        firstSpo.save();

        firstSpo.doTransaction(() -> {
            firstSpo.setName("testName");
            firstSpo.addAdditionalAchievement(firstAchievement);
            firstSpo.addNecessaryAchievement(secondAchievement);
            firstSpo.addNecessaryAchievement(thirdAchievement);
        });

        secondSpo = new SPO();
        secondSpo.save();

        semester.doTransaction(() -> {
            semester.setInfoText("testInfo");
            semester.setMaxGroupSize(5);
            semester.setName("testName");
            semester.setRegistrationEnd(new Date(0));
            semester.setRegistrationStart(new Date(0));
            semester.setWintersemester(true);
            semester.addSPO(firstSpo);
        });

        form = Mockito.mock(DynamicForm.class);
        Mockito.when(formFactory.form()).thenReturn(form);
        Mockito.when(form.bindFromRequest()).thenReturn(form);

        semester.refresh();
        firstSpo.refresh();
        secondSpo.refresh();
        firstAchievement.refresh();
        secondAchievement.refresh();
        thirdAchievement.refresh();
    }

    @Test
    public void addAchievementTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("id")).thenReturn(
                String.valueOf(firstSpo.getId()));
        Mockito.when(form.get("nameAchiev")).thenReturn("achievementName");

        controller.addAchievement();

        firstSpo.refresh();

        assertEquals(firstSpo.getNecessaryAchievements().size(), 3);

        Achievement newAchievement = Achievement
                .getAchievement("achievementName");

        assertNotNull(newAchievement);
        assertTrue(firstSpo.getNecessaryAchievements().contains(newAchievement));
    }

    @Test
    public void addSemesterTest() {

        controller.addSemester();

        assertEquals(Semester.getSemesters().size(), 2);

        Semester newSemester = Semester.getSemester("newSemester");

        assertNotNull(newSemester);
        assertTrue(newSemester.isWintersemester());
    }

    @Test
    public void addSPOTest() {

        controller.addSPO();

        assertEquals(SPO.getSPOs().size(), 3);

        SPO newSpo = SPO.getSPO("newSPO");

        assertNotNull(newSpo);
    }

    // TODO Testdatenbank austauschen
    @Ignore
    @Test
    public void changeSPOTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("id")).thenReturn(
                String.valueOf(firstSpo.getId()));
        Mockito.when(form.get("nameSPO")).thenReturn("spoName");
        Mockito.when(
                form.get("necessary-"
                        + String.valueOf(firstAchievement.getId())))
                .thenReturn("NotNull");
        Mockito.when(
                form.get("necessary-"
                        + String.valueOf(secondAchievement.getId())))
                .thenReturn(null);
        Mockito.when(
                form.get("delete-" + String.valueOf(thirdAchievement.getId())))
                .thenReturn("NotNull");

        controller.changeSPO();

        firstSpo.refresh();

        assertEquals(firstSpo.getName(), "spoName");
        assertEquals(firstSpo.getAdditionalAchievements().size(), 1);
        assertEquals(firstSpo.getAdditionalAchievements().get(0),
                secondAchievement);
        assertEquals(firstSpo.getNecessaryAchievements().size(), 1);
        assertEquals(firstSpo.getNecessaryAchievements().get(0),
                firstAchievement);
    }

    @Test
    public void editSemesterTest() {

        Map<String, String> map = new HashMap<>();
        map.put("spo-multiselect-" + String.valueOf(semester.getId() + "1"),
                String.valueOf(secondSpo.getId()));

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("id")).thenReturn(
                String.valueOf(semester.getId()));
        Mockito.when(form.get("name2")).thenReturn("semesterName");
        Mockito.when(form.get("maxGroupSize")).thenReturn("6");
        Mockito.when(form.get("info")).thenReturn("semesterInfo");
        Mockito.when(form.get("registrationStart")).thenReturn(
                "01.01.1970 00:00:01");
        Mockito.when(form.get("registrationEnd")).thenReturn(
                "01.01.1970 00:00:01");
        Mockito.when(form.get("wintersemester")).thenReturn(null);
        Mockito.when(form.get("semester-active")).thenReturn("NotNull");

        controller.editSemester();

        semester.refresh();

        assertEquals(semester.getName(), "semesterName");
        assertEquals(semester.getMaxGroupSize(), 6);
        assertEquals(semester.getInfoText(), "semesterInfo");
        assertFalse(semester.getRegistrationStart().getTime() == 0);
        assertFalse(semester.getRegistrationEnd().getTime() == 0);
        assertTrue(!semester.isWintersemester());
        assertEquals(semester, GeneralData.loadInstance().getCurrentSemester());
        assertEquals(semester.getSpos().size(), 1);
        assertEquals(semester.getSpos().get(0), secondSpo);
    }
}
