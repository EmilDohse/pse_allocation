package views;

import play.mvc.Http.Context;

public class AdminMenu extends Menu {

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
