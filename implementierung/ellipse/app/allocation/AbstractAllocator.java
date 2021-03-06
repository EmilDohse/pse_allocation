// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package allocation;

import java.util.List;

/************************************************************/
/**
 * Abstrakter Löser des Einteilungsproblems.
 */
public abstract class AbstractAllocator {

    /**
     * Initialisiert den Allocator und bereitet die Berechnung vor.
     * 
     * @param configuration
     *            Die Konfiguration, nach der die Einteilung berechnet werden
     *            soll.
     */
    public abstract void init(Configuration configuration);

    /**
     * Startet die Berechnung einer Einteilung.
     */
    public abstract void calculate();

    /**
     * Methode zum Abbruch einer Einteilung.
     */
    public abstract void cancel();

    /**
     * Gibt alle Kriterien, geladen über einen Serviceloader, zurück.
     * 
     * @return Die Liste aller verfügbarer Kriterien.
     */
    public abstract List<? extends Criterion> getAllCriteria();
}
