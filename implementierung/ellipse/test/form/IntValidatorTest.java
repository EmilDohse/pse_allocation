package form;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import exception.ValidationException;

/**
 * Diese Klasse beinhaltet Tests für den InValidator.
 */
public class IntValidatorTest {

    private static IntValidator valid;

    /**
     * Test für den message setter.
     */
    @Test
    public void testSetMessage() {
        valid = new IntValidator();
        valid.setMessage("test");
        try {
            valid.validate(new String());
        } catch (ValidationException e) {
            assertEquals("test", e.getMessage());
        }
    }

    /**
     * Test auf leere Eingabe.
     * 
     * @throws ValidationException
     *             ValidationException.
     */
    @Test(expected = ValidationException.class)
    public void testEmptyString() throws ValidationException {
        valid = new IntValidator();
        String test = new String();
        valid.validate(test);
    }

    /**
     * Test auf nicht-Zahl Eingabe.
     * 
     * @throws ValidationException
     *             ValidationException.
     */
    @Test(expected = ValidationException.class)
    public void testLetterString() throws ValidationException {
        valid = new IntValidator();
        String test = "a";
        valid.validate(test);
    }

    /**
     * Test auf nicht Ganzzahl.
     * 
     * @throws ValidationException
     *             ValidationException.
     */
    @Test(expected = ValidationException.class)
    public void testFloatingString() throws ValidationException {
        valid = new IntValidator();
        String test = "2.7";
        valid.validate(test);
    }

    /**
     * Test auf Unterschreitung des minimalen Werts.
     * 
     * @throws ValidationException
     *             ValidationException.
     */
    @Test(expected = ValidationException.class)
    public void testMinValue() throws ValidationException {
        valid = new IntValidator(3);
        String test = "2";
        valid.validate(test);
    }

    /**
     * Test auf Überschreitung des maximalen Werts.
     * 
     * @throws ValidationException
     *             ValidationException.
     */
    @Test(expected = ValidationException.class)
    public void testMaxValue() throws ValidationException {
        valid = new IntValidator(0, 8);
        String test = "9";
        valid.validate(test);
    }

    /**
     * Testet die Eingabe auf Korrektheit.
     * 
     * @throws ValidationException
     *             ValidationException.
     */
    @Test
    public void testCorrectInput() throws ValidationException {
        valid = new IntValidator(0, 8);
        String test = "5";
        int number = valid.validate(test);

        assertEquals(5, number);
    }

    /**
     * Aufräumen.
     */
    @After
    public void after() {
        valid = null;
    }
}
