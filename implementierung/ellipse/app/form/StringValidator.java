package form;

public class StringValidator implements Validator<String> {

    private int    min;
    private int    max;
    private String regex;

    private String msg = "INTERNAL_ERROR";

    public StringValidator(int min) {
        this(min, Integer.MAX_VALUE);
    }

    public StringValidator(int min, int max) {
        this(min, max, null);
    }

    public StringValidator(int min, int max, String regex) {
        this.min = min;
        this.max = max;
        this.regex = regex;
    }

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
