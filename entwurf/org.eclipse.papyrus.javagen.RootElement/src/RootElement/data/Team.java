// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.data;

import RootElement.data.Adviser;
import RootElement.data.Project;
import RootElement.data.Rating;
import RootElement.data.Student;

/************************************************************/
/**
 * Diese KLasse stellt ein Team eines Projektes dar.
 */
public class Team {
	/**
	 * ??
	 */
	public Project[] project;

	/**
	 * Diese Methode gibt die Studierenden(Mitgliederr) des Teams zur�ck.
	 * @return members Die Mitglieder des Teams.
	 */
	public Student getMembers() {
	}

	/**
	 * Diese Methode gibt die Bewertung eines Studierenden zu dem Projekt dieses Teams zur�ck.
	 * @param student Der Studierende, dessen Bewertung zur�ckgegeben werden soll.
	 * @return rating Die Bewertung des Studierenden.
	 */
	public Rating getRating(Student student) {
	}

	/**
	 * Diese Methode gibt die Betreuer des Teams zur�ck.
	 * @return advisers Die Betreuer des Teams.
	 */
	public Adviser getAdvisers() {
	}
};
