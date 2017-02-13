package views;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Dies ist eine Klasse die ein eindimensionales Menü abbildet. Sie wird für das
 * Seitenleisten-Menü des Web-Interfaces benötigt und speichert die
 * Menu-Einträge (s. {@code MenuItem}).
 * 
 * @author daniel
 *
 */
public class Menu {

    private ArrayList<MenuItem> menuItems;
    private MenuItem            active;

    /**
     * Konstruktor zum Erstellen eines leeren Menüs.
     */
    public Menu() {
        menuItems = new ArrayList<>();
    }

    /**
     * Fügt ein Eintrag zum Menü hinzu.
     * 
     * @param name
     *            der Name der angezeigt wird.
     * @param url
     *            die URL auf die referenziert wird.
     */
    public void addItem(String name, String url) {
        menuItems.add(new MenuItem(name, url));
        if (menuItems.size() == 1) {
            setActive(menuItems.get(0).getLink());
        }
    }

    /**
     * Setzt einen Eintrag auf aktiv. Dies bedeutet, auf dieser Seite befindet
     * man sich gerade.
     * 
     * @param url
     *            die aktuelle URL
     */
    public void setActive(String url) {
        if (active != null) {
            active.setActive(false);
        }
        for (MenuItem item : menuItems) {
            if (item.getLink().equals(url)) {
                item.setActive(true);
                active = item;
            }
        }
    }

    /**
     * Gibt ein Iterator zurück um über alle Menüeinträge zu iterieren.
     * 
     * @return ein Iterator-Objekt
     */
    public Iterator<MenuItem> getIterator() {
        return menuItems.iterator();
    }
}
