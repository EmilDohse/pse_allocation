import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * Simple (JUnit) tests that can call all parts of a play app. If you are
 * interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Ignore
    @Test
    public void renderTemplate() {
        // TODO warum zeigt er hier immer einen Fehler?
        // Content html = views.html.index.render("Your new application is
        // ready.");
        // assertEquals("text/html", html.contentType());
        // assertTrue(html.body().contains("Your new application is ready."));
    }

}
