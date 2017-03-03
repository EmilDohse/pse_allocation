package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import notificationSystem.Notifier;

import org.apache.commons.mail.EmailException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import play.mvc.Http.Context;
import security.BlowfishPasswordEncoder;
import security.UserManagement;
import data.Achievement;
import data.Administrator;
import data.Adviser;
import data.Allocation;
import data.GeneralData;
import data.LearningGroup;
import data.Project;
import data.SPO;
import data.Semester;
import data.Student;

public class GeneralAdminControllerTest extends ControllerTest {

    @Mock
    Notifier               notifier;

    @Mock
    UserManagement         userManagement;

    @InjectMocks
    GeneralAdminController controller;

    private Semester       semester;
    private Allocation     allocation;
    private Adviser        adviser;
    private Student        student;
    private SPO            spo;
    private Achievement    achievement;
    private Project        project;
    private Administrator  admin;

    @Override
    @Before
    public void before() {
        super.before();

        allocation = new Allocation();
        allocation.save();

        adviser = new Adviser();
        adviser.save();

        student = new Student();
        student.save();

        student.doTransaction(() -> {
            student.setMatriculationNumber(123456);
        });

        achievement = new Achievement();
        achievement.save();

        spo = new SPO();
        spo.save();

        spo.doTransaction(() -> {
            spo.addNecessaryAchievement(achievement);
        });

        project = new Project();
        project.save();

        semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addAllocation(allocation);
            semester.addStudent(student);
            semester.addSPO(spo);
            semester.addProject(project);
        });

        admin = new Administrator();
        admin.save();
        admin.doTransaction(() -> {
            admin.setEmailAddress("testemail");
            admin.setFirstName("testName");
            admin.setLastName("testName");
            admin.setPassword("password");
            admin.setUserName("admin");
        });

        semester.refresh();
        allocation.refresh();
        adviser.refresh();
        student.refresh();
        achievement.refresh();
        spo.refresh();
        project.refresh();
        admin.refresh();
    }

    @Test
    public void addAdviser() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("firstName")).thenReturn("firstName");
        Mockito.when(form.get("lastName")).thenReturn("lastName");
        Mockito.when(form.get("email")).thenReturn("e@mail");
        Mockito.when(form.get("password")).thenReturn("password");

        controller.addAdviser();

        try {
            Mockito.verify(notifier).sendAdviserPassword(
                    Mockito.any(Adviser.class), Mockito.any(String.class));
        } catch (EmailException e) {
            e.printStackTrace();
        }

        assertEquals(Adviser.getAdvisers().size(), 2);
        int temp = Adviser.getAdvisers().indexOf(adviser);
        Adviser newAdviser = Adviser.getAdvisers().get(1 - temp);
        assertNotNull(newAdviser);
        assertEquals(newAdviser.getEmailAddress(), "e@mail");
        assertEquals(newAdviser.getFirstName(), "firstName");
        assertEquals(newAdviser.getLastName(), "lastName");
        assertTrue((new BlowfishPasswordEncoder()).matches("password",
                newAdviser.getPassword()));
    }

    @Test
    public void addAllocationTest() {
        // TODO
    }

    @Test
    public void addStudentTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("firstName")).thenReturn("firstName");
        Mockito.when(form.get("lastName")).thenReturn("lastName");
        Mockito.when(form.get("email")).thenReturn("e@mail");
        Mockito.when(form.get("password")).thenReturn("password");
        Mockito.when(form.get("matrnr")).thenReturn("11");
        Mockito.when(form.get("semester")).thenReturn("1");
        Mockito.when(form.get("spo")).thenReturn(String.valueOf(spo.getId()));

        controller.addStudent();

        semester.refresh();

        assertEquals(semester.getStudents().size(), 2);
        assertEquals(semester.getLearningGroups().size(), 1);

        Student newStudent = Student.getStudent(11);

        assertNotNull(newStudent);
        assertEquals(newStudent.getEmailAddress(), "e@mail");
        assertEquals(newStudent.getFirstName(), "firstName");
        assertEquals(newStudent.getLastName(), "lastName");
        assertEquals(newStudent.getSemester(), 1);
        assertEquals(newStudent.getUserName(), "11");
        assertEquals(newStudent.getSPO(), spo);
        assertTrue((new BlowfishPasswordEncoder()).matches("password",
                newStudent.getPassword()));
        assertTrue(newStudent.getOralTestAchievements().isEmpty());
        assertEquals(newStudent.getCompletedAchievements().size(), 1);
        assertEquals(newStudent.getCompletedAchievements().get(0), achievement);

        LearningGroup lg = semester.getLearningGroupOf(newStudent);

        assertNotNull(lg);
        assertTrue(lg.isPrivate());
        assertEquals(lg.getMembers().size(), 1);
        assertEquals(lg.getRating(project), 3);
    }

    // TODO invalid salt version + flash()
    @Ignore
    @Test
    public void editAccountTest() {

        System.out.println("1");

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class)))
                .thenReturn(admin);

        Mockito.when(form.get("passwordChange")).thenReturn("NotNull");
        Mockito.when(form.get("oldPassword")).thenReturn("password");
        Mockito.when(form.get("newPassword")).thenReturn("password");
        Mockito.when(form.get("newPasswordRepeat")).thenReturn("password");

        controller.editAccount();

        admin.refresh();

        assertTrue((new BlowfishPasswordEncoder()).matches("password",
                admin.getPassword()));
    }

    @Test
    public void removeAdviserTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("id"))
                .thenReturn(String.valueOf(adviser.getId()));

        controller.removeAdviser();

        assertTrue(Adviser.getAdvisers().isEmpty());
    }

    @Test
    public void removeAllocationFromQueueTest() {
        // TODO
    }

    // TODO Testdatenbank austauschen
    @Ignore
    @Test
    public void removeStudentTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("matrnr2")).thenReturn("123456");

        controller.removeStudent();

        assertTrue(semester.getStudents().isEmpty());
        assertNull(Student.getStudent(123456));
    }
}
