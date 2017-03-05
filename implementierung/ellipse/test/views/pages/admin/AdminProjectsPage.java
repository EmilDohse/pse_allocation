package views.pages.admin;

import data.Project;
import data.Adviser;
import play.test.TestBrowser;
import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Projekt-Seite des Admins.
 */
public class AdminProjectsPage extends Page {

    /**
     * Getter für die URL.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/admin";
    }

    /**
     * Diese Methode befüllt die Form zum Erstellen eines Projekts und erstellt
     * dieses.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param name
     *            Der Name des Projekts.
     */
    public void fillAndSubmitAddProjectForm(TestBrowser browser, String name) {
        browser.$("#name").first().fill().with(name);
        browser.$("#addProject").first().click();
    }

    /**
     * Diese Methode vollzieht den Wechsel zur Projekteditieren Seite.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param id
     *            Die ProjektID.
     */
    public void gotoEditProjectPage(TestBrowser browser, int id) {
        browser.$("#edit-" + id).first().click();
    }

    /**
     * Diese Methode gibt zurück, ob ein bestimmtes Projekt angezeigt wird.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param id
     *            Die Id des Projekts.
     * @return Wahr, wenn das Projekt angezeigt wird, falsch sonst.
     */
    public boolean isProjectShown(TestBrowser browser, int id) {
        return !browser.$("#edit-" + id).isEmpty();
    }

    /**
     * Diese Methode gibt zurück, ob ein bestimmtes Projekt angezeigt wird.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param p
     *            Das anzuzeigende Projekt.
     * @return Wahr, wenn das Projekt angezeigt wird, falsch sonst.
     */
    public boolean isProjectShown(TestBrowser browser, Project p) {
        if (p == null) {
            return false;
        }
        String name = browser.$("#tr-" + p.getId() + " > .tr-name").first().getText();
        String inst = browser.$("#tr-" + p.getId() + " > .tr-inst").first().getText();
        String url = browser.$("#tr-" + p.getId() + " > .tr-url").first().getText();
        String advs = browser.$("#tr-" + p.getId() + " > .tr-advs").first().getText();
        String minmax = browser.$("#tr-" + p.getId() + " > .tr-minmax").first().getText();
        String numTeams = browser.$("#tr-" + p.getId() + " > .tr-numTeams").first().getText();
        boolean nameMatch = name.contains(p.getName());
        boolean instMatch = inst.contains(p.getInstitute());
        boolean urlMatch = url.contains(p.getProjectURL());
        boolean advsMatch = true;
        for (Adviser adv : p.getAdvisers()) {
            advsMatch = advsMatch && advs.contains(adv.getName());
        }
        boolean minmaxMatch = minmax.contains(p.getMinTeamSize() + " - " + p.getMaxTeamSize());
        boolean numTeamsMatch = numTeams.contains(Integer.toString(p.getNumberOfTeams()));
        return nameMatch && instMatch && urlMatch && advsMatch && minmaxMatch && numTeamsMatch;
    }
}