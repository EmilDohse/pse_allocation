package views;

import play.mvc.Http.Context;

/**
 * Diese Klasse wird für das Studenten-Menü verwendet und enthält alle Einträge
 * schon voreingestellt.
 * 
 * @author daniel
 *
 */
public class StudentMenu extends Menu {

    /**
     * Erstellt ein neues Studenten-Menü
     * 
     * @param context
     *            aktueller Kontext, wird für das Auswählen der aktuellen
     *            Sprache verwendet.
     * @param activeUrl
     *            die aktive URL, setzt den passenden Eintrag auf aktiv.
     */
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