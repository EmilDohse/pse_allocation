// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controller;

import allocation.AllocationQueue;

/************************************************************/
/**
 * Dieser Controller ist für das Bearbeiten der Http-Requests zuständig, 
 * welche abgeschickt werden, wenn Betreuer, 
 * Studenten oder Einteilungen hinzugefügt oder gelöscht werden sollen.
 */
public class GeneralAdminController extends Controller {
	/**
	 * 
	 */
	public AllocationQueue allocatorQueue;

	/**
	 * Diese Methode fügt einen Betreuer mit den Daten aus dem vom Administrator 
	 * auszufüllenden Formular zum System hinzu. 
	 * Der Administrator wird anschließend auf die Betreuerübersicht weitergeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result addAdviser() {
	}

	/**
	 * Diese Methode entfernt einen Betreuer und dessen Daten aus dem System. 
	 * Der Administrator wird anschließend auf die Betreuerübersicht weitergeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result removeAdviser() {
	}

	/**
	 * Diese Methode fügt eine Einteilungskonfiguration in die Berechnungswarteschlange hinzu. 
	 * Der Administrator wird anschließend auf die Berechnungsübersichtsseite weitergeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result addAllocation() {
	}

	/**
	 * Diese Methode fügt einen Studenten in das System hinzu. 
	 * Der Administrator wird anschließend auf die Seite zum weiteren 
	 * Hinzufügen und Löschen von Studenten weitergeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result addStudent() {
	}

	/**
	 * Diese Methode löscht einen Studenten aus dem System. 
	 * Der Administrator wird anschließend auf die Seite zum 
	 * weiteren Hinzufügen und Löschen von Studenten weitergeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result removeStudent() {
	}
}
