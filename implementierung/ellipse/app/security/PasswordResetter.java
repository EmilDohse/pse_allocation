package security;

import data.User;

/**
 * Diese Klasse übernimmt den Prozess des Passwort resettens. Dabei wird der
 * Klasse der User und das neue Passwort (encrypted!!!) übergeben. Daraufhin
 * wird ein Code zurückgegeben, der über ein Authentifizierungskanal (z.B.
 * E-Mail) an den Benuzter, der sein Passwort resetten will, weitergegeben wird.
 * Der Benutzer gibt den Code nun wieder ins System ein (z.B. Klicken eines
 * Links) um sich zu authentifizieren und das neue Passwort wird daraufhin ins
 * System eingetragen. Ein Code ist dabei eine Stunde lang gültig.
 * 
 * @author daniel
 *
 */
public class PasswordResetter {

    private final static PasswordResetter     instance            = new PasswordResetter();

    private static final int                  VALID_CODE_DURATION = 1;

    /**
     * Length of the verification code.
     */
    private static final int                  CODE_LENGTH         = 20;

    private final TimedCodeValueStore<User>   userStorage;
    private final TimedCodeValueStore<String> pwStorage;

    private PasswordResetter() {
        userStorage = new TimedCodeValueStore<>(VALID_CODE_DURATION,
                CODE_LENGTH);
        pwStorage = new TimedCodeValueStore<>(VALID_CODE_DURATION, CODE_LENGTH);
    }

    /**
     * Initialisiert den Passwort-Reset-Prozess (mehr Infos s. Klassen-Javadoc).
     * 
     * @param user
     *            der Benutzer
     * @param newPassword
     *            das neue Passwort (in encrypteter Form!!!)
     * @return der Authentifizierungscode
     */
    public String initializeReset(User user, String newPassword) {
        String userCode = userStorage.store(user);
        String pwCode = pwStorage.store(newPassword);
        return userCode + pwCode; // Concatenate codes
    }

    /**
     * Finalisiert den Passwort-Reset-Prozess (mehr Infos s. Klassen-Javadoc).
     * 
     * @param code
     *            der Authentifizierungscode
     * @return true wenn neues Passwort gesetzt wurde, false, falls der Code
     *         invalide ist
     */
    public boolean finalizeReset(String code) {
        // Split codes
        String userCode = code.substring(0, CODE_LENGTH);
        String pwCode = code.substring(CODE_LENGTH);
        User user = userStorage.pop(userCode);
        String pw = pwStorage.pop(pwCode);
        if (user != null && pw != null) {
            user.doTransaction(() -> {
                user.setPassword(pw);
            });
            return true;
        } else {
            return false;
        }
    }

    /**
     * Diese Methode ist Teil des Singeltons. Sie gibt die einzige Instanz des
     * PasswortResetters zurück.
     * 
     * @return die einzige Verifier-Instanz.
     */
    public static PasswordResetter getInstance() {
        return instance;
    }
}
