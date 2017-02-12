// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import data.Allocation;
import data.Student;
import data.Team;

/************************************************************/
/**
 * Konkretes Kommando zum vertauschen der Teamzugehörigkeit von zwei
 * Studierenden.
 */
public class SwapStudentCommand extends EditAllocationCommand {

    private Allocation allocation;
    private Student    firstStudent;
    private Student    secondStudent;

    /**
     * Erzeugt ein neues Kommando um die Teams von zwei Studenten zu
     * vertauschen.
     * 
     * @param allocation
     *            Einteilung, auf die sich die Änderung bezieht.
     * @param firstStudent
     *            Erster Studierender, der verschoben werden soll.
     * @param secondStudent
     *            Zweiter Studierender, der verschoben werden soll.
     */
    public SwapStudentCommand(Allocation allocation, Student firstStudent,
            Student secondStudent) {
        super();
        this.allocation = allocation;
        this.firstStudent = firstStudent;
        this.secondStudent = secondStudent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        Team firstTeam = allocation.getTeam(firstStudent);
        Team secondTeam = allocation.getTeam(secondStudent);
        // TODO hier eine warnung werfen falls die teamgröße überschritten wird
        if (firstTeam != null) {
            firstTeam.doTransaction(() -> {
                firstTeam.removeMember(firstStudent);
                firstTeam.addMember(secondStudent);
            });
        }
        if (secondTeam != null) {
            secondTeam.doTransaction(() -> {
                secondTeam.addMember(firstStudent);
                secondTeam.removeMember(secondStudent);
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo() {
        Team firstTeam = allocation.getTeam(firstStudent);
        Team secondTeam = allocation.getTeam(secondStudent);
        // TODO hier eine warnung werfen falls die teamgröße überschritten wird
        if (firstTeam != null) {
            firstTeam.doTransaction(() -> {
                firstTeam.removeMember(firstStudent);
                firstTeam.addMember(secondStudent);
            });
        }
        if (secondTeam != null) {
            secondTeam.doTransaction(() -> {
                secondTeam.addMember(firstStudent);
                secondTeam.removeMember(secondStudent);
            });
        }
    }
}
