package form;

import exception.ValidationException;


/**
 * Klasse um ein int zu validieren.
 */
public class IntValidator implements Validator<Integer> {

    private int    min;
    private int    max;

    private String msg = "INTERNAL_ERROR";

    /**
     * Konstruktor ohne Grenzen.
     */
    public IntValidator() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Konstruktor mit Minimalwert.
     * 
     * @param min
     *            Minimalwert.
     */
    public IntValidator(int min) {
        this(min, Integer.MAX_VALUE);
    }

    /**
     * Konstruktor mit Minimal- und Maximalwert.
     * 
     * @param min
     *            Minimalwert.
     * @param max
     *            Maximalwert.
     */
    public IntValidator(int min, int max) {
        this.min = min;
        this.max = max;
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
    public Integer validate(String element) throws ValidationException {
        if (element.isEmpty()) {
            throw new ValidationException(msg);
        }
        int value;
        try {
            value = Integer.valueOf(element);
        } catch (NumberFormatException ex) {
            throw new ValidationException(msg);
        }
        if (min > value || max < value) {
            throw new ValidationException(msg);
        }
        return value;
    }

}
