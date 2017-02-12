package form;

public class IntValidator implements Validator<Integer> {

    private int     min;
    private int     max;
    private boolean requiered;

    public IntValidator(boolean requiered, int min, int max) {
        this.requiered = requiered;
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer validate(String element) throws ValidationException {
        if (requiered && element.isEmpty()) {
            throw new ValidationException(""); // New Exception
        }
        int value;
        try {
            value = Integer.valueOf(element);
        } catch (NumberFormatException ex) {
            throw new ValidationException(ex.getMessage());
        }
        if (min > value || max < value) {
            throw new ValidationException(""); // TODO: Fehlermeldung
        }
        return value;
    }

}
