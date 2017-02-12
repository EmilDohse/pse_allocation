// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package data;

import java.util.NoSuchElementException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/************************************************************/
/**
 * Diese Klasse beinhaltet generelle Daten, über den Zustand der Software.
 */
@Entity
public class GeneralData extends ElipseModel {

    /**
     * !!!DO NOT USE THIS!!! GeneralData is supposed to be a Singleton.
     * Constructor is only public due to restrictions in EBean. Use
     * GeneralData.getInstance() instead.
     */
    @Deprecated
    public GeneralData() {
        super();
    }

    /**
     * Das momentane Semester.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    private Semester currentSemester;

    /**
     * Getter für das aktuelle Semester.
     * 
     * @return Das aktuelle Semester.
     */
    public Semester getCurrentSemester() {
        //Ein currentSemester wird durch die DB Evolutions immer gesetzt
        return currentSemester;
    }

    /**
     * Setter für das aktuelle Semester.
     * 
     * @param currentSemester
     *            Das aktuelle Semester.
     */
    public void setCurrentSemester(Semester currentSemester) {
        this.currentSemester = currentSemester;
    }

    /**
     * Lädt die Daten aus der Datenbank
     */
    public static GeneralData loadInstance() {
        // General Data wird durch die DB Evolutions immer angelegt
        return ElipseModel.getAll(GeneralData.class).stream()
                    .findFirst().get();
    }

}
