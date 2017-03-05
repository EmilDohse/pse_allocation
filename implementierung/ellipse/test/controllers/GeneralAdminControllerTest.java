package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.mail.EmailException;
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

/**
 * Diese Klasse beinhaltet die Tests für den GeneralAdminController.
 */
public class GeneralAdminControllerTest extends ControllerTest {

    @Mock
    Notifier               notifier;

    @Mock
    UserManagement         userManagement;

    @InjectMocks
    GeneralAdminController controller;

    /**
     * Test für das Hinzufügen eines Betreuers.
     * 
     * @throws EmailException
     *             EmailException.
     */
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

    /**
     * Test ob das Hinzufügen eines Betreuers fehlschlägt falls die Eingabe
     * einen leeren String enthält.
     */
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

    /**
     * Test für das Entfernen eines Betreuers.
     */
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

    /**
     * Test für das Hinzufügen einer Einteilung.
     * 
     * @throws InterruptedException
     *             InterruptedException.
     */
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

    /**
     * Test ob beim Hinzufügen Einteilung ohne Namen ein Fehler auftritt.
     */
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

    /**
     * Test ob beeim Hinzufügen eines Admins mit falschen Parametern ein Fehler
     * auftritt.
     */
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

    /**
     * Test ob beim Hinzufügen von Criterien mit falschem Parameter ein Fehler
     * auftritt.
     */
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

    /**
     * Test für das Entfernen einer Allocation aus der Warteschlange.
     */
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

    /**
     * Test für das Hinzufügen eines Studenten.
     */
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

    /**
     * Test ob das Hinzufügen eines Studenten mit falschem Eingabeformat
     * fehlschlägt.
     */
    @Test
    public void testValidationExceptionInStudent() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("firstName")).thenReturn(new String());
        when(messages.at("general.error.noEmptyString"))
                .thenReturn("Validation Exception");

        controller.addStudent();

        assertTrue(Context.current().flash()
                .containsValue("Validation Exception"));
    }

    /**
     * Test ob das Hinzufügen eines Studenten fehlschlägt, falls er bereits
     * existiert.
     */
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
        when(messages.at("admin.studentEdit.matrNrExistsError"))
                .thenReturn("already existing");

        controller.addStudent();

        assertTrue(Context.current().flash().containsValue("already existing"));
    }

    /**
     * Test für das Entfernen eines Studenten.
     * 
     * Funktioniert nicht, da EBean mit der TestDatenbank Probleme hat.
     */
    @Test
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
        when(messages.at(eq("admin.studentEdit.removeStudentSuccess"),
                any(String.class))).thenReturn("Success");

        controller.removeStudent();

        assertTrue(GeneralData.loadInstance().getCurrentSemester().getStudents()
                .isEmpty());
        assertNull(Student.getStudent(1));
        assertTrue(Context.current().flash().containsValue("Success"));
    }

    /**
     * Testet auf NumberformatException beim Entfernen Eines Studenten.
     */
    @Test
    public void testNFEinRemoveStudent() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("matrnr2")).thenReturn("a");
        when(messages.at("error.wrongInput")).thenReturn("NFE");

        controller.removeStudent();

        assertTrue(Context.current().flash().containsValue("NFE"));
    }

    /**
     * Test ob das Entfernen eines nicht vorhandenen Studenten fehlschlägt.
     */
    @Test
    public void testDeleteNonExistentStudent() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("matrnr2")).thenReturn("1");
        when(messages.at("admin.studentEdit.noSuchStudentError"))
                .thenReturn("no such Student");

        controller.removeStudent();

        assertTrue(Context.current().flash().containsValue("no such Student"));
    }

    /**
     * Test für das Editieren des Accounts.
     */
    @Test
    public void editAccountTest() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);

        Administrator admin = new Administrator();
        admin.doTransaction(() -> {
            admin.savePassword("password");
        });

        when(userManagement.getUserProfile(any(Context.class)))
                .thenReturn(admin);

        when(form.get("passwordChange")).thenReturn("NotNull");
        when(form.get("oldPassword")).thenReturn("password");
        when(form.get("newPassword")).thenReturn("newpassword");
        when(form.get("newPasswordRepeat")).thenReturn("newpassword");
        when(messages.at("admin.account.success.passwords"))
                .thenReturn("Success");

        controller.editAccount();

        assertTrue((new BlowfishPasswordEncoder()).matches("newpassword",
                admin.getPassword()));
        assertTrue(Context.current().flash().containsValue("Success"));
    }

    /**
     * Test ob beim Editieren des Accounts mit falschem Eingabeformat ein Fehler
     * auftritt.
     */
    @Test
    public void testValidationExceptionInEdit() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);

        Administrator admin = new Administrator();
        admin.doTransaction(() -> {
            admin.savePassword("password");
        });

        when(userManagement.getUserProfile(any(Context.class)))
                .thenReturn(admin);
        when(form.get("passwordChange")).thenReturn("NotNull");
        when(form.get("oldPassword")).thenReturn("password");
        when(form.get("newPassword")).thenReturn(new String());
        when(messages.at("general.error.minimalPasswordLength"))
                .thenReturn("Validation");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("Validation"));
    }

    /**
     * Test ob beim Ändern des Passworts das neue Passwort richtig wiederholt
     * wird.
     */
    @Test
    public void testNonMatchingPasswordInEdit() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);

        Administrator admin = new Administrator();
        admin.doTransaction(() -> {
            admin.savePassword("password");
        });

        when(userManagement.getUserProfile(any(Context.class)))
                .thenReturn(admin);
        when(form.get("passwordChange")).thenReturn("NotNull");
        when(form.get("oldPassword")).thenReturn("something");
        when(form.get("newPassword")).thenReturn("something");
        when(form.get("newPasswordRepeat")).thenReturn("something");
        when(messages.at("admin.account.error.passwords"))
                .thenReturn("wrong password");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("wrong password"));
    }

}
