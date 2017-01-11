// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.Einteilung;

import RootElement.Einteilung.AbstractAllocator;
import RootElement.Einteilung.Configuration;

/************************************************************/
/**
 * Die Queue ist dazu da die Stapelverarbeitung von Einteilungs berechnungen zu realisieren
 */
public class AllocationQueue {
	/**
	 * Die intern verwendete queue
	 */
	private List<Configuration> configurationQueue;
	/**
	 * Der Singelton der der Allocation queue
	 */
	private static AllocationQueue instance
	/**
	 * Der Alocator der zur Berechnung verwendet wird
	 */
	private AbstractAllocator Allocator;
	/**
	 * Die Konfiguration die aktuell zur Berechnung verwendet wird
	 */
	private Configuration currentlyCalculatedConfiguration;

	/**
	 * gibt die eine existierende Instanz der AllocationQueue (Singeltion) zurück
	 * @return  die Instanz der AllocationQueue
	 */
	public static AllocationQueue getInstance() {
	}

	/**
	 * fügt der Berechnungsqueue ein element hinzu das dann berechnet wird
	 * @param configuration Die Konfiguration die zur Berechnungswarteliste hinzugefügt wird
	 */
	public void addToQueue(Configuration configuration) {
	}

	/**
	 * Nimmt eine Konfiguration aus der Berechnungsqueue heraus. Falls diese Konfiguration bereits berechnet wird, wird die Berechnung abgebrochen
	 * @param configuration Die Konfiguration die entfernt werden soll
	 */
	public void cancelAllocation(Configuration configuration) {
	}

	/**
	 * Gibt die Queue der Berechnungen zurück, inklusive der Konfiguration die aktuell berechnet wird. 
	 * @return queue Liste der Konfigurationen als FIFO-Queue angeordnet
	 */
	public List<Configuration> getQueue() {
	}
};