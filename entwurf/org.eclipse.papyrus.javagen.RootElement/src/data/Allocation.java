// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package data;

import java.util.List;
import java.util.Map;

/************************************************************/
/**
 * Diese Klasse stellt eine Einteilung von Studierenden in einem Semester dar.
 */
public class Allocation {
	/**
	 * Liste, die alle Teams enthält.
	 */
	private List<Team> teams;
	/**
	 * Der Name der Einteilung.
	 */
	private String name;
	/**
	 * Parameter, mit der die Einteilung gemacht wurde.
	 */
	private List<AllocationParameter> parameters;

	/**
	 * Getter für die Parameter der Einteilung.
	 * 
	 * @return Parameter der Einteilung.
	 */
	public List<AllocationParameter> getParameters() {
		return parameters;
	}
	
	/**
	 * Setter für die Parameter der Einteilung.
	 * 
	 * @param parameters Parameter der Einteilung.
	 */
	public void setSemester(List<AllocationParameter> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Getter für den Namen der Einteilung.
	 * 
	 * @return Name der Einteilung.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter für den Namen der Einteilung.
	 * 
	 * @param name Name der Einteilung.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter für die Liste der Teams.
	 * 
	 * @return Liste der Teams.
	 */
	public List<Team> getTeams() {
		return teams;
	}
	
	/**
	 * Setter für die Liste der Teams.
	 * 
	 * @param teams Liste der Teams.
	 */
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	
	/**
	 * Gibt das Team zurück, in das ein bestimmter Student eingeteilt wurde.
	 * Gibt null zurück, wenn der Student nicht zugeteilt wurde.
	 * 
	 * @param student Student, zu welchem das zugeteilte Team zurückgegeben wird.
	 * 
	 * @return Team, das dem Studenten zugeteilt wurde.
	 */
	public Team getTeam(Student student) {
		//TODO
		return null;
	}
	
	/**
	 * Ändert für einen Studenten das eingeteilte Team.
	 * Wenn das Team null ist, wird der Student keinem Team zugeteilt.
	 * 
	 * @param student Student, dessen Team geändert wird.
	 * 
	 * @param team neues Team, in das der Student eingeteilt ist.
	 */
	public void setStudentsTeam(Student student, Team team) {
		
	}
	
	/**
	 * Diese Methode gibt alle Einteilungen zurück.
	 * 
	 * @return Alle Einteilungen.
	 */
	public static List<Allocation> getAllocations() {
		// TODO
		return null;
	}

	/**
	 * Diese Methode gibt eine spezifische Einteilung zurück, die über ihren
	 * Namen identifiziert wird.
	 * 
	 * @param name
	 *            Der Name der Einteilung.
	 * @return Die Einteilung mit dem gegebenen Namen.
	 */
	public static Allocation getAllocation(String name) {
		// TODO
		return null;
	}

}
