// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controller;

/************************************************************/
/**
 * Dieser Controller ist für das Bearbeiten der Http-Requests zuständig, welche
 * im Betreuerbereich aufkommen.
 */
public class AdviserPageController extends Controller {

	/**
	 * Diese Methode gibt die Seite zurück, auf der der Betreuer Projekte sieht,
	 * Projekte hinzufügen, editieren oder entfernen kann. Editieren und
	 * Entfernen eines Projektes ist beschränkt auf Betreuer, welche dem Projekt
	 * beigetreten sind.
	 * 
	 * @param name Der Name des Projektes (da mitgegeben über die URL, ist der String encoded)
	 * 
	 * @return die Seite, die als Antwort verschickt wird.
	 */
	public Result projectsPage(String name) {
		// TODO
		return null;
	}

	/**
	 * Diese Methode fügt ein neues Projekt in das System ein und leitet den
	 * Betreuer zurück auf die Seite zum Editieren des Projektes.
	 * 
	 * @return die Seite, die als Antwort verschickt wird.
	 */
	public Result addProject() {
		// TODO
		return null;
	}

	/**
	 * Diese Methode löscht ein Projekt und alle dazugehörenden Daten aus dem
	 * System und leitet den Betreuer auf die Seite zum Editieren und Hinzufügen
	 * von Projekten. Nur Betreuer welche dem Projekt beigetreten sind können
	 * dieses editieren.
	 * 
	 * @return die Seite, die als Antwort verschickt wird.
	 */
	public Result removeProject() {
		// TODO
		return null;
	}

	/**
	 * Diese Methode editiert ein bereits vorhandenes Projekt. Die zu
	 * editierenden Daten übermittelt der Betreuer über ein Formular, welches er
	 * zum Editieren abschickt. Nur Betreuer welche dem Projekt beigetreten sind
	 * können dieses editieren. Anschließend wird der Betreuer auf die Seite zum
	 * Hinzufügen und Editieren von Projekten weitergeleitet.
	 * 
	 * @return die Seite, die als Antwort verschickt wird.
	 */
	public Result editProject() {
		// TODO
		return null;
	}

	/**
	 * Diese Methode fügt einen Betreuer zu einem bereits existierenden Projekt
	 * hinzu, sodass dieser auch die Möglichkeit besitzt das Projekt zu
	 * editieren oder zu löschen und nach der Veröffentlichung einer Einteilung
	 * auch Teams und deren Mitglieder sieht. Nach dem Beitreten wird der
	 * Betreuer auf die Seite zum Hinzufügen und Editieren von Projekten
	 * weitergeleitet.
	 * 
	 * @return die Seite, die als Antwort verschickt wird.
	 */
	public Result joinProject() {
		// TODO
		return null;
	}

	/**
	 * Diese Methode entfernt einen Betreuer aus einem existierenden Projekt,
	 * sodass dieser nicht mehr das Projekt editieren oder löschen kann.
	 * Anschließend wird der Betreuer auf die Seite zum Hinzufügen und Editieren
	 * von Projekten weitergeleitet.
	 * 
	 * @return die Seite, die als Antwort verschickt wird.
	 */
	public Result leaveProject() {
		// TODO
		return null;
	}

	/**
	 * Diese Methode speichert alle von einem Betreuer in ein Formular
	 * eingegebenen Noten, sodass diese vom Administrator in das CMS importiert
	 * werden können. Anschließend wird der Betreuer auf die Projektseite des
	 * jeweiligen Projektes weitergeleitet.
	 * 
	 * @return die Seite, die als Antwort verschickt wird.
	 */
	public Result saveStudentsGrades() {
		// TODO
		return null;
	}

	/**
	 * Diese Methode gibt die Seite zurück, auf der der Betreuer seine
	 * Benutzerdaten wie E-Mail-Adresse und Passwort ändern kann.
	 * 
	 * @return die Seite, die als Antwort verschickt wird.
	 */
	public Result accountPage() {
		// TODO
		return null;
	}

	/**
	 * Diese Methode editiert die Daten des Betreuers, welche er auf der
	 * Account-Seite geändert hat.
	 * 
	 * @return die Seite, die als Antwort verschickt wird.
	 */
	public Result editAccount() {
		// TODO
		return null;
	}
}
