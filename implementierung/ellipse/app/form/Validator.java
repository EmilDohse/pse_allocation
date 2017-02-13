package form;


/**
 * Klasse zum Validieren.
 * 
 * @param <T>
 *            Datentyp, der validiert wird.
 */
public interface Validator<T> {

    /**
     * Validiert einen String.
     * 
     * @param element
     *            String, der validiert wird.
     * @return Datentyp, der validiert wird.
     * @throws ValidationException
     */
    public T validate(String element) throws ValidationException;
}
