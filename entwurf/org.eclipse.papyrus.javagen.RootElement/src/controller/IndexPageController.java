// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controller;

import notificationSystem.Notifier;

/************************************************************/
/**
 * Dieser Controller ist zuständig für alle Http-Requests, 
 * die in dem Bereich aufkommen, welche ohne Anmeldung zugänglich sind. 
 * Dazu zählt neben der Index-Seite auch die Passwort vergessen-Seite 
 * und die E-Mail-Verifikations-Seite.
 */
public class IndexPageController extends Controller {
	/**
	 * 
	 */
	public Notifier notifier;

	/**
	 * Diese Methode gibt die Seite zurück, welche die Startseite ist. 
	 * Auf dieser Seite kann sich Administrator, Betreuer und Student 
	 * anmelden oder aktuelle Informationen einsehen.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result indexPage() {
	}

	/**
	 * Diese Methode gibt die Seite zurück, 
	 * auf der sich ein Student registrieren kann.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result registerPage() {
	}

	/**
	 * Diese Methode initiiert die Login-Prozedur und leitet den 
	 * Anzumeldenden je nach Autorisierung auf die passende Seite weiter.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result login() {
	}

	/**
	 * Diese Methode registriert einen Studenten und fügt diesen 
	 * in die Datenbank ein, sofern alle notwendigen Teillestungen 
	 * als bestanden angegeben wurden.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result register() {
	}

	/**
	 * Diese Methode gibt die Seite zurück, die ein 
	 * Passwort-Rücksetz-Formular für Studenten und Betreuer anzeigt.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result passwordResetPage() {
	}

	/**
	 * Diese Methode schickt eine Email anhand der Daten aus 
	 * dem Passwort-Rücksetz-Formular an den Studenten 
	 * oder den Betreurer, welche ein neues Passwort enthält.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result passwordReset() {
	}

	/**
	 * Diese Methode gibt die Seite zurück, welche einen Studenten verifiziert. 
	 * Dies funktioniert, indem der Student eine Mail mit einen Link auf diese 
	 * Seite erhält, welche noch einen Code als Parameter übergibt. 
	 * Anhand dieses Parameters wird der Student verifiziert.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result verificationPage() {
	}
}