// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package data;

import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Ebean;

/************************************************************/
/**
 * Diese Klasse stellt eine Teilleistung im Studium dar.
 */
@Entity
public class Achievement extends ElipseModel {

    /**
     * Der Name der Teilleistung.
     */
    @NotNull
    private String name;

    /**
     * Getter für den Namen der Teilleistung.
     * 
     * @return Der Name der Teilleistung.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter für den Namen der Teilleistung.
     * 
     * @param name
     *            Der Name der Teilleistung.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Diese Methode gibt alle Teilleistungen zurück.
     * 
     * @return Alle existierenden Teilleistungen.
     */
    public static List<Achievement> getAchievements() {
        return Ebean.find(Achievement.class).findList();
    }

    /**
     * Diese Methode gibt eine bestimmte Teilleistung zurück, die durch ihren
     * Namen identifiziert wird.
     * 
     * @param name
     *            Der Name der Teilleistung.
     * @return Die bestimmte Teilleistung. Null falls keine Teilleistung den
     *         übergebenen Namen hat.
     */
    public static Achievement getAchievement(String name) {
        return Ebean.find(Achievement.class).findList().stream()
                .filter(achievement -> achievement.getName().equals(name)).findFirst().orElse(null);
    }

}
