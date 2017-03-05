package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import data.Allocation;
import data.AllocationParameter;
import data.GeneralData;
import data.Semester;
import data.Student;
import data.Team;
import exception.AllocationEditUndoException;

/**
 * Diese Klasse beinhaltet Tests für den SwapStudentCommand.
 */
public class SwapStudentCommandTest extends ControllerTest {

    private Allocation         allocation;
    private Student            firstStudent;
    private Student            secondStudent;
    private Team               firstTeam;
    private Team               secondTeam;
    private SwapStudentCommand command;
    private Semester           semester;

    /**
     * Initialisierung der Testdaten.
     */
    @Override
    @Before
    public void before() {
        super.before();
        allocation = new Allocation();
        allocation.save();
        firstStudent = new Student();
        firstStudent.save();
        secondStudent = new Student();
        secondStudent.save();
        semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addAllocation(allocation);
            semester.addStudent(firstStudent);
            semester.addStudent(secondStudent);
        });
        firstTeam = new Team();
        firstTeam.addMember(firstStudent);
        secondTeam = new Team();
        secondTeam.addMember(secondStudent);
        // Teams dürfen nicht explizit gespeichert werden. WTF Ebean?
        List<Team> teams = new ArrayList<>();
        teams.add(firstTeam);
        teams.add(secondTeam);
        allocation.doTransaction(() -> {
            allocation.setTeams(teams);
        });

        command = new SwapStudentCommand(allocation, firstStudent,
                secondStudent);
    }

    /**
     * Test für das Tauschen zweier Studenten.
     */
    @Test
    public void executeTest() {
        command.execute();

        allocation.refresh();
        assertNotNull(allocation.getTeam(firstStudent));
        assertNotNull(allocation.getTeam(secondStudent));
        assertEquals(secondTeam, allocation.getTeam(firstStudent));
        assertEquals(firstTeam, allocation.getTeam(secondStudent));
    }

    /**
     * Test für das Tauschen zweier Studenten während der finalen Einteilung.
     */
    @Test
    public void executeFinalTest() {
        semester.doTransaction(() -> {
            semester.setFinalAllocation(allocation);
        });
        command.execute();
        assertNotNull(allocation.getTeam(firstStudent));
        assertNotNull(allocation.getTeam(secondStudent));
        assertEquals(firstTeam, allocation.getTeam(firstStudent));
        assertEquals(secondTeam, allocation.getTeam(secondStudent));
    }

    /**
     * Test für das Rückgängigmachen eines Swaps.
     * 
     * @throws AllocationEditUndoException
     *             AllocationEditUndoException.
     */
    @Test
    public void undoTest() throws AllocationEditUndoException {
        command.execute();
        command.undo();

        assertNotNull(allocation.getTeam(firstStudent));
        assertNotNull(allocation.getTeam(secondStudent));
        assertEquals(firstTeam, allocation.getTeam(firstStudent));
        assertEquals(secondTeam, allocation.getTeam(secondStudent));
    }

    /**
     * Test für das Rückgängigmachen eines Löschvorgangs.
     * 
     * @throws AllocationEditUndoException
     *             AllocationEditUndoException.
     */
    @Test(expected = AllocationEditUndoException.class)
    public void undoExceptionDeletedTest() throws AllocationEditUndoException {
        command.execute();

        for (Team t : allocation.getTeams()) {
            t.doTransaction(() -> {
                t.setMembers(new ArrayList<Student>());
            });
        }
        allocation.doTransaction(() -> {
            allocation.setParameters(new ArrayList<AllocationParameter>());
        });
        allocation.delete();
        command.undo();
    }
}
