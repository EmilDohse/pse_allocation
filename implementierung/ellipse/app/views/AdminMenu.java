package views;

import play.mvc.Http.Context;

/**
 * Diese Klasse wird für das Administrator-Menü verwendet und enthält alle
 * Einträge schon voreingestellt.
 * 
 * @author daniel
 *
 */
public class AdminMenu extends Menu {

    /**
     * Erstellt ein neues Admin-Menü
     * 
     * @param context
     *            aktueller Kontext, wird für das Auswählen der aktuellen
     *            Sprache verwendet.
     * @param activeUrl
     *            die aktive URL, setzt den passenden Eintrag auf aktiv.
     */
    public AdminMenu(Context context, String activeUrl) {
        addItems(context);
        setActive(activeUrl);
    }

    private final void addItems(Context context) {
        addItem(context.messages().at("admin.sidebar.adviser"),
                controllers.routes.AdminPageController.adviserPage("").path());
        addItem(context.messages().at("admin.sidebar.projects"),
                controllers.routes.AdminPageController.projectPage("").path());
        addItem(context.messages().at("admin.sidebar.allocation"),
                controllers.routes.AdminPageController.allocationPage("")
                        .path());
        addItem(context.messages().at("admin.sidebar.results"),
                controllers.routes.AdminPageController.resultsPage("").path());
        addItem(context.messages().at("admin.sidebar.exportImport"),
                controllers.routes.AdminPageController.exportImportPage("")
                        .path());
        addItem(context.messages().at("admin.sidebar.studentEdit"),
                controllers.routes.AdminPageController.studentEditPage("")
                        .path());
        addItem(context.messages().at("admin.sidebar.properties"),
                controllers.routes.AdminPageController.propertiesPage("")
                        .path());
    }
}
