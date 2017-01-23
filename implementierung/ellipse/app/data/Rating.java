// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/************************************************************/
/**
 * Diese Klasse stellt eine Bewertung eines Studierenden oder einerr Lerngruppe
 * für ein Projekt dar.
 */
@Entity
public class Rating extends ElipseModel {

    /**
     * Der Wert der Bewertung.
     */
    private int     rating;
    /**
     * Das Projekt, dem die Bewertung gilt.
     */
    @OneToOne
    private Project project;

    /**
     * Getter für den Wert der Bewertung.
     * 
     * @return Der Wert der Bewertung.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Getter für das Projekt der Bewertung.
     * 
     * @return Das Projekt, das bewertet wird.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Setter für den Wert der Bewertung.
     * 
     * @param rating
     *            Der Wert der Bewertung.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Setter für das Projekt der Bewertung.
     * 
     * @param project
     *            Das Projekt, das bewertet wird.
     */
    public void setProject(Project project) {
        this.project = project;
    }

}
