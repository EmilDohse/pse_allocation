package form;

import exception.ValidationException;


/**
 * Klasse um einen String zu validieren.
 */
public class StringValidator implements Validator<String> {

    private int    min;
    private int    max;
    private String regex;

    private String msg = "INTERNAL_ERROR";

    /**
     * Konstruktor mit minimaler Stringlänge.
     * 
     * @param min
     *            minimale Länge des Strings.
     */
    public StringValidator(int min) {
        this(min, Integer.MAX_VALUE);
    }

    /**
     * Konstruktor mit minimaler und maximaler Stringlänge.
     * 
     * @param min
     *            minimale Länge des Strings.
     * @param max
     *            maximale Länge des Strings.
     */
    public StringValidator(int min, int max) {
        this(min, max, null);
    }

    /**
     * Konstruktor mit minimaler und maximaler Stringlänge und Regex.
     * 
     * @param min
     *            minimale Länge des Strings.
     * @param max
     *            maximale Länge des Strings.
     * @param regex
     *            Regex, zu der der String passen muss.
     */
    public StringValidator(int min, int max, String regex) {
        this.min = min;
        this.max = max;
        this.regex = regex;
    }

    /**
     * Setzt die Errormessage.
     * 
     * @param message
     *            Errormessage.
     */
    public void setMessage(String message) {
        this.msg = message;
    }

    @Override
    public String validate(String element) throws ValidationException {
        int size = element.length();
        if (min > size || max < size) {
            throw new ValidationException(msg);
        }
        if (regex != null && !element.matches(regex)) {
            throw new ValidationException(msg);
        }
        return element;
    }

}
