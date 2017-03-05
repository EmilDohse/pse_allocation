package form;

/**
 * Klasse, die spezielle Valifizierer zur Verfügung stellt.
 */
public class Forms {

    private static final int MINIMUM_PASSWORD_LENGTH = 6;

    /**
     * Utility Klasse sollte nicht instanziiert werden.
     */
    private Forms() {
    }

    /**
     * Gibt einen Passwort-Valifizierer zurück.
     * 
     * @return Passwort-Valifizierer.
     */
    public static StringValidator getPasswordValidator() {
        StringValidator validator = new StringValidator(MINIMUM_PASSWORD_LENGTH);
        validator.setMessage("general.error.minimalPasswordLength");
        return validator;
    }

    /**
     * Gibt einen Email-Valifizierer zurück.
     * 
     * @return Email-Valifizierer.
     */
    public static StringValidator getEmailValidator() {
        StringValidator validator = new StringValidator(1, Integer.MAX_VALUE,
                ".+@.+");
        validator.setMessage("user.noValidEmail");
        return validator;
    }

    /**
     * Gibt einen Valifizierer für einen nicht leeren String zurück.
     * 
     * @return Valifizierer für einen nicht leeren String.
     */
    public static StringValidator getNonEmptyStringValidator() {
        StringValidator validator = new StringValidator(1);
        validator.setMessage("general.error.noEmptyString");
        return validator;
    }
}
