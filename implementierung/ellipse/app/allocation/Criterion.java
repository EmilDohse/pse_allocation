/**
 * Interface für allgemeine Einteilungskriterien deren Namen in der GUI angezeigt werden.
 */
package allocation;

public interface Criterion {

    // TODO Namen für unterschiedliche Sprachen
    /**
     * Getter für den Namen des Kriteriums.
     * 
     * @return Der Name des Kriteriums.
     */
    public String getName();

    public String getDisplayName(String local);
}
