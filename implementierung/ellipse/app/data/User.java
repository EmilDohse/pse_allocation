// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package data;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import exception.DataException;
import security.BlowfishPasswordEncoder;

/************************************************************/
/**
 * Diese Klasse stellt einen Benutzer der Anwendung dar.
 */
@MappedSuperclass
public abstract class User extends ElipseModel implements Comparable<User> {

    /**
     * Der Anmeldename des Benutzers.
     */
    @NotNull
    private String username;
    /**
     * Das Anmeldepasswort des Benutzers.
     */
    @NotNull
    private String password;
    /**
     * Die E-Mail-Adresse des Benutzers.
     */
    @NotNull
    private String emailAddress;
    /**
     * Vorname des Benutzers.
     */
    @NotNull
    private String firstName;
    /**
     * Nachname des Benutzers.
     */
    @NotNull
    private String lastName;

    public User() throws DataException {
        this("defautl_username", "1234", "default_email", "default_firstName",
                "default_lastName");
    }

    public User(String username, String password, String emailAddress,
            String firstName, String lastName) throws DataException {
        super();
        setUserName(username);
        savePassword(password);
        setEmailAddress(emailAddress);
        setFirstName(firstName);
        setLastName(lastName);
    }

    /**
     * Getter für den Benutzernamen.
     * 
     * @return Der Benutzername.
     */
    public String getUserName() {
        return username;
    }

    /**
     * Getter für das Benutzerpasswort.
     * 
     * @return Das Benutzerpasswort.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter für die E-Mail-Addresse.
     * 
     * @return Die E_Mail-Addresse.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Getter für den Vornamen.
     * 
     * @return Der Vorname.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter für den Nachnamen.
     * 
     * @return Der Nachname.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter für den kompletten Namen, bestehend aus Vor- und Nachnamen.
     * 
     * @return Der Name.
     */
    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * Setter für den Benutzernamen.
     * 
     * @param username
     *            Der Benutzername.
     * @throws DataException
     *             Wird vom Controller behandelt.
     */
    public void setUserName(String username) throws DataException {
        if (username == null) {
            throw new DataException(IS_NULL_ERROR);
        }
        if (username.isEmpty()) {
            throw new DataException(STRING_EMPTY_ERROR);
        }
        this.username = username;
    }

    /**
     * Setter für das Benutzerpasswort. Nur wegen Ebean nicht private... Sollte
     * nicht aufgerufen werden, sondern savePassword.
     * 
     * @param password
     *            Das Passwort
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Verschlüsselt und setzt das Passwort.
     * 
     * @param password
     *            Das Passwort im Klartext.
     * @throws DataException
     *             Wird vom Controller behandelt.
     */
    public void savePassword(String password) throws DataException {
        if (password == null) {
            throw new DataException(IS_NULL_ERROR);
        }
        if (password.length() < MINIMAL_PASSWORD_LENGTH) {
            throw new DataException(MINIMAL_PASSWORD_ERROR);
        }
        setPassword(new BlowfishPasswordEncoder().encode(password));
    }

    /**
     * Setter für die E-Mail-Addresse.
     * 
     * @param email
     *            Die E-Mail-Adresse
     * @throws DataException
     *             Wird vom Controller behandelt.
     */
    public void setEmailAddress(String email) throws DataException {
        if (email == null) {
            throw new DataException(IS_NULL_ERROR);
        }
        if (!email.contains("@")) {
            throw new DataException("user.noValidEmail");
        }
        this.emailAddress = email;
    }

    /**
     * Setter für den Vornamen.
     * 
     * @param firstName
     *            Der Vorname.
     * @throws DataException
     *             Wird vom Controller behandelt.
     */
    public void setFirstName(String firstName) throws DataException {
        if (firstName == null) {
            throw new DataException(IS_NULL_ERROR);
        }
        if (firstName.isEmpty()) {
            throw new DataException(STRING_EMPTY_ERROR);
        }
        this.firstName = firstName;
    }

    /**
     * Setter für den Nachnamen.
     * 
     * @param lastName
     *            Der Nachname.
     * @throws DataException
     *             Wird vom Controller behandelt.
     */
    public void setLastName(String lastName) throws DataException {
        if (lastName == null) {
            throw new DataException(IS_NULL_ERROR);
        }
        if (lastName.isEmpty()) {
            throw new DataException(STRING_EMPTY_ERROR);
        }
        this.lastName = lastName;
    }

    @Override
    public int compareTo(User o) {
        int temp = lastName.compareTo(o.getLastName());
        if (temp == 0) {
            return firstName.compareTo(o.getFirstName());
        } else {
            return temp;
        }
    }

}
