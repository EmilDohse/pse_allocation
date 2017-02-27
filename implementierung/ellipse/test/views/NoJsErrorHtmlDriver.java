package views;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.gargoylesoftware.htmlunit.WebClient;

public class NoJsErrorHtmlDriver extends HtmlUnitDriver {

    @Override
    protected WebClient modifyWebClient(WebClient client) {
        // currently does nothing, but may be changed in future versions
        WebClient modifiedClient = super.modifyWebClient(client);

        modifiedClient.getOptions().setThrowExceptionOnScriptError(false);
        return modifiedClient;
    }
}
