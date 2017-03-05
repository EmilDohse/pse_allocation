package views.pages.admin;

import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Einstellungs-Seite des
 * Admins.
 */
public class AdminPropertiesPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin/properties";
    }
}