// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import notificationSystem.Notifier;
import play.mvc.Controller;
import play.mvc.Result;

/************************************************************/
/**
 * Dieser Controller ist zuständig für alle Http-Requests, die in dem Bereich
 * aufkommen, welche ohne Anmeldung zugänglich sind. Dazu zählt neben der
 * Index-Seite auch die Passwort vergessen-Seite und die
 * E-Mail-Verifikations-Seite.
 */
public class IndexPageController extends Controller {

    /**
     * 
     */
    private Notifier notifier;

    /**
     * Diese Methode gibt die Startseite zurück. Auf dieser Seite können sich
     * Administrator, Betreuer und Studenten anmelden oder aktuelle
     * Informationen einsehen.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result indexPage() {
        // TODO
        play.twirl.api.Html content = views.html.indexInformation
                .render("Hier könnte ihre Werbung stehen!");
        return ok(views.html.index.render(content));
    }

    /**
     * Diese Methode gibt die Seite zurück, auf der sich ein Student
     * registrieren kann.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result registerPage() {
        // TODO
        return null;
    }

    /**
     * Diese Methode initiiert die Login-Prozedur und leitet den Anzumeldenden
     * je nach Autorisierung auf die passende Seite weiter.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result login() {
        // TODO
        return null;
    }

    /**
     * Diese Methode registriert einen Studenten und fügt diesen in die
     * Datenbank ein, sofern alle notwendigen Teillestungen als bestanden
     * angegeben wurden.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result register() {
        // TODO
        return null;
    }

    /**
     * Diese Methode gibt die Seite zurück, die ein Passwort-Rücksetz-Formular
     * für Studenten und Betreuer anzeigt.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result passwordResetPage() {
        // TODO
        return null;
    }

    /**
     * Diese Methode schickt eine E-Mail anhand der Daten aus dem
     * Passwort-Rücksetz-Formular an den Studenten oder den Betreuer, welche ein
     * neues Passwort enthält.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result passwordReset() {
        // TODO
        return null;
    }

    /**
     * Diese Methode gibt die Seite zurück, welche einen Studenten verifiziert.
     * Dies funktioniert, indem der Student eine Mail mit einen Link auf diese
     * Seite erhält, welche noch einen Code als Parameter übergibt. Anhand
     * dieses Parameters wird der Student verifiziert.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result verificationPage() {
        // TODO
        return null;
    }
}
