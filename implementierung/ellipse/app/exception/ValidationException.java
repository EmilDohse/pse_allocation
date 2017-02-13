package exception;

/**
 * Exception, die geworfen wird, wen bei der Validierung ein Fehler auftritt.
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
