package views;

import data.GeneralData;
import data.Project;

/**
 * Diese Klasse wird für das Betreuer-Menü verwendet und enthält alle Einträge
 * schon voreingestellt.
 * 
 * @author daniel
 *
 */
public class AdviserMenu extends Menu {

    /**
     * Erstellt ein neues Betreuer-Menü
     * 
     * @param activeUrl
     *            die aktive URL, setzt den passenden Eintrag auf aktiv.
     */
    public AdviserMenu(String activeUrl) {
        addItems();
        setActive(activeUrl);
    }

    private final void addItems() {
        for (Project p : GeneralData.loadInstance().getCurrentSemester()
                .getProjects()) {
            addItem(p.getName(), controllers.routes.AdviserPageController
                    .projectsPage(p.getId()).path());
        }
    }
}