package data;

import java.util.List;

import javax.persistence.Entity;

/**
 * Diese Klasse stellt einen Administrator dar
 */
@Entity
public class Administrator extends User {

    public static final String START_PASSWORD_HASH = "$2a$10$nVJZIOx02y8lk5L3N4MAhuE1QFjBKKQDEDBvTHFCc7Jmw4jQPbhG.";

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
