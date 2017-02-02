package views;

import play.mvc.Http.Context;

public class StudentMenu extends Menu {

    public StudentMenu(Context context, String activeUrl) {
        addItems(context);
        setActive(activeUrl);
    }

    private final void addItems(Context context) {
        addItem(context.messages().at("student.sidebar.learningGroup"),
                controllers.routes.StudentPageController.learningGroupPage("")
                        .path());
        addItem(context.messages().at("student.sidebar.rating"),
                controllers.routes.StudentPageController.ratingPage("").path());
        addItem(context.messages().at("student.sidebar.results"),
                controllers.routes.StudentPageController.resultsPage("")
                        .path());
        addItem(context.messages().at("student.sidebar.account"),
                controllers.routes.StudentPageController.accountPage("")
                        .path());
    }
}