// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package allocation;

import java.util.ArrayList;
import java.util.List;

import data.AllocationParameter;
import data.GeneralData;
import data.LearningGroup;
import data.Project;
import data.Student;
import data.Team;

/************************************************************/
/**
 * Eine Konfiguration dient als Sammlung von Daten, die zur
 * Einteilungsberechnung benötigt werden.
 */
public class Configuration {

    /**
     * Der Name der Einteilung der angezeigt wird.
     */
    private String                    allocationName;
    /**
     * Alle Studenten, die bei der Einteilung berücksichtigt werden.
     */
    private List<Student>             students;
    /**
     * Alle Lerngruppen, die bei der Einteilung berücksichtigt werden.
     */
    private List<LearningGroup>       learningGroups;
    /**
     * Die Teams, die bei der Einteilung berücksichtigt werden.
     */
    private List<Team>                teams;
    /**
     * Die Parameter für Kriterien, die bei der Einteilung berücksichtigt
     * werden.
     */
    private List<AllocationParameter> parameters;

    /**
     * Konstruktor, der alle Arrays als Parameter entgegen nimmt.
     * 
     * @param allocationName
     *            Der Name der Einteilung, die berechnet werden soll.
     * @param students
     *            Liste von Studenten, die eingeteilt werden sollen.
     * @param learningGroups
     *            Liste von Lerngruppen, die zugeteilt werden sollen.
     * @param teams
     *            Liste von Teams, denen Studenten zugeteilt werden sollen.
     * @param parameters
     *            Liste von Parametern, die der Admin eingestellt hat.
     */
    public Configuration(String allocationName, List<Student> students, List<LearningGroup> learningGroups,
            List<AllocationParameter> parameters) {
        this.allocationName = allocationName;
        this.students = students;
        this.parameters = parameters;
        this.learningGroups = learningGroups;
        teams = new ArrayList<>();
        // teams werden aus projekten erstellt
        ArrayList<Project> projects = (ArrayList<Project>) GeneralData.getCurrentSemester().getProjects();
        for (Project project : projects) {
            for (int i = 1; i <= project.getNumberOfTeams(); i++) {
                Team team = new Team(project, new ArrayList<>());
                team.setTeamNumber(i);
                teams.add(team);
            }
        }
    }

    /**
     * Getter für den Einteilungsname.
     * 
     * @return Der Name der Einteilung, die berechnet werden soll.
     */
    public String getName() {
        return allocationName;
    }

    /**
     * Getter für Studenten.
     * 
     * @return Liste von Studenten, die eingeteilt werden sollen.
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Getter für Teams.
     * 
     * @return Liste von Teams, denen Studenten zugeteilt werden sollen.
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Getter für Lerngruppen.
     * 
     * @return Array von Lerngruppen, die zugeteilt werden sollen.
     */
    public List<LearningGroup> getLearningGroups() {
        return learningGroups;
    }

    /**
     * Getter für Kriterien-Parameter.
     * 
     * @return Liste von Parametern, die der Admin eingegeben hat.
     */
    public List<AllocationParameter> getParameters() {
        return parameters;
    }

    /**
     * compares two configurations by name
     * 
     * @param o
     *            die configuration mit der verglichen wird
     * @return true wenn die namen übereinstimmen
     */
    public boolean compareTo(Object o) {
        if (o instanceof Configuration) {
            return ((Configuration) o).getName().equals(this.getName());
        }
        return false;
    }
}
