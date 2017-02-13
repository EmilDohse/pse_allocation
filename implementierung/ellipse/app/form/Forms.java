package form;

public class Forms {

    public static StringValidator getPasswordValidator() {
        StringValidator validator = new StringValidator(6);
        validator.setMessage("general.error.minimalPasswordLegth");
        return validator;// TODO: Als Konstante
    }

    public static StringValidator getEmailValidator() {
        StringValidator validator = new StringValidator(1, Integer.MAX_VALUE,
                ".+@.+");
        validator.setMessage("user.noValidEmail");
        return validator;
    }

    public static StringValidator getNonEmptyStringValidator() {
        StringValidator validator = new StringValidator(1);
        validator.setMessage("general.error.noEmptyString");
        return validator;
    }
}
