package views.pages.student;

import java.util.List;

import data.Project;
import play.test.TestBrowser;
import views.pages.Page;

/**
 * Diese Klasse beinhaltet Methoden zum Befüllen der Rate-Seite eines Studenten.
 */
public class StudentRatingPage extends Page {

    /**
     * Diese Methode gibt die Url der Seite zurück.
     * 
     * @return Die Url der Seite.
     */
    @Override
    public String getUrl() {
        return "/student/rating";
    }

    /**
     * Fills the rating form.
     * 
     * @param browser
     *            the browser
     * @param projects
     *            the projects to rate
     * @param rating
     *            the rating
     */
    public void fillRateForm(TestBrowser browser, List<Project> projects,
            int rating) {
        projects.forEach((p) -> {
            browser.$("#" + p.getId() + "-" + rating).first().click();
        });
        browser.$("#submit_rate").first().click();
    }
}
