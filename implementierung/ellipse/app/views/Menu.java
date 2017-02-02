package views;

import java.util.ArrayList;
import java.util.Iterator;

public class Menu {

    private ArrayList<MenuItem> menu;
    private MenuItem            active;

    public Menu() {
        menu = new ArrayList<>();
    }

    public void addItem(String name, String url) {
        menu.add(new MenuItem(name, url));
        if (menu.size() == 1) {
            setActive(menu.get(0).getLink());
        }
    }

    public void setActive(String url) {
        if (active != null) {
            active.setActive(false);
        }
        for (MenuItem item : menu) {
            if (item.getLink().equals(url)) {
                item.setActive(true);
                active = item;
            }
        }
    }

    public Iterator<MenuItem> getIterator() {
        return menu.iterator();
    }
}
