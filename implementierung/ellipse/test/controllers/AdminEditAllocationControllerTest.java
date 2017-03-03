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

import controllers.AdminEditAllocationController;
import data.Allocation;
import data.AllocationParameter;
import data.ElipseModel;
import data.GeneralData;
import data.Semester;
import data.Student;
import data.Team;

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

    @Test
    public void duplicateAllocationTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn(
                String.valueOf(allocation.getId()));
        Mockito.when(form.data()).thenReturn(map);

        controller.duplicateAllocation();

        assertEquals(2, GeneralData.loadInstance().getCurrentSemester()
                .getAllocations().size());

        Allocation a = Allocation.getAllocation("clonedtest");
        assertNotNull(a);

        assertEquals(2, a.getTeams().size());
        assertNotNull(a.getTeam(Student.getStudent(1)));
        assertEquals(a.getTeam(Student.getStudent(1)),
                a.getTeam(Student.getStudent(2)));
        assertNull(a.getTeam(Student.getStudent(3)));

        assertEquals(1, a.getParameters().size());
        assertEquals("test", a.getParameters().get(0).getName());
        assertEquals(1234, a.getParameters().get(0).getValue());
    }

    @Test
    public void editAllocationMoveStudentsAndUndoTest() {

        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.get("move")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(
                String.valueOf(allocation.getId()));
        Mockito.when(form.get("project-selection")).thenReturn(
                String.valueOf(secondTeam.getId()));
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

        assertTrue(firstTeam.getMembers().contains(firstStudent));
        assertTrue(!secondTeam.getMembers().contains(firstStudent));
        // TODO
        // assertTrue(allocation.getNotAllocatedStudents().contains(thirdStudent));
        assertTrue(!secondTeam.getMembers().contains(thirdStudent));
        assertTrue(firstTeam.getMembers().contains(secondStudent));
    }

    @Test
    public void editAllocationSwapStudentsAndUndoTest() {
        Map<String, String> map = new HashMap<>();
        map.put("selected-students1", String.valueOf(firstStudent.getId()));
        map.put("selected-students2", String.valueOf(thirdStudent.getId()));

        Mockito.when(form.get("exchange")).thenReturn("NotNull");
        Mockito.when(form.get("allocationID")).thenReturn(
                String.valueOf(allocation.getId()));
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

        assertTrue(firstTeam.getMembers().contains(firstStudent));
        // TODO
        // assertTrue(!allocation.getNotAllocatedStudents().contains(firstStudent));
        // assertTrue(allocation.getNotAllocatedStudents().contains(thirdStudent));
        assertTrue(!firstTeam.getMembers().contains(thirdStudent));
        assertTrue(firstTeam.getMembers().contains(secondStudent));

    }

    @Test
    public void publishAllocationTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn(
                String.valueOf(allocation.getId()));
        Mockito.when(form.data()).thenReturn(map);

        controller.publishAllocation();

        try {
            Mockito.verify(notifier).notifyAllUsers(allocation);
        } catch (EmailException e) {
            e.printStackTrace();
        }

        assertTrue(GeneralData.loadInstance().getCurrentSemester()
                .getFinalAllocation().equals(allocation));
    }

    // TODO Testdatenbank austauschen
    @Ignore
    @Test
    public void removeAllocationTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("allocationID")).thenReturn(
                String.valueOf(allocation.getId()));
        Mockito.when(form.data()).thenReturn(map);

        controller.removeAllocation();

        assertEquals(GeneralData.loadInstance().getCurrentSemester()
                .getAllocations().size(), 0);
        assertNull(ElipseModel.getById(Allocation.class, allocation.getId()));
    }
}
