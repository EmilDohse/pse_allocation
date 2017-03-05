package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import allocation.AllocationQueue;
import allocation.Configuration;
import allocation.Criterion;
import data.Achievement;
import data.Administrator;
import data.Adviser;
import data.Allocation;
import data.AllocationParameter;
import data.GeneralData;
import data.LearningGroup;
import data.Project;
import data.SPO;
import data.Semester;
import data.Student;
import notificationSystem.Notifier;
import play.mvc.Http.Context;
import security.BlowfishPasswordEncoder;
import security.UserManagement;

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
            admin.setPassword(
                    (new BlowfishPasswordEncoder()).encode("password"));
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

        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn("firstName");
        when(form.get("lastName")).thenReturn("lastName");
        when(form.get("email")).thenReturn("e@mail");
        when(form.get("password")).thenReturn("password");

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
        assertEquals("e@mail", newAdviser.getEmailAddress());
        assertEquals("firstName", newAdviser.getFirstName());
        assertEquals("lastName", newAdviser.getLastName());
        assertTrue((new BlowfishPasswordEncoder()).matches("password",
                newAdviser.getPassword()));
    }

    @Test
    public void testValidationExceptionInAddAdviser() {
        Map<String, String> data = new HashMap<>();
        data.put("1", "1");

        when(form.data()).thenReturn(data);
        when(form.get("firstName")).thenReturn(new String());
        when(messages.at("general.error.noEmptyString"))
                .thenReturn("Empty String");

        controller.addAdviser();

        assertTrue(Context.current().flash().containsValue("Empty String"));
    }

    @Test
    public void removeAdviserTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        when(form.data()).thenReturn(map);
        when(form.get("id")).thenReturn(String.valueOf(adviser.getId()));

        controller.removeAdviser();

        assertTrue(Adviser.getAdvisers().isEmpty());
    }

    @Test
    public void addAllocationTest() throws InterruptedException {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("name")).thenReturn("Test");
        when(form.get("preferedTeamSize")).thenReturn("3");
        when(form.get("minTeamSize")).thenReturn("1");
        when(form.get("maxTeamSize")).thenReturn("3");

        for (Criterion criterion : AllocationQueue.getInstance().getAllocator()
                .getAllCriteria()) {
            when(form.get(criterion.getName())).thenReturn("1");
        }

        controller.addAllocation();

        assertEquals(1, AllocationQueue.getInstance().getQueue().size());

        // TODO bessere Lösung finden
        Thread.sleep(100);
    }

    @Test
    public void testValidationExceptionInAllocation() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("name")).thenReturn(new String());
        when(messages.at("general.error.noEmptyString"))
                .thenReturn("Validation Exception");

        controller.addAllocation();

        assertTrue(Context.current().flash()
                .containsValue("Validation Exception"));
    }

    @Test
    public void testInvalidAdminParameter() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("name")).thenReturn("Test");
        when(form.get("preferedTeamSize")).thenReturn("3");
        when(form.get("minTeamSize")).thenReturn("2");
        when(form.get("maxTeamSize")).thenReturn("1");

        when(messages.at("admin.allocation.error.generalError"))
                .thenReturn("Wrong Input");

        controller.addAllocation();

        assertTrue(Context.current().flash().containsValue("Wrong Input"));
    }

    @Test
    public void testInvalidCriterionParameter() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("name")).thenReturn("Test");
        when(form.get("preferedTeamSize")).thenReturn("3");
        when(form.get("minTeamSize")).thenReturn("1");
        when(form.get("maxTeamSize")).thenReturn("3");

        for (Criterion criterion : AllocationQueue.getInstance().getAllocator()
                .getAllCriteria()) {
            when(form.get(criterion.getName())).thenReturn("1");
        }
        when(form.get("Allocated")).thenReturn("a");
        when(messages.at("error.internalError")).thenReturn("Criterion Error");

        controller.addAllocation();

        assertTrue(Context.current().flash().containsValue("Criterion Error"));
    }

    @Test
    public void removeAllocationFromQueueTest() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        Student firstStudent = new Student();
        firstStudent.save();
        Student secondStudent = new Student();
        secondStudent.save();
        Project project = new Project();
        project.doTransaction(() -> {
            project.setNumberOfTeams(1);
        });
        LearningGroup group = new LearningGroup();
        group.doTransaction(() -> {
            group.addMember(firstStudent);
            group.addMember(secondStudent);
        });

        ArrayList<Student> students = new ArrayList<>();
        students.add(firstStudent);
        students.add(secondStudent);
        ArrayList<Project> projects = new ArrayList<>();
        projects.add(project);
        ArrayList<LearningGroup> lgs = new ArrayList<>();
        lgs.add(group);

        AllocationQueue allocQueue = AllocationQueue.getInstance();
        AllocationParameter paramOne = new AllocationParameter("minSize", 1);
        AllocationParameter paramTwo = new AllocationParameter("maxSize", 2);
        List<AllocationParameter> paramList = new ArrayList<>();
        paramList.add(paramOne);
        paramList.add(paramTwo);
        Configuration config = new Configuration("test", students, lgs,
                projects, paramList);

        when(form.data()).thenReturn(data);
        when(form.get("queue")).thenReturn("test");

        allocQueue.addToQueue(config);

        controller.removeAllocationFromQueue();

        assertTrue(allocQueue.getQueue().isEmpty());
        System.out.println();
        System.out.println(Allocation.getAllocations().get(0).getName());
        System.out.println();
    }

    @Test
    public void addStudentTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn("firstName");
        when(form.get("lastName")).thenReturn("lastName");
        when(form.get("email")).thenReturn("e@mail");
        when(form.get("password")).thenReturn("password");
        when(form.get("matrnr")).thenReturn("11");
        when(form.get("semester")).thenReturn("1");
        when(form.get("spo")).thenReturn(String.valueOf(spo.getId()));

        controller.addStudent();

        semester.refresh();

        assertEquals(2, semester.getStudents().size());
        assertEquals(1, semester.getLearningGroups().size());

        Student newStudent = Student.getStudent(11);

        assertNotNull(newStudent);
        assertEquals("e@mail", newStudent.getEmailAddress());
        assertEquals("firstName", newStudent.getFirstName());
        assertEquals("lastName", newStudent.getLastName());
        assertEquals(1, newStudent.getSemester());
        assertEquals("11", newStudent.getUserName());
        assertEquals(spo, newStudent.getSPO());
        assertTrue((new BlowfishPasswordEncoder()).matches("password",
                newStudent.getPassword()));
        assertTrue(newStudent.getOralTestAchievements().isEmpty());
        assertEquals(1, newStudent.getCompletedAchievements().size());
        assertEquals(achievement, newStudent.getCompletedAchievements().get(0));

        LearningGroup lg = semester.getLearningGroupOf(newStudent);

        assertNotNull(lg);
        assertTrue(lg.isPrivate());
        assertEquals(1, lg.getMembers().size());
        assertEquals(3, lg.getRating(project));
    }

    // TODO Testdatenbank austauschen
    @Ignore
    @Test
    public void removeStudentTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        when(form.data()).thenReturn(map);
        when(form.get("matrnr2")).thenReturn("123456");

        controller.removeStudent();

        assertTrue(semester.getStudents().isEmpty());
        assertNull(Student.getStudent(123456));
    }

    // TODO flash()
    @Ignore
    @Test
    public void editAccountTest() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        when(form.data()).thenReturn(map);

        when(userManagement.getUserProfile(Mockito.any(Context.class)))
                .thenReturn(admin);

        when(form.get("passwordChange")).thenReturn("NotNull");
        when(form.get("oldPassword")).thenReturn("password");
        when(form.get("newPassword")).thenReturn("newpassword");
        when(form.get("newPasswordRepeat")).thenReturn("newpassword");

        controller.editAccount();

        admin.refresh();

        assertTrue((new BlowfishPasswordEncoder()).matches("newpassword",
                admin.getPassword()));
    }

}
