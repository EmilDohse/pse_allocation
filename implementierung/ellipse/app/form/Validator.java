package form;

public interface Validator<T> {

    T validate(String element) throws ValidationException;
}
