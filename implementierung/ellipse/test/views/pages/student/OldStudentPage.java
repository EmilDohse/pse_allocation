package views.pages.student;

import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum befüllen der Seite eines Studeierenden,
 * der sich zum zweiten mal anmeldet.
 * 
 * @author samue
 *
 */
public class OldStudentPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/studentOld/changeForm";
    }
}
