// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/************************************************************/
/**
 * Klasse, die ein Project repräsentiert
 */
@Entity
public class Project extends ElipseModel implements Comparable<Project> {

    public static final String CONCURRENCY_ERROR = "error.project.deletedConcurrently";
    /**
     * Der Name des Projektes.
     */
    @NotNull
    private String             name;
    /**
     * Die minimale Anzahl der Teilnehmer einer Gruppe für dieses Projekt.
     */
    private int                minTeamSize;
    /**
     * Die maximale Anzahl der Teilnehmer einer Gruppe für dieses Projekt.
     */
    private int                maxTeamSize;
    /**
     * Anzahl der Teams die zu diesem Projekt zugeteilt werden.
     */
    private int                numberOfTeams;
    /**
     * Die Projektbeschreibung.
     */
    @NotNull
    private String             projectInfo;
    /**
     * URL zu der Website des Projektes.
     */
    @NotNull
    private String             projectURL;
    /**
     * Das Institut, welches das Projekt anbietet.
     */
    @NotNull
    private String             institute;
    /**
     * Betreuer des Projekts
     */
    @ManyToMany
    private List<Adviser>      advisers;

    /**
     * Danke Ebean.
     */
    @ManyToOne
    private Semester           semester;

    public Project() {
        this("", "", "", "");
    }

    public Project(String name, String projectInfo, String institut, String url) {
        super();
        this.name = name;
        this.projectInfo = projectInfo;
        this.institute = institut;
        this.projectURL = url;
        advisers = new ArrayList<>();
    }

    /**
     * Setter für das Semester. Sollte nicht manuell benutzt werden. Zum Setzten
     * reicht es, das Projekt uber Semester.addProject() oder
     * Semester.setProjects hinzuzufügen.
     * 
     * @param semester
     *            Das Semester, zu dem diese Projekt gehört.
     */
    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    /**
     * Getter für die Anzahl der Teams.
     * 
     * @return Anzahl der Teams.
     */
    public int getNumberOfTeams() {
        return numberOfTeams;
    }

    /**
     * Setter für die Anzahl der Teams.
     * 
     * @param numberOfTeams
     *            Anzahl der Teams.
     */
    public void setNumberOfTeams(int numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    /**
     * Getter für die Betreuer des Projekts.
     * 
     * @return Betreuer des Projekts.
     */
    public List<Adviser> getAdvisers() {
        return advisers;
    }

    /**
     * Setter für die Betreuer des Projekts.
     * 
     * @param advisers
     *            Betreuer des Projekts.
     */
    public void setAdvisers(List<Adviser> advisers) {
        this.advisers = advisers;
    }

    /**
     * Fügt dem Projekt einen Betreuer hinzu.
     * 
     * @param adviser
     *            Betreuer der hinzugefügt wird.
     */
    public void addAdviser(Adviser adviser) {
        advisers.add(adviser);
    }

    /**
     * Entfernt einen Betreuer vom Projekt.
     * 
     * @param adviser
     *            Betreuer der entfernt wird.
     */
    public void removeAdviser(Adviser adviser) {
        advisers.remove(adviser);
    }

    /**
     * Getter für den Namen des Projektes.
     * 
     * @return Der Name des Projektes.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter der maximalen Größe für Teams dieses Projektes. -1 entspricht
     * keiner gesetzten Teamgröße.
     * 
     * @return Die maximale Teamgröße.
     */
    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    /**
     * Getter der minimalen Größe für Teams dieses Projektes. -1 entspricht
     * keiner gesetzten Teamgröße.
     * 
     * @return Die minimale Teamgröße.
     */
    public int getMinTeamSize() {
        return minTeamSize;
    }

    /**
     * Getter für die Information über dieses Projektes.
     * 
     * @return Die Information des Projektes.
     */
    public String getProjectInfo() {
        return projectInfo;
    }

    /**
     * Getter für die URL des Projektes.
     * 
     * @return Die URL des Projektes.
     */
    public String getProjectURL() {
        return projectURL;
    }

    /**
     * Setter für den Namen des Projektes.
     * 
     * @param name
     *            Der Name des Projektes.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter der maximalen Größe für Teams dieses Projektes. -1 entspricht
     * keiner gesetzten Teamgröße.
     * 
     * @param maxTeamSize
     *            Die maximale Größe für Teams dieses Projektes.
     */
    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    /**
     * Setter der minimalen Größe für Teams dieses Projektes. -1 entspricht
     * keiner gesetzten Teamgröße.
     * 
     * @param minTeamSize
     *            Die minimale Größe für Teams dieses Projektes.
     */
    public void setMinTeamSize(int minTeamSize) {
        this.minTeamSize = minTeamSize;
    }

    /**
     * Setter für die Information über dieses Projektes.
     * 
     * @param projektInfo
     *            Die Information des Projektes.
     */
    public void setProjectInfo(String projectInfo) {
        this.projectInfo = projectInfo;
    }

    /**
     * Setter für die URL des Projektes.
     * 
     * @param projectURL
     *            Die URL des Projektes.
     */
    public void setProjectURL(String projectURL) {
        this.projectURL = projectURL;
    }

    /**
     * Gibt den Institutsnamen des Institutes zurück, welches das Projekt
     * anbietet.
     * 
     * @return den Namen
     */
    public String getInstitute() {
        return institute;
    }

    /**
     * Setzt den Institutsnamen.
     * 
     * @param institute
     *            der Name des Instituts.
     */
    public void setInstitute(String institute) {
        this.institute = institute;
    }

    /**
     * Diese Methode gibt alle Projekte zurück.
     * 
     * @return Alle Projekte.
     */
    public static List<Project> getProjects() {
        return ElipseModel.getAll(Project.class);
    }

    /**
     * Gibt das Semester zurück, in dem das Projekt angeboten wurde.
     * 
     * @return Semester, in dem das Projekt angeboten wurde.
     */
    public Semester getSemester() {
        return semester;
    }

    @Override
    public int compareTo(Project o) {
        return name.compareTo(o.getName());
    }

}
