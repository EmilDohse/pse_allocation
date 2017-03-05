package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import controllers.AdminEditAllocationController;
import data.Allocation;
import data.AllocationParameter;
import data.ElipseModel;
import data.GeneralData;
import data.Semester;
import data.Student;
import data.Team;

/**
 * Diese Klasse beinhaltet Tests für den AdminEditAllocationController.
 */
public class AdminEditAllocationControllerTest extends ControllerTest {

    @Mock
    Notifier                      notifier;

    @InjectMocks
    AdminEditAllocationController controller;

    private Allocation            allocation;
    private Team                  firstTeam;
    private Team                  secondTeam;
    private Student               firstStudent;
    private Student               secondStudent;
    private Student               thirdStudent;

    /**
     * Initialisierung der Testdaten.
     */
    @Override
    @Before
    public void before() {
        super.before();

        firstStudent = new Student();
        secondStudent = new Student();
        thirdStudent = new Student();

        firstStudent.doTransaction(() -> {
            firstStudent.setMatriculationNumber(1);
        });
        secondStudent.doTransaction(() -> {
            secondStudent.setMatriculationNumber(2);
        });
        thirdStudent.doTransaction(() -> {
            thirdStudent.setMatriculationNumber(3);
        });

        Semester semester = GeneralData.loadInstance().getCurrentSemester();

        semester.doTransaction(() -> {
            semester.addStudent(firstStudent);
            semester.addStudent(secondStudent);
            semester.addStudent(thirdStudent);
        });

        firstTeam = new Team();
        secondTeam = new Team();

        firstTeam.addMember(firstStudent);
        firstTeam.addMember(secondStudent);

        List<Team> teams = new ArrayList<>();
        teams.add(firstTeam);
        teams.add(secondTeam);

        AllocationParameter p = new AllocationParameter("test", 1234);
        p.save();
        List<AllocationParameter> parameters = new ArrayList<>();
        parameters.add(p);

        allocation = new Allocation();
        allocation.doTransaction(() -> {
            allocation.setTeams(teams);
            allocation.setParameters(parameters);
            allocation.setName("test");
        });

        semester.doTransaction(() -> {
            semester.addAllocation(allocation);
        });

        allocation.refresh();
        firstTeam.refresh();
        secondTeam.refresh();
        firstStudent.refresh();
        secondStudent.refresh();
        thirdStudent.refresh();
    }

    /**
     * Test für das Duplizieren einer Einteilung.
     */
    @Test
    public void duplicateAllocationTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));
        Mockito.when(form.data()).thenReturn(map);

        controller.duplicateAllocation();

        assertEquals(2, GeneralData.loadInstance().getCurrentSemester().getAllocations().size());

        Allocation a = Allocation.getAllocation("clonedtest");
        assertNotNull(a);

        assertEquals(2, a.getTeams().size());
        assertNotNull(a.getTeam(Student.getStudent(1)));
        assertEquals(a.getTeam(Student.getStudent(1)), a.getTeam(Student.getStudent(2)));
        assertNull(a.getTeam(Student.getStudent(3)));

        assertEquals(1, a.getParameters().size());
        assertEquals("test", a.getParameters().get(0).getName());
        assertEquals(1234, a.getParameters().get(0).getValue());
    }

    /**
     * Test ob beim Duplizieren mit falschen Bedingungen eine Exception geworfen
     * wird.
     */
    @Test
    public void duplicateAllocationValidationExceptionTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn("abc");
        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.duplicateAllocation();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Test ob beim Duplizieren mit falschen Bedingungen eine Exception geworfen
     * wird.
     */
    @Test
    public void duplicateAllocationUnknownAllocationIdTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId() + 1));
        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(messages.at("error.allocation.deletedConcurrently")).thenReturn("error");

        controller.duplicateAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob beim Editieren mit falschen Bedingungen eine Exception geworfen
     * wird.
     */
    @Test
    public void editAllocationValidationExceptionTest() {

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", "abc");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.editAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob beim Editieren mit falschen Bedingungen ein Fehler auftritt.
     */
    @Test
    public void editAllocationNoActionTest() {

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.editAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test für das Bewegen eines Studenten und Rückgängigmachen des Vorgangs.
     */
    @Test
    public void editAllocationMoveStudentsAndUndoTest() {

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.get("move")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));
        Mockito.when(form.get("project-selection")).thenReturn(String.valueOf(secondTeam.getId()));
        Mockito.when(form.data()).thenReturn(map);

        controller.editAllocation();

        allocation.refresh();
        firstTeam.refresh();
        secondTeam.refresh();
        firstStudent.refresh();
        secondStudent.refresh();
        thirdStudent.refresh();

        assertTrue(!firstTeam.getMembers().contains(firstStudent));
        assertTrue(secondTeam.getMembers().contains(firstStudent));
        assertTrue(!allocation.getNotAllocatedStudents().contains(thirdStudent));
        assertTrue(secondTeam.getMembers().contains(thirdStudent));
        assertTrue(firstTeam.getMembers().contains(secondStudent));

        controller.undoAllocationEdit();

        allocation.refresh();
        firstTeam.refresh();
        secondTeam.refresh();
        firstStudent.refresh();
        secondStudent.refresh();
        thirdStudent.refresh();

        allocation = Allocation.getAllocations().get(0);

        assertTrue(firstTeam.getMembers().contains(firstStudent));
        assertTrue(!secondTeam.getMembers().contains(firstStudent));
        assertTrue(allocation.getNotAllocatedStudents().contains(thirdStudent));
        assertTrue(!secondTeam.getMembers().contains(thirdStudent));
        assertTrue(firstTeam.getMembers().contains(secondStudent));
    }

    /**
     * Test ob beim Editieren mit falschen Bedingungen eine Exception geforfen
     * wird.
     */
    @Test
    public void editAllocationMoveStudentsValidationExceptionTest() {

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("move")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn("abc");
        Mockito.when(form.get("project-selection")).thenReturn(String.valueOf(secondTeam.getId()));

        Mockito.when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.editAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob beim Editieren mit falschen Bedingungen eine Exception geforfen
     * wird.
     */
    @Test
    public void editAllocationMoveStudentsUnknownAllocationIdTest() {

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("move")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId() + 1));
        Mockito.when(form.get("project-selection")).thenReturn(String.valueOf(secondTeam.getId()));

        Mockito.when(messages.at("error.allocation.deletedConcurrently")).thenReturn("error");

        controller.editAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test für das Bewegen von Studenten in einer finalen Einteilung.
     */
    @Test
    public void editAllocationMoveStudentsFinalAllocationTest() {

        Semester s = GeneralData.loadInstance().getCurrentSemester();
        s.doTransaction(() -> {
            s.setFinalAllocation(allocation);
        });

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("move")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));
        Mockito.when(form.get("project-selection")).thenReturn(String.valueOf(secondTeam.getId()));

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.editAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test für das Bewegen von Studenten, wenn keine Studenten ausgewählt sind.
     */
    @Test
    public void editAllocationMoveStudentsNoStudentsSelectedTest() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("move")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));
        Mockito.when(form.get("project-selection")).thenReturn(String.valueOf(secondTeam.getId()));

        Mockito.when(messages.at("admin.edit.noStudentSelected")).thenReturn("error");

        controller.editAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob beim Editieren mit falschen Bedingungen ein Fehler auftritt.
     */
    @Test
    public void editAllocationMoveStudentsAndUndoAllocationPublishedTest() {

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.get("move")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));
        Mockito.when(form.get("project-selection")).thenReturn(String.valueOf(secondTeam.getId()));
        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(messages.at("error.allocation.deletedConcurrently")).thenReturn("error");

        controller.editAllocation();

        Semester s = GeneralData.loadInstance().getCurrentSemester();
        s.doTransaction(() -> {
            s.setFinalAllocation(allocation);
        });

        controller.undoAllocationEdit();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob beim Rückgängigmachen mit falschen Bedingungen ein Fehler
     * auftritt.
     */
    @Test
    public void undoAllocationEditEmptyStackTest() {

        Mockito.when(messages.at("error.undoStackEmpty")).thenReturn("error");

        controller.undoAllocationEdit();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test für das Tauschen von Studenten, wenn keine Studenten ausgewählt
     * sind.
     */
    @Test
    public void editAllocationSwapStudentsAndUndoTest() {
        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.get("exchange")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));
        Mockito.when(form.data()).thenReturn(map);

        controller.editAllocation();

        allocation.refresh();
        firstTeam.refresh();
        secondTeam.refresh();
        firstStudent.refresh();
        secondStudent.refresh();
        thirdStudent.refresh();

        assertTrue(!firstTeam.getMembers().contains(firstStudent));
        assertTrue(allocation.getNotAllocatedStudents().contains(firstStudent));
        assertTrue(!allocation.getNotAllocatedStudents().contains(thirdStudent));
        assertTrue(firstTeam.getMembers().contains(thirdStudent));
        assertTrue(firstTeam.getMembers().contains(secondStudent));

        controller.undoAllocationEdit();

        allocation.refresh();
        firstTeam.refresh();
        secondTeam.refresh();
        firstStudent.refresh();
        secondStudent.refresh();
        thirdStudent.refresh();

        allocation = Allocation.getAllocations().get(0);

        assertTrue(firstTeam.getMembers().contains(firstStudent));
        assertTrue(!allocation.getNotAllocatedStudents().contains(firstStudent));
        assertTrue(allocation.getNotAllocatedStudents().contains(thirdStudent));
        assertTrue(!firstTeam.getMembers().contains(thirdStudent));
        assertTrue(firstTeam.getMembers().contains(secondStudent));

    }

    /**
     * Test ob beim Tauschen von Studenten mit falschen Bedingungen ein Fehler
     * auftritt.
     */
    @Test
    public void editAllocationSwapStudentsValidationExceptionTest() {

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("exchange")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn("abc");

        Mockito.when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.editAllocation();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Test ob beim Tauschen von Studenten mit falschen Bedingungen ein Fehler
     * auftritt.
     */
    @Test
    public void editAllocationSwapStudentsUnknownAllocationIdTest() {

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("exchange")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId() + 1));

        Mockito.when(messages.at("error.allocation.deletedConcurrently")).thenReturn("error");

        controller.editAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob beim Tauschen von Studenten mit falschen Bedingungen ein Fehler
     * auftritt.
     */
    @Test
    public void editAllocationSwapStudentsFinalAllocationTest() {

        Semester s = GeneralData.loadInstance().getCurrentSemester();
        s.doTransaction(() -> {
            s.setFinalAllocation(allocation);
        });

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("exchange")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.editAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob beim Tauschen von Studenten mit falschen Bedingungen ein Fehler
     * auftritt.
     */
    @Test
    public void editAllocationSwapStudentsNoStudentsSelectedTest() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("exchange")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.editAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test für das Veröffentlichen einer Einteilung.
     */
    @Test
    public void publishAllocationTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));
        Mockito.when(form.data()).thenReturn(map);

        controller.publishAllocation();

        try {
            Mockito.verify(notifier).notifyAllUsers(allocation);
        } catch (EmailException e) {
            e.printStackTrace();
        }

        assertTrue(GeneralData.loadInstance().getCurrentSemester().getFinalAllocation().equals(allocation));
    }

    /**
     * Test ob beim Veröffentlichen der Einteilung mit falschen Bedingungen ein
     * Fehler auftritt.
     */
    @Test
    public void publishAllocationValidationExceptionTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn("abc");
        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.publishAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob beim Veröffentlichen der Einteilung mit falschen Bedingungen ein
     * Fehler auftritt.
     */
    @Test
    public void publishAllocationUnknownAllocationIdTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId() + 1));
        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(messages.at("error.allocation.deletedConcurrently")).thenReturn("error");

        controller.publishAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob beim Veröffentlichen der Einteilung mit falschen Bedingungen ein
     * Fehler auftritt.
     */
    @Test
    public void publishAllocationFinalAllocationTest() {

        Semester s = GeneralData.loadInstance().getCurrentSemester();
        s.doTransaction(() -> {
            s.setFinalAllocation(allocation);
        });

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));
        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(messages.at("admin.edit.noFinalAllocation")).thenReturn("error");

        controller.publishAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test für das Entfernen eine Einteilung.
     * 
     * Funktioniert nicht, da EBean Probleme mit der Testdatenbank hat.
     */
    @Ignore
    @Test
    public void removeAllocationTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));
        Mockito.when(form.data()).thenReturn(map);

        controller.removeAllocation();

        assertEquals(GeneralData.loadInstance().getCurrentSemester().getAllocations().size(), 0);
        assertNull(ElipseModel.getById(Allocation.class, allocation.getId()));
    }

    /**
     * Test ob beim Entfernen der Einteilung mit falschen Bedingungen ein Fehler
     * auftritt.
     */
    @Test
    public void removeAllocationValidationExceptionTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn("abc");
        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.removeAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob beim Entfernen der finalen Einteilung ein Fehler auftritt.
     */
    @Test
    public void removeAllocationFinalAllocationTest() {

        Semester s = GeneralData.loadInstance().getCurrentSemester();
        s.doTransaction(() -> {
            s.setFinalAllocation(allocation);
        });

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn(String.valueOf(allocation.getId()));
        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(messages.at("admin.edit.removeFinalAllocation")).thenReturn("error");

        controller.removeAllocation();

        assertTrue(Context.current().flash().containsValue("error"));
    }

}
