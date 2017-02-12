package form;

public class IntValidator implements Validator<Integer> {

    private int    min;
    private int    max;

    private String msg = "INTERNAL_ERROR";

    public IntValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

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
