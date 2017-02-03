package views;

/**
 * Dies ist ein Menüeintrag, welcher in der Klasse {@code Menu} verwendet wird.
 * 
 * @author daniel
 *
 */
public class MenuItem {

    private String  menuText;
    private String  link;
    private boolean isActive = false;

    /**
     * Dies erstellt einen Menüeintrag
     * 
     * @param menuText
     *            der Text des Eintrages
     * @param link
     *            die URL des Eintrages
     */
    public MenuItem(String menuText, String link) {
        this.menuText = menuText;
        this.link = link;
    }

    /**
     * Gibt den Menütext zurück.
     * 
     * @return den Text.
     */
    public String getMenuText() {
        return menuText;
    }

    /**
     * Setzt den Menütext.
     * 
     * @param menuText
     *            neuer Text.
     */
    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    /**
     * Gibt den Link zurück.
     * 
     * @return den Link.
     */
    public String getLink() {
        return link;
    }

    /**
     * Setzt den Link neu.
     * 
     * @param link
     *            der neue Link.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Makiert diesen Eintrag als aktiv.
     * 
     * @param isActive
     *            true falls aktiv.
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Gibt zurück ob dieser Eintrag aktiv ist.
     * 
     * @return true wenn aktiv.
     */
    public boolean isActive() {
        return isActive;
    }
}
