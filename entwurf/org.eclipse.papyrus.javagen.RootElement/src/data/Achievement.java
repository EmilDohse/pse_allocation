// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package data;

/************************************************************/
/**
 * Diese Klasse stellt eine Teilleistung im Studium dar.
 */
public class Achievement {
	/**
	 * Der Name der Teilleistung.
	 */
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
	public static Achievement[] getAchievements() {
		// TODO
		return null;
	}

	/**
	 * Diese Methode gibt eine bestimmte Teilleistung zurück, die durch ihren
	 * Namen identifiziert wird.
	 * 
	 * @param name
	 *            Der Name der Teilleistung.
	 * @return Die bestimmte Teilleistung.
	 */
	public static Achievement getAchievement(String name) {
		// TODO
		return null;
	}
}
