import org.junit.Test;

import play.mvc.Http.RequestBuilder;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Result;
import play.test.WithServer;
import views.TestHelpers;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletionStage;

/**
 * Diese Klasse beinhaltet BadRequest-Tests.
 *
 */
public class BadRequestTest extends WithServer {

    /**
     * Test für einen BadRequest create während der Registrierungsphase.
     * 
     * @throws Exception
     *             Exception.
     */
    @Test
    public void indexCreateBadReq() throws Exception {
        TestHelpers.setStateToRegistration();
        testBadRequest("create");
    }

    /**
     * Test für einen BadRequest requestReset.
     * 
     * @throws Exception
     *             Exception.
     */
    @Test
    public void indexRequestResetBadReq() throws Exception {
        testBadRequest("requestReset");
    }

    /**
     * Test für einen BadRequest.
     * 
     * @param path
     *            Pfad der Seite.
     * @throws Exception
     *             Exception.
     */
    private void testBadRequest(String path) throws Exception {
        int timeout = 5000;
        String url = "http://localhost:" + this.testServer.port() + "/" + path;
        try (WSClient ws = WS.newClient(this.testServer.port())) {
            CompletionStage<WSResponse> stage = ws.url(url).post((String) null);
            WSResponse response = stage.toCompletableFuture().get();
            assertEquals(400, response.getStatus());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
