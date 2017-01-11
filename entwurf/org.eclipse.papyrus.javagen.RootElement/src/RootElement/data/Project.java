// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.data;

import RootElement.data.Rating;
import RootElement.data.Semester;
import RootElement.data.Student;

/************************************************************/
/**
 * 
 */
public class Project {
	/**
	 * Der Name des Projektes.
	 */
	private String name;
	/**
	 * Die minimale Anzahl der Teilnehmer einer Gruppe f�r dieses Projekt.
	 */
	private int minTeamSize;
	/**
	 * Die maximale Anzahl der Teilnehmer einer Gruppe f�r dieses Projekt.
	 */
	private int maxTeamSize;
	/**
	 * Die Projektbeschreibung.
	 */
	private String projectInfo;
	/**
	 * URL zu der Website des Projektes.
	 */
	private String projectURL;
	/**
	 * Teams des Projekt
	 */
	private Team[] teams;
	/**
	 * Getter f�r den Namen des Projektes.
	 * @return Der Name des Projektes.
	 */
	public String getName() {
	    return name;
	}
	/**
	 * Getter der maximalen Gr��e f�r Teams dieses Projektes.
	 * @return Die maximale Teamgr��e.
	 */
	public int getMaxTeamSize() {
	    return this.maxTeamSize;
	}
	/**
     * Getter der minimalen Gr��e f�r Teams dieses Projektes.
     * @return Die minimale Teamgr��e.
     */
    public int getMinTeamSize() {
        return this.minTeamSize;
    }
    /**
     * Getter f�r die Information �ber dieses Projektes.
     * @return Die Information des Projektes.
     */
    public String getProjectInfo() {
        return this.projectInfo;
    }
    /**
     * Getter f�r die URL des Projektes.
     * @return Die URL des Projektes.
     */
    public String getProjectURL() {
        return this.projectURL;
    }
    /**
     * Getter f�r die Teams des Projektes.
     * @return Die Teams des Projektes.
     */
    public Team[] getTeams() {
        return this.teams;
    }
    
    /**
     * Setter f�r den Namen des Projektes.
     * @param name Der Name des Projektes.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Setter der maximalen Gr��e f�r Teams dieses Projektes.
     * @param maxTeamSize Die maximale Gr��e f�r Teams dieses Projektes.
     */
    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }
    /**
     * Setter der minimalen Gr��e f�r Teams dieses Projektes.
     * @param minTeamSize Die minimale Gr��e f�r Teams dieses Projektes.
     */
    public void setMinTeamSize(int minTeamSize) {
        this.minTeamSize = minTeamSize;
    }
    /**
     * Setter f�r die Information �ber dieses Projektes.
     * @param projektInfo Die Information des Projektes.
     */
    public void setProjectInfo(String projectInfo) {
        this.projectInfo = projectInfo;
    }
    /**
     * Setter f�r die URL des Projektes.
     * @param projectURL Die URL des Projektes.
     */
    public void setProjectURL(String projectURL) {
        this.projectURL = projectURL;
    }
    /**
     * Setter f�r die Teams des Projektes.
     * @param team Die Teams des Projektes.
     */
    public void setTeams(Team[] teams) {
        this.teams = teams;
    }
    
	/**
	 * Diese Methode gibt alle Projekte zur�ck.
	 * @return projects Alle Projekte.
	 */
	public static RootElement.data.Project getProjects() {
	}

	/**
	 * Diese Methode gibt ein spezifisches Projekt zur�ck, welches �ber seinen Namen und
	 * das Semester, in dem es erstellt wurde, identifiziert wird.
	 * @param name Der Name des Projektes.
	 * @param semester Das Semester, in dem das Projekt erstellt wurde.
	 * @return project Das spezifiscche Projekt.
	 */
	public static RootElement.data.Project getProject(String name, Semester semester) {
	}

	/**
	 * Diese Methode gibt die Bewertung eines spezifischen Studenten f�r dieses Projekt zur�ck.
	 * @param student Der Student, dessen Bewertung zur�ckgegeben werdedn soll.
	 * @return rating Die Bewertung des Studenten.
	 */
	public Rating getRating(Student student) {
	}
};
