package data;

import java.util.List;

import javax.persistence.Entity;

/**
 * Diese Klasse stellt einen Administrator dar
 */
@Entity
public class Administrator extends User {

    /**
     * Das Startpasswort, was auotamtisch in die anfangs leere Datebank
     * gespeichert wird. Der Admin wird beim login dann aufgefordert das
     * Passwort zu ändern.
     */
    public static String START_PASSWORD = "adminadmin";

    public Administrator() {
        super();
    }

    public Administrator(String username, String password, String emailAddress,
            String firstName, String lastName) {
        super(username, password, emailAddress, firstName, lastName);
    }

    /**
     * Diese Methode gibt alle Administratoren zurück, die es gibt.
     * 
     * @return Liste aller Administratoren.
     */
    public static List<Administrator> getAdministrators() {
        return ElipseModel.getAll(Administrator.class);
    }
}
