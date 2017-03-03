package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import data.SMTPOptions;
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

    private Semester          firstSemester;
    private Semester          secondSemester;
    private SPO               firstSpo;
    private SPO               secondSpo;
    private Achievement       firstAchievement;
    private Achievement       secondAchievement;
    private Achievement       thirdAchievement;

    @Override
    @Before
    public void before() {
        super.before();

        firstSemester = GeneralData.loadInstance().getCurrentSemester();

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

        secondSpo = new SPO("deleteSPO");
        secondSpo.save();

        firstSemester.doTransaction(() -> {
            firstSemester.setInfoText("testInfo");
            firstSemester.setMaxGroupSize(5);
            firstSemester.setName("testName");
            firstSemester.setRegistrationEnd(new Date(0));
            firstSemester.setRegistrationStart(new Date(0));
            firstSemester.setWintersemester(true);
            firstSemester.addSPO(firstSpo);
        });

        secondSemester = new Semester("deleteSemester", true);
        secondSemester.save();

        form = Mockito.mock(DynamicForm.class);
        Mockito.when(formFactory.form()).thenReturn(form);
        Mockito.when(form.bindFromRequest()).thenReturn(form);

        firstSemester.refresh();
        secondSemester.refresh();
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

        assertEquals(Semester.getSemesters().size(), 3);

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
        map.put("spo-multiselect-"
                + String.valueOf(firstSemester.getId() + "1"),
                String.valueOf(secondSpo.getId()));

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("id")).thenReturn(
                String.valueOf(firstSemester.getId()));
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

        firstSemester.refresh();

        assertEquals(firstSemester.getName(), "semesterName");
        assertEquals(firstSemester.getMaxGroupSize(), 6);
        assertEquals(firstSemester.getInfoText(), "semesterInfo");
        assertFalse(firstSemester.getRegistrationStart().getTime() == 0);
        assertFalse(firstSemester.getRegistrationEnd().getTime() == 0);
        assertTrue(!firstSemester.isWintersemester());
        assertEquals(firstSemester, GeneralData.loadInstance()
                .getCurrentSemester());
        assertEquals(firstSemester.getSpos().size(), 1);
        assertEquals(firstSemester.getSpos().get(0), secondSpo);
    }

    @Test
    public void editSMTPOptionsTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("ssl")).thenReturn("NotNull");
        Mockito.when(form.get("tls")).thenReturn(null);
        Mockito.when(form.get("debug")).thenReturn("NotNull");
        Mockito.when(form.get("port")).thenReturn("5000");
        Mockito.when(form.get("connectionTimeOut")).thenReturn("1000");
        Mockito.when(form.get("timeout")).thenReturn("500");
        Mockito.when(form.get("host")).thenReturn("testHost");
        Mockito.when(form.get("mail")).thenReturn("testMail");
        Mockito.when(form.get("username")).thenReturn("testName");
        Mockito.when(form.get("password")).thenReturn("password");

        controller.editSMTPOptions();

        SMTPOptions options = SMTPOptions.getInstance();

        assertEquals(options.getSsl(), true);
        assertEquals(options.getTls(), false);
        assertEquals(options.getDebug(), true);
        assertEquals(options.getPort(), 5000);
        assertEquals(options.getConnectionTimeout(), 1000);
        assertEquals(options.getTimeout(), 500);
        assertEquals(options.getHost(), "testHost");
        assertEquals(options.getMailFrom(), "testMail");
        assertEquals(options.getUsername(), "testName");
        assertEquals(options.getPassword(), "password");
    }

    @Test
    public void removeSemesterTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("id")).thenReturn(
                String.valueOf(secondSemester.getId()));

        controller.removeSemester();

        assertEquals(Semester.getSemesters().size(), 1);
        assertNull(Semester.getSemester("deleteSemester"));
    }

    @Test
    public void removeSPOTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("id")).thenReturn(
                String.valueOf(secondSpo.getId()));

        controller.removeSPO();

        assertEquals(SPO.getSPOs().size(), 1);
        assertNull(SPO.getSPO("deleteSPO"));
    }
}
