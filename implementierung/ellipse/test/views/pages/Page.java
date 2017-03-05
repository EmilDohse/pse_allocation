package views.pages;

import static org.junit.Assert.assertTrue;
import org.fluentlenium.core.FluentPage;

import play.test.TestBrowser;

/**
 * Diese Klasse beinhaltet Seiten-Navigagtions-Methoden.
 */
public class Page extends FluentPage {

    /**
     * Diese Methode navigiert zu einem Menüeintrag.
     * 
     * @param browser
     *            Der TestBrowser.
     * @param index
     *            Der index des bestimmten Menüeintrags.
     */
    public void gotoMenuEntry(TestBrowser browser, int index) {
        browser.$(".nav-sidebar > li > a").get(index).click();
    }

    /**
     * Diese Methode "testet" ob man sich auf der Seite befindet.
     */
    @Override
    public void isAt() {
        assertTrue(url().startsWith(getUrl()));
    }
}
