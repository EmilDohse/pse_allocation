// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.data;

import RootElement.data.LearningGroup;
import RootElement.data.Project;

/************************************************************/
/**
 * Diese Klasse stellt eine Bewertung eines Studierenden oder einerr Lerngruppe f�r ein Projekt dar.
 */
public class Rating {
	/**
	 * Der Wert der Bewertung.
	 */
	private int rating;
	/**
	 * Das Projekt, dem die Bewertung gilt.
	 */
	private Project project;
	/**
	 * Die Lerngruppe, welche die Bewertung abgegeben hat.
	 */
	private LearningGroup learningGroup;
	/**
	 * Getter f�r den Wert der Bewertung.
	 * @return Der Wert der Bewertung.
	 */
	public int getRating() {
	    return rating;
	}
	/**
	 * Getter f�r das Projekt der Bewertung.
	 * @return Das Projekt, das bewertet wird.
	 */
	public Project getProject() {
	    return project;
	}
	/**
	 * Getter f�r die Lerngruppe.
	 * @return Die Lerngruppe, die bewertet.
	 */
	public LearningGroup getLearningGroup() {
	    return this.learningGroup;
	}
}
