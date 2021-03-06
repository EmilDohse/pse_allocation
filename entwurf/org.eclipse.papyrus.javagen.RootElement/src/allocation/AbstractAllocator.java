// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package allocation;

import java.util.List;

/************************************************************/
/**
 * Abstrakter Löser des Einteilungsproblems.
 */
abstract class AbstractAllocator {

    /**
     * Startet die Berechnung einer Einteilung.
     * 
     * @param configuration
     *            Die Konfiguration, nach der die Einteilung berechnet werden
     *            soll.
     */
    public abstract void calculate(Configuration configuration);

    /**
     * Gibt alle Kriterien, geladen über einen Serviceloader, zurück.
     * 
     * @return Die Liste aller verfügbarer Kriterien.
     */
    public static List<Criterion> getAllCriteria() {
        // TODO
        return null;
    }
}
