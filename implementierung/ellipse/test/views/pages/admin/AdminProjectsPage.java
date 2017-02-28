package views.pages.admin;

import data.Project;
import data.Adviser;
import play.test.TestBrowser;
import views.pages.Page;

public class AdminProjectsPage extends Page {

    @Override
    public String getUrl() {
        return "/admin";
    }

    public void fillAndSubmitAddProjectForm(TestBrowser browser, String name) {
        browser.$("#name").first().fill().with(name);
        browser.$("#addProject").first().click();
    }

    public void gotoEditProjectPage(TestBrowser browser, int id) {
        browser.$("#edit-" + id).first().click();
    }

    public boolean isProjectShown(TestBrowser browser, int id) {
        return !browser.$("#edit-" + id).isEmpty();
    }

    public boolean isProjectShown(TestBrowser browser, Project p) {
        if (p == null) {
            return false;
        }
        String name = browser.$("#tr-" + p.getId() + " > .tr-name").first()
                .getText();
        String inst = browser.$("#tr-" + p.getId() + " > .tr-inst").first()
                .getText();
        String url = browser.$("#tr-" + p.getId() + " > .tr-url").first()
                .getText();
        String advs = browser.$("#tr-" + p.getId() + " > .tr-advs").first()
                .getText();
        String minmax = browser.$("#tr-" + p.getId() + " > .tr-minmax").first()
                .getText();
        String numTeams = browser.$("#tr-" + p.getId() + " > .tr-numTeams")
                .first().getText();
        boolean nameMatch = name.contains(p.getName());
        boolean instMatch = inst.contains(p.getInstitute());
        boolean urlMatch = url.contains(p.getProjectURL());
        boolean advsMatch = true;
        for (Adviser adv : p.getAdvisers()) {
            advsMatch = advsMatch && advs.contains(adv.getName());
        }
        boolean minmaxMatch = minmax
                .contains(p.getMinTeamSize() + " - " + p.getMaxTeamSize());
        boolean numTeamsMatch = numTeams
                .contains(Integer.toString(p.getNumberOfTeams()));
        return nameMatch && instMatch && urlMatch && advsMatch && minmaxMatch
                && numTeamsMatch;
    }
}