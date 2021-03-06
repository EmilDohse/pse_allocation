// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package data;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/************************************************************/
/**
 * Diese Klasse stellt einen Parameter für die Einteilungsberechnung dar.
 */
@Entity
public class AllocationParameter extends ElipseModel {

    /**
     * Der Name des Parameters.
     */
    @NotNull
    private String name;
    /**
     * Die Gewichtung des Parameters.
     */
    private int    value;

    public AllocationParameter() {
        this("default_name", 0);
    }

    public AllocationParameter(String name, int value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * Getter für den Namen des Parameters.
     * 
     * @return Der Name des Parameters.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter für den Wert des Parameters.
     * 
     * @return Der Wert des Parameters.
     */
    public int getValue() {
        return value;
    }

    /**
     * Setter für den Namen des Parameters.
     * 
     * @param name
     *            Der Name des Parameters.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter für den Wert des Parameters.
     * 
     * @param value
     *            Der Wert des Parameters.
     */
    public void setValue(int value) {
        this.value = value;
    }

}
