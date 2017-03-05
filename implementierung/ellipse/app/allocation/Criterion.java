/**
 * Interface für allgemeine Einteilungskriterien deren Namen in der GUI angezeigt werden.
 */
package allocation;

/**
 * Abstraktes Kriterium, anhand dem die Einteilung berechnet wird.
 */
public interface Criterion {

    /**
     * Getter für den Namen des Kriteriums.
     * 
     * @return Der Name des Kriteriums.
     */
    public String getName();

    /**
     * Gibt den Namen zurück, unter dem das Kriterium in der GUI angezeigt wird.
     * 
     * @param local
     *            der Ländercode, der Sprache, die gerade angezeigt wird.
     * @return Den Anzeigenamen des Kriteriums.
     */
    public String getDisplayName(String local);
}
