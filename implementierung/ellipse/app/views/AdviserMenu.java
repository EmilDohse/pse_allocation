package views;

import data.GeneralData;
import data.Project;
import play.mvc.Http.Context;

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
    public AdviserMenu(Context context, String activeUrl) {
        addItems(context);
        setActive(activeUrl);
    }

    private final void addItems(Context context) {
        for (Project p : GeneralData.getInstance().getCurrentSemester()
                .getProjects()) {
            addItem(p.getName(), controllers.routes.AdviserPageController
                    .projectsPage(p.getId()).path());
        }
        addItem(context.messages().at("adviser.sidebar.account"),
                controllers.routes.AdviserPageController.accountPage("")
                        .path());
    }
}