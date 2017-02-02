package views;

import play.mvc.Http.Context;

public class IndexMenu extends Menu {

    public IndexMenu(Context context, String activeUrl) {
        addItems(context);
        setActive(activeUrl);
    }

    private final void addItems(Context context) {
        addItem(context.messages().at("index.sidebar.information"),
                controllers.routes.IndexPageController.indexPage("").path());
        addItem(context.messages().at("index.sidebar.registration"),
                controllers.routes.IndexPageController.registerPage("").path());
    }
}