// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package data;

import java.util.List;

/************************************************************/
/**
 * Diese Klasse repräsentiert eine Lerngruppe, das heißt eine Gruppe von
 * Studierenden, die sich gemeinsam zum PSE anmelden wollen.
 */
public class LearningGroup {

    private int id;

    /**
     * Getter für die eindeutige ID des Objektes.
     * 
     * @return Die eindeutige ID des Objektes.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter für die eindeutige ID des Objektes.
     * 
     * @param id
     *            Die neue eindeutige ID des Objektes.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Der Name der Lerngruppe.
     */
    private String        name;
    /**
     * Das nötige Passwort, um der Lerngruppe beizutreten.
     */
    private String        password;
    /**
     * Die Mitglieder der Lerngruppe.
     */
    private List<Student> members;

    /**
     * Die Projektbewertungen der Lerngruppe
     */
    private List<Rating>  ratings;

    /**
     * Studierende, die keiner Lerngruppe angehören, werden als private
     * Lerngruppe der Größe 1 gespeichert. Eine private Lerngruppe kann also
     * niemals von einem Studenten erstellt werden.
     */
    private boolean       isPrivate;

    /**
     * Getter für die Projektbewertungen.
     * 
     * @return Projektbewertungen der Lerngruppe.
     */
    public List<Rating> getRatings() {
        return ratings;
    }

    /**
     * Setter für die Projektbewertungen.
     * 
     * @param ratings
     *            Projektbewertungen der Lerngruppe.
     */
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    /**
     * Ändert die Bewertung für ein Projekt.
     * 
     * @param project
     *            Projekt, für das die Bewertung geändert wird.
     * @param rating
     *            Bewertung des Projekts.
     */
    public void rate(Project project, int rating) {
        for (Rating r : ratings) {
            if (r.getProject().equals(project)) {
                r.setRating(rating);
                return;
            }
        }

        Rating r = new Rating();
        r.setProject(project);
        r.setRating(rating);
        // TODO save
    }

    /**
     * Gibt die Bewertung für ein Projekt zurück.
     * 
     * @param project
     *            Projekt, für welches die Bewertung zurückgegeben wird.
     * 
     * @return Bewertung des Projekts.
     */
    public int getRating(Project project) {
        for (Rating r : ratings) {
            if (r.getProject().equals(project)) {
                return r.getRating();
            }
        }

        // TODO throws
        return 0;
    }

    /**
     * Getter für den Namen.
     * 
     * @return Name der Lerngruppe.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter für das Passwort.
     * 
     * @return Das Passwort, um der Lerngruppe beizutreten.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter für die Mitglieder der Lerngruppe.
     * 
     * @return Die Mitglieder der Lerngruppe.
     */
    public List<Student> getMembers() {
        return members;
    }

    /**
     * Setter für den Namen.
     * 
     * @param name
     *            Name der Lerngruppe.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter für das Passwort.
     * 
     * @param password
     *            Das Passwort, um der Lerngruppe beizutreten.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter für die Mitglieder der Lerngruppe.
     * 
     * @param members
     *            Die Mitglieder der Lerngruppe.
     */
    public void setMembers(List<Student> members) {
        this.members = members;
    }

    /**
     * Fügt einen Studenten zu der Lerngruppe hinzu.
     *
     * @param student
     *            Student, der hinzugefügt wird.
     */
    public void addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
        } else {
            // TODO throws
        }
    }

    /**
     * Entfernt einen Studenten von der Lengruppe.
     * 
     * @param student
     *            Student, der entfernt wird.
     */
    public void removeMember(Student student) {
        if (members.contains(student)) {
            members.remove(student);
        } else {
            // TODO throws
        }
    }

    /**
     * Getter, ob Lerngruppe privat ist.
     * 
     * @return Wahr, wenn privat, sonst falsch.
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Setter, ob Lerngruppe privat ist.
     * 
     * @param isPrivate
     *            Wahr, wenn privat, sonst falsch.
     */
    public boolean setPrivate(boolean isPrivate) {
        return this.isPrivate = isPrivate;
    }

    /**
     * Diese Methode gibt eine spezifische Lerngruppe zurück.
     * 
     * @param name
     *            Der Name der Lerngruppe.
     * @param semester
     *            Das Semster, in dem die Lerngruppe erstellt wurde.
     * @return Die spezifische Lerngruppe.
     */
    public static LearningGroup getLearningGroup(String name, Semester semester) {
        // TODO
        return null;
    }

    /**
     * Diese Methode gibt alle Lerngruppen zurück.
     * 
     * @return Alle Lerngruppen.
     */
    public static List<LearningGroup> getLearningGroups() {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LearningGroup) {
            return this.id == ((LearningGroup) obj).id;
        } else {
            return false;
        }
    }
}
