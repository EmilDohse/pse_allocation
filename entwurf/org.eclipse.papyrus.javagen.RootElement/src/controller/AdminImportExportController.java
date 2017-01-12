// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controller;

import importExport.Importer;

/************************************************************/
/**
 * Dieser Controller ist für das Bearbeiten der Http-Requests zuständig, 
 * welche Importieren und Exportieren auf der Import/Export-Seite regeln.
 */
public class AdminImportExportController extends Controller {

	/**
	 * Diese Methode importiert eine Einteilung, sodass sie in der Einteilungsübersicht erscheint. 
	 * Der Administrator wird daraufhin auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result importAllocation() {
	}

	/**
	 * Diese Methode lässt den Administrator eine Datei downloaden, 
	 * welche in einem Textformat eine Einteilung speichert. 
	 * Der Administrator wird daraufhin auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public void exportAllocation() {
	}

	/**
	 * Diese Methode importiert eine SPO, sodass sie in der SPO-Auswahl eines Semesters erscheinen. 
	 * Der Administrator wird daraufhin auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public void importSPO() {
	}

	/**
	 * Diese Methode lässt den Administrator eine Datei downloaden, 
	 * welche in einem Textformat eine SPO speichert. Der Administrator wird 
	 * daraufhin auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public void exportSPO() {
	}

	/**
	 * Diese Methode importiert eine Liste an Projekten, 
	 * welche daraufhin zum aktuellen Semester hinzugefügt werden. 
	 * Der Administrator wird daraufhin auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public void importProjects() {
	}

	/**
	 * Diese Methode lässt den Administrator eine Datei downloaden, 
	 * welche in einem Textformat alle Projekte des aktuellen Semesters abspeichert. 
	 * Der Administrator wird daraufhin auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result exportProjects() {
	}

	/**
	 * Diese Methode importiert eine csv-Datei mit Daten aus dem CMS (CampusManagementSystem) 
	 * und fügt die Daten zu den bereits vorhanden hinzu. Falls Konflikt auftreten, 
	 * werden diese als Liste angezeigt. Der Administrator wird daraufhin 
	 * auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result importCMSData() {
	}

	/**
	 * Diese Methode lässt den Administrator eine Datei downloaden, 
	 * welche eine csv-Datei ist und die Studenten des aktuellen Semester mit 
	 * den eingetragenen TSE und PSE Noten enthält. Der Administrator wird 
	 * daraufhin auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result exportCMSData() {
	}

	/**
	 * Diese Methode importiert eine Liste an Studenten, welche daraufhin zum aktuelle 
	 * Semester hinzugefügt werden. Der Administrator wird daraufhin 
	 * auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result importStudents() {
	}

	/**
	 * Diese Methode lässt den Administrator eine Datei downloaden, 
	 * welche in einem Textformat alle Studenten des aktuellen Semesters abspeichert. 
	 * Der Administrator wird daraufhin auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result exportStudents() {
	}

	/**
	 * Diese Methode löst alle Konflikte auf, welche durch einen CMS-Daten-Import auftreten. 
	 * Der Administrator wählt dafür für jeden Konflikt aus, welchen Datensatz er behalten will. 
	 * Der Administrator wird anschließend auf die Import/Export-Seite zurückgeleitet.
	 * 
	 * @param die Seite, die als Antwort verschickt wird.
	 */
	public Result solveConflicts() {
	}
}