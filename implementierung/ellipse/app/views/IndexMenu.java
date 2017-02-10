package views;

import play.mvc.Http.Context;

/**
 * Diese Klasse wird für das Index-Menü verwendet und enthält alle Einträge
 * schon voreingestellt.
 * 
 * @author daniel
 *
 */
public class IndexMenu extends Menu {

    /**
     * Erstellt ein neues Index-Menü
     * 
     * @param context
     *            aktueller Kontext, wird für das Auswählen der aktuellen
     *            Sprache verwendet.
     * @param activeUrl
     *            die aktive URL, setzt den passenden Eintrag auf aktiv.
     */
    public IndexMenu(Context context, String activeUrl) {
        addItems(context);
        setActive(activeUrl);
    }

    private final void addItems(Context context) {
        addItem(context.messages().at("index.sidebar.information"),
                controllers.routes.IndexPageController.indexPage().path());
        addItem(context.messages().at("index.sidebar.registration"),
                controllers.routes.IndexPageController.registerPage().path());
    }
}