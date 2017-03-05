package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.mail.EmailException;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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

    @Test
    public void addAdviser() throws EmailException {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn("firstName");
        when(form.get("lastName")).thenReturn("lastName");
        when(form.get("email")).thenReturn("e@mail");
        when(form.get("password")).thenReturn("password");

        controller.addAdviser();

        verify(notifier).sendAdviserPassword(any(Adviser.class),
                any(String.class));

        assertEquals(Adviser.getAdvisers().size(), 1);
        Adviser adviser = Adviser.getAdvisers().get(0);
        assertNotNull(adviser);
        assertEquals("e@mail", adviser.getEmailAddress());
        assertEquals("firstName", adviser.getFirstName());
        assertEquals("lastName", adviser.getLastName());
        assertTrue((new BlowfishPasswordEncoder()).matches("password",
                adviser.getPassword()));
    }

    @Test
    public void testValidationExceptionInAddAdviser() {
        Map<String, String> data = new HashMap<>();
        data.put("1", "1");

        when(form.data()).thenReturn(data);
        when(form.get("firstName")).thenReturn(new String());
        when(messages.at("general.error.noEmptyString")).thenReturn(
                "Empty String");

        controller.addAdviser();

        assertTrue(Context.current().flash().containsValue("Empty String"));
    }

    @Test
    public void removeAdviserTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Adviser adviser = new Adviser();
        adviser.save();

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

        while (!AllocationQueue.getInstance().getQueue().isEmpty()) {
            // warte darauf, dass die Queue leer ist
        }
    }

    @Test
    public void testValidationExceptionInAllocation() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("name")).thenReturn(new String());
        when(messages.at("general.error.noEmptyString")).thenReturn(
                "Validation Exception");

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

        when(messages.at("admin.allocation.error.generalError")).thenReturn(
                "Wrong Input");

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

        ArrayList<Student> students = new ArrayList<>();
        ArrayList<LearningGroup> lgs = new ArrayList<>();

        IntStream.rangeClosed(1, 15).forEach((number) -> {
            Student student = new Student();
            student.save();
            students.add(student);
            LearningGroup lg = new LearningGroup();
            lg.doTransaction(() -> {
                lg.addMember(student);
            });
            lgs.add(lg);
        });
        Project project = new Project();
        project.doTransaction(() -> {
            project.setNumberOfTeams(3);
        });

        ArrayList<Project> projects = new ArrayList<>();
        projects.add(project);

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
        assertTrue(Allocation.getAllocations().isEmpty());
    }

    @Test
    public void addStudentTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        SPO spo = new SPO();
        Achievement achievement = new Achievement();
        achievement.save();
        spo.doTransaction(() -> {
            spo.addNecessaryAchievement(achievement);
        });
        Project project = new Project();
        Semester current = GeneralData.loadInstance().getCurrentSemester();
        current.doTransaction(() -> {
            current.addProject(project);
        });

        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn("firstName");
        when(form.get("lastName")).thenReturn("lastName");
        when(form.get("email")).thenReturn("e@mail");
        when(form.get("password")).thenReturn("password");
        when(form.get("matrnr")).thenReturn("11");
        when(form.get("semester")).thenReturn("1");
        when(form.get("spo")).thenReturn(String.valueOf(spo.getId()));

        controller.addStudent();

        assertEquals(1, current.getStudents().size());
        assertEquals(1, current.getLearningGroups().size());

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

        LearningGroup lg = current.getLearningGroupOf(newStudent);

        assertNotNull(lg);
        assertTrue(lg.isPrivate());
        assertEquals(1, lg.getMembers().size());
        assertEquals(3, lg.getRating(project));
    }

    @Test
    public void testValidationExceptionInStudent() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("firstName")).thenReturn(new String());
        when(messages.at("general.error.noEmptyString")).thenReturn(
                "Validation Exception");

        controller.addStudent();

        assertTrue(Context.current().flash()
                .containsValue("Validation Exception"));
    }

    @Test
    public void testAddAlreadyExistingStudent() {
        Student student = new Student();
        student.doTransaction(() -> {
            student.setMatriculationNumber(1);
        });

        SPO spo = new SPO();
        Achievement achievement = new Achievement();
        achievement.save();
        spo.doTransaction(() -> {
            spo.addNecessaryAchievement(achievement);
        });

        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("firstName")).thenReturn("firstName");
        when(form.get("lastName")).thenReturn("lastName");
        when(form.get("email")).thenReturn("e@mail");
        when(form.get("password")).thenReturn("password");
        when(form.get("matrnr")).thenReturn("1");
        when(form.get("semester")).thenReturn("1");
        when(form.get("spo")).thenReturn(String.valueOf(spo.getId()));
        when(messages.at("admin.studentEdit.matrNrExistsError")).thenReturn(
                "already existing");

        controller.addStudent();

        assertTrue(Context.current().flash().containsValue("already existing"));
    }

    // TODO Testdatenbank austauschen
    @Test
    @Ignore
    public void removeStudentTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Student student = new Student();
        student.doTransaction(() -> {
            student.setMatriculationNumber(1);
        });
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addStudent(student);
        });

        when(form.data()).thenReturn(map);
        when(form.get("matrnr2")).thenReturn("1");

        controller.removeStudent();

        assertTrue(GeneralData.loadInstance().getCurrentSemester()
                .getStudents().isEmpty());
        assertNull(Student.getStudent(1));
    }

    @Test
    public void testNFEinRemoveStudent() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("matrnr2")).thenReturn("a");
        when(messages.at("error.internalError")).thenReturn("NFE");

        controller.removeStudent();

        assertTrue(Context.current().flash().containsValue("NFE"));
    }

    @Test
    public void testDeleteNonExistentStudent() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("matrnr2")).thenReturn("1");
        when(messages.at("admin.studentEdit.noSuchStudentError")).thenReturn(
                "no such Student");

        controller.removeStudent();

        assertTrue(Context.current().flash().containsValue("no such Student"));
    }

    @Test
    public void editAccountTest() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);

        Administrator admin = new Administrator();
        admin.doTransaction(() -> {
            admin.savePassword("password");
        });

        when(userManagement.getUserProfile(any(Context.class))).thenReturn(
                admin);

        when(form.get("passwordChange")).thenReturn("NotNull");
        when(form.get("oldPassword")).thenReturn("password");
        when(form.get("newPassword")).thenReturn("newpassword");
        when(form.get("newPasswordRepeat")).thenReturn("newpassword");
        when(messages.at("admin.account.success.passwords")).thenReturn(
                "Success");

        controller.editAccount();

        assertTrue((new BlowfishPasswordEncoder()).matches("newpassword",
                admin.getPassword()));
        assertTrue(Context.current().flash().containsValue("Success"));
    }

    @Test
    public void testValidationExceptionInEdit() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);

        Administrator admin = new Administrator();
        admin.doTransaction(() -> {
            admin.savePassword("password");
        });

        when(userManagement.getUserProfile(any(Context.class))).thenReturn(
                admin);
        when(form.get("passwordChange")).thenReturn("NotNull");
        when(form.get("oldPassword")).thenReturn("password");
        when(form.get("newPassword")).thenReturn(new String());
        when(messages.at("general.error.minimalPasswordLength")).thenReturn(
                "Validation");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("Validation"));
    }

    @Test
    public void testNonMatchingPasswordInEdit() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);

        Administrator admin = new Administrator();
        admin.doTransaction(() -> {
            admin.savePassword("password");
        });

        when(userManagement.getUserProfile(any(Context.class))).thenReturn(
                admin);
        when(form.get("passwordChange")).thenReturn("NotNull");
        when(form.get("oldPassword")).thenReturn("something");
        when(form.get("newPassword")).thenReturn("something");
        when(form.get("newPasswordRepeat")).thenReturn("something");
        when(messages.at("admin.account.error.passwords")).thenReturn(
                "wrong password");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("wrong password"));
    }

}
