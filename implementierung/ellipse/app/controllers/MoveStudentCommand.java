// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import data.Allocation;
import data.GeneralData;
import data.Student;
import data.Team;
import exception.AllocationEditUndoException;

/************************************************************/
/**
 * Konkretes Kommando zum verschieben eines Studierenden von seinem aktuellen
 * Team in ein neues.
 */
public class MoveStudentCommand extends EditAllocationCommand {

    private Allocation         allocation;
    private List<Student>      students;
    private Team               newTeam;
    private Map<Student, Team> oldTeams;

    /**
     * Erzeugt ein neues Kommando zum verschieben eines Studierenden.
     * 
     * @param allocation
     *            Einteilung, auf die sich die Änderung bezieht.
     * @param student
     *            Studierender, der verschoben werden soll.
     * @param newTeam
     *            Neues Team, in das der Studierende eingeteilt wird.
     */
    public MoveStudentCommand(Allocation allocation, List<Student> students,
            Team newTeam) {
        super();
        this.allocation = allocation;
        this.students = students;
        this.newTeam = newTeam;
        oldTeams = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {

        for (Student s : students) {
            Team t = allocation.getTeam(s);
            oldTeams.put(s, t);
            if (t != null) {
                t.doTransaction(() -> {
                    t.removeMember(s);
                });
            }
            if (newTeam != null) {
                newTeam.doTransaction(() -> {
                    newTeam.addMember(s);
                });
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo() throws AllocationEditUndoException {

        try {
            allocation.refresh();
        } catch (EntityNotFoundException e) {
            throw new AllocationEditUndoException("Allocation removed");
        }

        for (Student s : students) {
            if (newTeam != null) {
                newTeam.doTransaction(() -> {
                    newTeam.removeMember(s);
                });
            }
            if (oldTeams.get(s) != null) {
                oldTeams.get(s).doTransaction(() -> {
                    oldTeams.get(s).addMember(s);
                });
            }
        }
    }
}
