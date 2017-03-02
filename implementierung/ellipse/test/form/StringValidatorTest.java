package form;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import exception.ValidationException;

public class StringValidatorTest {

    private static StringValidator valid;

    @Test
    public void testSetMessage() {
        valid = new StringValidator(1);
        valid.setMessage("test");
        try {
            valid.validate(new String());
        } catch (ValidationException e) {
            assertEquals("test", e.getMessage());
        }
    }

    @Test(expected = ValidationException.class)
    public void testMinSize() throws ValidationException {
        valid = new StringValidator(2);
        String test = "a";
        valid.validate(test);
    }

    @Test(expected = ValidationException.class)
    public void testMaxSize() throws ValidationException {
        valid = new StringValidator(0, 3);
        String test = "abcd";
        valid.validate(test);
    }

    @Test(expected = ValidationException.class)
    public void testRegex() throws ValidationException {
        valid = new StringValidator(Integer.MIN_VALUE, Integer.MAX_VALUE, "a+");
        String test = "b";
        valid.validate(test);
    }

    @Test
    public void testCorrectInput() throws ValidationException {
        valid = new StringValidator(0, 4, "a+");
        String test = "aaa";
        assertEquals(test, valid.validate(test));
    }
}
